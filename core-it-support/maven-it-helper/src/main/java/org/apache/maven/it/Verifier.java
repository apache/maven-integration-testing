/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.maven.it;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.maven.executor.ExecutorException;
import org.apache.maven.executor.ExecutorHelper;
import org.apache.maven.executor.ExecutorRequest;
import org.apache.maven.executor.ExecutorResult;
import org.apache.maven.executor.embedded.EmbeddedMavenExecutor;
import org.apache.maven.executor.forked.ForkedMavenExecutor;
import org.apache.maven.shared.utils.StringUtils;
import org.apache.maven.shared.utils.io.FileUtils;

/**
 * Maven test harness for integration tests.
 *
 * <p>This is the in-repo successor of {@code org.apache.maven.shared.verifier.Verifier}
 * (maven-verifier), which is deprecated (see
 * <a href="https://github.com/apache/maven-verifier/issues/186">apache/maven-verifier#186</a>).
 * It keeps the API surface the 3.x core ITs actually use, but drives Maven through
 * {@code org.apache.maven.executor:maven-executor} instead of the bespoke launchers
 * maven-verifier used, the same way {@code apache/maven}'s {@code core-it-support/maven-it-helper}
 * does on master.</p>
 *
 * <p>Unlike the master copy, this class does not rely on a plugin execution to resolve local-repository
 * or artifact/metadata paths: it computes them locally (default repository layout only), the way
 * maven-verifier did it, so running the ITs does not require downloading a third-party plugin
 * (see <a href="https://github.com/apache/maven-executor/issues/44">apache/maven-executor#44</a>).</p>
 *
 * @author Jason van Zyl
 * @author <a href="mailto:brett@apache.org">Brett Porter</a>
 */
public class Verifier {
    private static final String LOG_FILENAME = "log.txt";

    private static final List<String> DEFAULT_CLI_ARGUMENTS = Collections.unmodifiableList(newDefaultCliArguments());

    /**
     * Command used to clean the project before execution. Neither test lifecycle binding nor prefix
     * resolution here, the goal is called directly.
     */
    private static final String CLEAN_CLI_ARGUMENT = "org.apache.maven.plugins:maven-clean-plugin:clean";

    /**
     * The Maven home/installation directory we are testing/executing with, honoring the same
     * {@code maven.home} system property the core-it-suite (and run-its.sh) has always set.
     */
    private static final java.nio.file.Path MAVEN_HOME = Paths.get(requireMavenHome());

    /**
     * Keep the executors alive as long as this class is loaded: the embedded executor keeps the
     * classworld of the Maven under test alive instead of re-creating it per invocation, which
     * makes embedded execution (the "embedded" profile) fast(er).
     */
    private static final EmbeddedMavenExecutor EMBEDDED_MAVEN_EXECUTOR = new EmbeddedMavenExecutor(MAVEN_HOME);

    private static final ForkedMavenExecutor FORKED_MAVEN_EXECUTOR = new ForkedMavenExecutor(MAVEN_HOME);

    /**
     * The preferred fork mode. Honors the same {@code verifier.forkMode} system property the
     * "embedded" profile has always set (to "auto"); absent that property (the default, non-embedded
     * run) executions are forked, matching maven-verifier's historical default of forking whenever
     * a real {@code maven.home} is configured.
     *
     * @see ExecutorHelper.Mode
     */
    private static final ExecutorHelper.Mode VERIFIER_FORK_MODE = resolveForkMode();

    public static final String USER_HOME = System.getProperty("user.home");

    public static final File USER_MAVEN_CONFIGURATION_HOME = new File(USER_HOME, ".m2");

    private final ExecutorHelper executorHelper;

    private final String basedir;

    private String localRepoOverride;

    private String resolvedLocalRepo;

    private List<String> defaultCliArguments;

    private List<String> cliArguments = new ArrayList<>();

    private Properties systemProperties = new Properties();

    private Map<String, String> environmentVariables = new HashMap<>();

    private boolean autoclean = true;

    private boolean forkJvm = false;

    private String logFileName = LOG_FILENAME;

    private String executable = ExecutorRequest.MVN;

    public Verifier(String basedir) throws VerificationException {
        this.basedir = basedir;
        this.executorHelper =
                ExecutorHelper.forExecutors(VERIFIER_FORK_MODE, EMBEDDED_MAVEN_EXECUTOR, FORKED_MAVEN_EXECUTOR);
        this.defaultCliArguments = newDefaultCliArguments();
    }

    /**
     * @deprecated the {@code debug} flag has not had any effect since maven-verifier 2.0; kept only
     *             for source compatibility with call sites written against maven-verifier.
     */
    @Deprecated
    public Verifier(String basedir, boolean debug) throws VerificationException {
        this(basedir);
    }

    private static List<String> newDefaultCliArguments() {
        List<String> args = new ArrayList<>();
        args.add("-e");
        args.add("--batch-mode");
        return args;
    }

    private static String requireMavenHome() {
        String mavenHome = System.getProperty("maven.home");
        if (mavenHome == null || mavenHome.isEmpty()) {
            throw new IllegalStateException("The maven.home system property must be set to run the Maven ITs; "
                    + "the core-it-suite POM and run-its.sh always set it, and the module POM propagates it to "
                    + "the module's own unit tests as well.");
        }
        return mavenHome;
    }

    private static ExecutorHelper.Mode resolveForkMode() {
        String forkMode = System.getProperty("verifier.forkMode");
        if (forkMode == null || forkMode.isEmpty()) {
            return ExecutorHelper.Mode.FORKED;
        }
        return ExecutorHelper.Mode.valueOf(forkMode.toUpperCase(Locale.ROOT));
    }

    public void setLocalRepo(String localRepo) {
        this.localRepoOverride = localRepo;
        this.resolvedLocalRepo = null;
    }

    /**
     * @deprecated will be removed without replacement
     */
    @Deprecated
    public void displayStreamBuffers() {}

    // ----------------------------------------------------------------------
    //
    // ----------------------------------------------------------------------

    public void verifyErrorFreeLog() throws VerificationException {
        List<String> lines = loadFile(getLogFile(), false);

        for (String line : lines) {
            // A hack to keep stupid velocity resource loader errors from triggering failure
            if (stripAnsi(line).contains("[ERROR]") && !isVelocityError(line)) {
                throw new VerificationException("Error in execution: " + line);
            }
        }
    }

    /**
     * Checks whether the specified line is just an error message from Velocity. Especially old versions of Doxia
     * employ a very noisy Velocity instance.
     *
     * @param line The log line to check, must not be <code>null</code>.
     * @return <code>true</code> if the line appears to be a Velocity error, <code>false</code> otherwise.
     */
    private static boolean isVelocityError(String line) {
        return line.contains("VM_global_library.vm") || line.contains("VM #") && line.contains("macro");
    }

    /**
     * Throws an exception if the text is not present in the log.
     *
     * @param text the text to assert present
     * @throws VerificationException if text is not found in log
     */
    public void verifyTextInLog(String text) throws VerificationException {
        List<String> lines = loadFile(getLogFile(), false);

        boolean result = false;
        for (String line : lines) {
            if (stripAnsi(line).contains(text)) {
                result = true;
                break;
            }
        }
        if (!result) {
            throw new VerificationException("Text not found in log: " + text);
        }
    }

    public static String stripAnsi(String msg) {
        return msg.replaceAll("\u001B\\[[;\\d]*[ -/]*[@-~]", "");
    }

    public Properties loadProperties(String filename) throws VerificationException {
        Properties properties = new Properties();

        File propertiesFile = new File(getBasedir(), filename);
        try (FileInputStream fis = new FileInputStream(propertiesFile)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new VerificationException("Error reading properties file", e);
        }

        return properties;
    }

    /**
     * Loads the (non-empty) lines of the specified text file.
     *
     * @param filename The path to the text file to load, relative to the base directory, must not be <code>null</code>.
     * @param encoding The character encoding of the file, may be <code>null</code> or empty to use the platform default
     *                 encoding.
     * @return The list of (non-empty) lines from the text file, can be empty but never <code>null</code>.
     * @throws IOException If the file could not be loaded.
     * @since 1.2
     */
    public List<String> loadLines(String filename, String encoding) throws IOException {
        List<String> lines = new ArrayList<>();

        try (java.io.BufferedReader reader = getReader(filename, encoding)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() > 0) {
                    lines.add(line);
                }
            }
        }

        return lines;
    }

    public List<String> loadLines(String filename) throws IOException {
        return loadLines(filename, null);
    }

    private java.io.BufferedReader getReader(String filename, String encoding) throws IOException {
        File file = new File(getBasedir(), filename);

        if (encoding != null && !encoding.isEmpty()) {
            return Files.newBufferedReader(file.toPath(), Charset.forName(encoding));
        } else {
            return Files.newBufferedReader(file.toPath());
        }
    }

    public List<String> loadFile(String basedir, String filename, boolean hasCommand) throws VerificationException {
        return loadFile(new File(basedir, filename), hasCommand);
    }

    public List<String> loadFile(File file, boolean hasCommand) throws VerificationException {
        List<String> lines = new ArrayList<>();

        if (file.exists()) {
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new FileReader(file))) {
                String line = reader.readLine();

                while (line != null) {
                    line = line.trim();

                    if (!line.startsWith("#") && line.length() != 0) {
                        lines.addAll(replaceArtifacts(line, hasCommand));
                    }
                    line = reader.readLine();
                }
            } catch (IOException e) {
                throw new VerificationException(e);
            }
        }

        return lines;
    }

    private static final String MARKER = "${artifact:";

    private List<String> replaceArtifacts(String line, boolean hasCommand) {
        int index = line.indexOf(MARKER);
        if (index >= 0) {
            String newLine = line.substring(0, index);
            index = line.indexOf("}", index);
            if (index < 0) {
                throw new IllegalArgumentException("line does not contain ending artifact marker: '" + line + "'");
            }
            String artifact = line.substring(newLine.length() + MARKER.length(), index);

            newLine += getArtifactPath(artifact);
            newLine += line.substring(index + 1);

            List<String> l = new ArrayList<>();
            l.add(newLine);

            int endIndex = newLine.lastIndexOf('/');

            String command = null;
            String filespec;
            if (hasCommand) {
                int startIndex = newLine.indexOf(' ');

                command = newLine.substring(0, startIndex);

                filespec = newLine.substring(startIndex + 1, endIndex);
            } else {
                filespec = newLine;
            }

            File dir = new File(filespec);
            addMetadataToList(dir, hasCommand, l, command);
            addMetadataToList(dir.getParentFile(), hasCommand, l, command);

            return l;
        } else {
            return Collections.singletonList(line);
        }
    }

    private static void addMetadataToList(File dir, boolean hasCommand, List<String> l, String command) {
        if (dir != null && dir.exists() && dir.isDirectory()) {
            String[] files = dir.list(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.startsWith("maven-metadata") && name.endsWith(".xml");
                }
            });

            if (files != null) {
                for (String file : files) {
                    if (hasCommand) {
                        l.add(command + " " + new File(dir, file).getPath());
                    } else {
                        l.add(new File(dir, file).getPath());
                    }
                }
            }
        }
    }

    private String getArtifactPath(String artifact) {
        StringTokenizer tok = new StringTokenizer(artifact, ":");
        if (tok.countTokens() != 4) {
            throw new IllegalArgumentException("Artifact must have 4 tokens: '" + artifact + "'");
        }

        String[] a = new String[4];
        for (int i = 0; i < 4; i++) {
            a[i] = tok.nextToken();
        }

        String groupId = a[0];
        String artifactId = a[1];
        String version = a[2];
        String ext = a[3];
        return getArtifactPath(groupId, artifactId, version, ext);
    }

    public String getArtifactPath(String groupId, String artifactId, String version, String ext) {
        return getArtifactPath(groupId, artifactId, version, ext, null);
    }

    /**
     * Returns the absolute path to the artifact denoted by groupId, artifactId, version, extension and classifier,
     * computed locally against the default repository layout (no plugin execution involved, see
     * apache/maven-executor#44).
     *
     * @param gid        The groupId, must not be null.
     * @param aid        The artifactId, must not be null.
     * @param version    The version, must not be null.
     * @param ext        The extension, must not be null.
     * @param classifier The classifier, may be null to be omitted.
     * @return the absolute path to the artifact denoted by groupId, artifactId, version, extension and classifier,
     *         never null.
     */
    public String getArtifactPath(String gid, String aid, String version, String ext, String classifier) {
        if (classifier != null && classifier.length() == 0) {
            classifier = null;
        }
        if ("maven-plugin".equals(ext)) {
            ext = "jar";
        }
        if ("coreit-artifact".equals(ext)) {
            ext = "jar";
            classifier = "it";
        }
        if ("test-jar".equals(ext)) {
            ext = "jar";
            classifier = "tests";
        }

        String repositoryPath = gid.replace('.', '/');
        repositoryPath = repositoryPath + "/" + aid + "/" + version;
        repositoryPath = repositoryPath + "/" + aid + "-" + version;
        if (classifier != null) {
            repositoryPath = repositoryPath + "-" + classifier;
        }
        repositoryPath = repositoryPath + "." + ext;

        return getLocalRepository() + "/" + repositoryPath;
    }

    public List<String> getArtifactFileNameList(String org, String name, String version, String ext) {
        List<String> files = new ArrayList<>();
        String artifactPath = getArtifactPath(org, name, version, ext);
        File dir = new File(artifactPath);
        files.add(artifactPath);
        addMetadataToList(dir, false, files, null);
        addMetadataToList(dir.getParentFile(), false, files, null);
        return files;
    }

    /**
     * Gets the path to the local artifact metadata. Note that the method does not check whether the returned path
     * actually points to existing metadata.
     *
     * @param gid     The group id, must not be <code>null</code>.
     * @param aid     The artifact id, must not be <code>null</code>.
     * @param version The artifact version, may be <code>null</code>.
     * @return The (absolute) path to the local artifact metadata, never <code>null</code>.
     */
    public String getArtifactMetadataPath(String gid, String aid, String version) {
        return getArtifactMetadataPath(gid, aid, version, "maven-metadata-local.xml");
    }

    /**
     * Gets the path to a file in the local artifact directory. Note that the method does not check whether the returned
     * path actually points to an existing file.
     *
     * @param gid      The group id, must not be <code>null</code>.
     * @param aid      The artifact id, may be <code>null</code>.
     * @param version  The artifact version, may be <code>null</code>.
     * @param filename The filename to use, must not be <code>null</code>.
     * @return The (absolute) path to the local artifact metadata, never <code>null</code>.
     */
    public String getArtifactMetadataPath(String gid, String aid, String version, String filename) {
        StringBuilder buffer = new StringBuilder(256);

        buffer.append(getLocalRepository());
        buffer.append('/');

        buffer.append(gid.replace('.', '/'));
        buffer.append('/');

        if (aid != null) {
            buffer.append(aid);
            buffer.append('/');

            if (version != null) {
                buffer.append(version);
                buffer.append('/');
            }
        }

        buffer.append(filename);

        return buffer.toString();
    }

    /**
     * Gets the path to the local artifact metadata. Note that the method does not check whether the returned path
     * actually points to existing metadata.
     *
     * @param gid The group id, must not be <code>null</code>.
     * @param aid The artifact id, must not be <code>null</code>.
     * @return The (absolute) path to the local artifact metadata, never <code>null</code>.
     */
    public String getArtifactMetadataPath(String gid, String aid) {
        return getArtifactMetadataPath(gid, aid, null);
    }

    public void deleteArtifact(String org, String name, String version, String ext) throws IOException {
        List<String> files = getArtifactFileNameList(org, name, version, ext);
        for (String fileName : files) {
            FileUtils.forceDelete(new File(fileName));
        }
    }

    /**
     * Deletes all artifacts in the specified group id from the local repository.
     *
     * @param gid The group id whose artifacts should be deleted, must not be <code>null</code>.
     * @throws IOException If the artifacts could not be deleted.
     * @since 1.2
     */
    public void deleteArtifacts(String gid) throws IOException {
        String path = gid.replace('.', '/');
        FileUtils.deleteDirectory(new File(getLocalRepository(), path));
    }

    /**
     * Deletes all artifacts in the specified g:a:v from the local repository.
     *
     * @param gid     The group id whose artifacts should be deleted, must not be <code>null</code>.
     * @param aid     The artifact id whose artifacts should be deleted, must not be <code>null</code>.
     * @param version The (base) version whose artifacts should be deleted, must not be <code>null</code>.
     * @throws IOException If the artifacts could not be deleted.
     * @since 1.3
     */
    public void deleteArtifacts(String gid, String aid, String version) throws IOException {
        String path = gid.replace('.', '/') + '/' + aid + '/' + version;
        FileUtils.deleteDirectory(new File(getLocalRepository(), path));
    }

    /**
     * Deletes the specified directory.
     *
     * @param path The path to the directory to delete, relative to the base directory, must not be <code>null</code>.
     * @throws IOException If the directory could not be deleted.
     * @since 1.2
     */
    public void deleteDirectory(String path) throws IOException {
        FileUtils.deleteDirectory(new File(getBasedir(), path));
    }

    /**
     * Filters a text file by replacing some user-defined tokens.
     * This method is equivalent to:
     *
     * <pre>
     *     filterFile( srcPath, dstPath, fileEncoding, verifier.newDefaultFilterMap() )
     * </pre>
     *
     * @param srcPath          The path to the input file, relative to the base directory, must not be
     *                         <code>null</code>.
     * @param dstPath          The path to the output file, relative to the base directory and possibly equal to the
     *                         input file, must not be <code>null</code>.
     * @param fileEncoding     The file encoding to use, may be <code>null</code> or empty to use the platform's default
     *                         encoding.
     * @return The path to the filtered output file, never <code>null</code>.
     * @throws IOException If the file could not be filtered.
     * @since 2.0
     */
    public File filterFile(String srcPath, String dstPath, String fileEncoding) throws IOException {
        return filterFile(srcPath, dstPath, fileEncoding, newDefaultFilterMap());
    }

    /**
     * Filters a text file by replacing some user-defined tokens.
     *
     * @param srcPath      The path to the input file, relative to the base directory, must not be
     *                     <code>null</code>.
     * @param dstPath      The path to the output file, relative to the base directory and possibly equal to the
     *                     input file, must not be <code>null</code>.
     * @param fileEncoding The file encoding to use, may be <code>null</code> or empty to use the platform's default
     *                     encoding.
     * @param filterMap    The mapping from tokens to replacement values, must not be <code>null</code>.
     * @return The path to the filtered output file, never <code>null</code>.
     * @throws IOException If the file could not be filtered.
     * @since 1.2
     */
    /**
     * @deprecated use {@link #filterFile(String, String, String, Map)}
     */
    @Deprecated
    @SuppressWarnings({"rawtypes", "unchecked"})
    public File filterFile(String srcPath, String dstPath, String fileEncoding, Properties filterProperties)
            throws IOException {
        return filterFile(srcPath, dstPath, fileEncoding, (Map) filterProperties);
    }

    public File filterFile(String srcPath, String dstPath, String fileEncoding, Map<String, String> filterMap)
            throws IOException {
        File srcFile = new File(getBasedir(), srcPath);
        String data = FileUtils.fileRead(srcFile, fileEncoding);

        for (Map.Entry<String, String> entry : filterMap.entrySet()) {
            data = StringUtils.replace(data, entry.getKey(), entry.getValue());
        }

        File dstFile = new File(getBasedir(), dstPath);
        //noinspection ResultOfMethodCallIgnored
        dstFile.getParentFile().mkdirs();
        FileUtils.fileWrite(dstFile.getPath(), fileEncoding, data);

        return dstFile;
    }

    /**
     * Gets a new copy of the default filter map. These default filter map, contains the tokens "@basedir@" and
     * "@baseurl@" to the test's base directory and its base <code>file:</code> URL, respectively.
     *
     * @return The (modifiable) map with the default filter map, never <code>null</code>.
     * @since 2.0
     */
    public Map<String, String> newDefaultFilterMap() {
        Map<String, String> filterMap = new HashMap<>();

        java.nio.file.Path basedir = Paths.get(getBasedir()).toAbsolutePath();
        filterMap.put("@basedir@", basedir.toString());
        filterMap.put("@baseurl@", basedir.toUri().toASCIIString());

        return filterMap;
    }

    /**
     * Verifies that the given file exists.
     *
     * @param file the path of the file to check
     * @throws VerificationException in case the given file does not exist
     */
    public void verifyFilePresent(String file) throws VerificationException {
        verifyFilePresence(file, true);
    }

    /**
     * Verifies that the given file does not exist.
     *
     * @param file the path of the file to check
     * @throws VerificationException if the given file exists
     */
    public void verifyFileNotPresent(String file) throws VerificationException {
        verifyFilePresence(file, false);
    }

    private void verifyArtifactPresence(boolean wanted, String groupId, String artifactId, String version, String ext)
            throws VerificationException {
        List<String> files = getArtifactFileNameList(groupId, artifactId, version, ext);
        for (String fileName : files) {
            verifyFilePresence(fileName, wanted);
        }
    }

    /**
     * Verifies that the artifact given through its Maven coordinates exists.
     *
     * @param groupId the groupId of the artifact (must not be null)
     * @param artifactId the artifactId of the artifact (must not be null)
     * @param version the version of the artifact (must not be null)
     * @param ext the extension of the artifact (must not be null)
     * @throws VerificationException if the given artifact does not exist
     */
    public void verifyArtifactPresent(String groupId, String artifactId, String version, String ext)
            throws VerificationException {
        verifyArtifactPresence(true, groupId, artifactId, version, ext);
    }

    /**
     * Verifies that the artifact given through its Maven coordinates does not exist.
     *
     * @param groupId the groupId of the artifact (must not be null)
     * @param artifactId the artifactId of the artifact (must not be null)
     * @param version the version of the artifact (must not be null)
     * @param ext the extension of the artifact (must not be null)
     * @throws VerificationException if the given artifact exists
     */
    public void verifyArtifactNotPresent(String groupId, String artifactId, String version, String ext)
            throws VerificationException {
        verifyArtifactPresence(false, groupId, artifactId, version, ext);
    }

    private void verifyFilePresence(String filePath, boolean wanted) throws VerificationException {
        if (filePath.contains("!/")) {
            Path basedir = Paths.get(getBasedir()).toAbsolutePath();
            String urlString = "jar:" + basedir.toUri().toASCIIString() + "/" + filePath;

            InputStream is = null;
            try {
                URL url = new URL(urlString);

                is = url.openStream();

                if (is == null) {
                    if (wanted) {
                        throw new VerificationException("Expected JAR resource was not found: " + filePath);
                    }
                } else {
                    if (!wanted) {
                        throw new VerificationException("Unwanted JAR resource was found: " + filePath);
                    }
                }
            } catch (MalformedURLException e) {
                throw new VerificationException("Error looking for JAR resource", e);
            } catch (IOException e) {
                if (wanted) {
                    throw new VerificationException("Error looking for JAR resource: " + filePath);
                }
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        // ignore
                    }
                }
            }
        } else {
            File expectedFile = new File(filePath);

            // NOTE: On Windows, a path with a leading (back-)slash is relative to the current drive
            if (!expectedFile.isAbsolute() && !expectedFile.getPath().startsWith(File.separator)) {
                expectedFile = new File(getBasedir(), filePath);
            }

            if (filePath.indexOf('*') > -1) {
                File parent = expectedFile.getParentFile();

                if (!parent.exists()) {
                    if (wanted) {
                        throw new VerificationException(
                                "Expected file pattern was not found: " + expectedFile.getPath());
                    }
                } else {
                    String shortNamePattern = expectedFile.getName().replaceAll("\\*", ".*");

                    String[] candidates = parent.list();

                    boolean found = false;

                    if (candidates != null) {
                        for (String candidate : candidates) {
                            if (candidate.matches(shortNamePattern)) {
                                found = true;
                                break;
                            }
                        }
                    }

                    if (!found && wanted) {
                        throw new VerificationException(
                                "Expected file pattern was not found: " + expectedFile.getPath());
                    } else if (found && !wanted) {
                        throw new VerificationException("Unwanted file pattern was found: " + expectedFile.getPath());
                    }
                }
            } else {
                if (!expectedFile.exists()) {
                    if (wanted) {
                        throw new VerificationException("Expected file was not found: " + expectedFile.getPath());
                    }
                } else {
                    if (!wanted) {
                        throw new VerificationException("Unwanted file was found: " + expectedFile.getPath());
                    }
                }
            }
        }
    }

    /**
     * Verifies that the artifact given by its Maven coordinates exists and contains the given content.
     *
     * @param groupId the groupId of the artifact (must not be null)
     * @param artifactId the artifactId of the artifact (must not be null)
     * @param version the version of the artifact (must not be null)
     * @param ext the extension of the artifact (must not be null)
     * @param content the expected content
     * @throws IOException if reading from the artifact fails
     * @throws VerificationException if the content of the artifact differs
     */
    public void verifyArtifactContent(String groupId, String artifactId, String version, String ext, String content)
            throws IOException, VerificationException {
        String fileName = getArtifactPath(groupId, artifactId, version, ext);
        if (!content.equals(FileUtils.fileRead(fileName))) {
            throw new VerificationException("Content of " + fileName + " does not equal " + content);
        }
    }

    // ----------------------------------------------------------------------
    //
    // ----------------------------------------------------------------------

    /**
     * Execute Maven.
     */
    public void execute() throws VerificationException {
        List<String> args = new ArrayList<>(defaultCliArguments);

        args.add("-Dmaven.repo.local=" + getLocalRepository());

        if (autoclean) {
            args.add(CLEAN_CLI_ARGUMENT);
        }

        for (Map.Entry<Object, Object> entry : systemProperties.entrySet()) {
            args.add("-D" + entry.getKey() + "=" + entry.getValue());
        }

        for (String cliArgument : cliArguments) {
            args.add(cliArgument.replace("${basedir}", getBasedir()));
        }

        File logFile = getLogFile();

        // Capture the real stdout/stderr of the Maven-under-test and write both into the log file,
        // exactly as maven-verifier's ForkedLauncher/Embedded3xLauncher did (both streams into the
        // same writer). Unlike a "-l <file>" CLI argument, this does not fight with a CLI argument
        // list that (rarely) adds its own "-l" (e.g. MavenITmng3183LoggingToFileTest), since it
        // captures whatever actually reaches the process' own stdout/stderr rather than competing
        // with it for the same log destination. Each pump thread in maven-executor closes whatever
        // stream it is given, so two private ByteArrayOutputStreams are used (close() on those is a
        // harmless no-op) and copied into the real log file afterward, under our own control.
        ByteArrayOutputStream stdOut = new ByteArrayOutputStream();
        ByteArrayOutputStream stdErr = new ByteArrayOutputStream();

        try {
            ExecutorRequest.Builder builder = ExecutorRequest.mavenBuilder()
                    .command(executable)
                    .cwd(Paths.get(basedir))
                    .arguments(args)
                    .skipMavenRc(true)
                    .stdOut(stdOut)
                    .stdErr(stdErr);
            if (!environmentVariables.isEmpty()) {
                builder.environmentVariables(environmentVariables);
            }

            ExecutorHelper.Mode mode = executorHelper.getDefaultMode();
            if (forkJvm) {
                mode = ExecutorHelper.Mode.FORKED;
            }

            ExecutorRequest request = builder.build();
            ExecutorResult result = executorHelper.execute(mode, request);

            writeLogFile(logFile, stdOut, stdErr);

            if (!result.success()) {
                throw new VerificationException("Exit code was non-zero: "
                        + result.exitCode().orElse(-1) + "; command line and log = \n" + getExecutable() + " "
                        + StringUtils.join(args.iterator(), " ") + "\n" + getLogContents(logFile));
            }
        } catch (ExecutorException e) {
            writeLogFile(logFile, stdOut, stdErr);
            throw new VerificationException("Failed to execute Maven", e);
        }
    }

    private static void writeLogFile(File logFile, ByteArrayOutputStream stdOut, ByteArrayOutputStream stdErr)
            throws VerificationException {
        try (FileOutputStream fos = new FileOutputStream(logFile)) {
            stdOut.writeTo(fos);
            stdErr.writeTo(fos);
        } catch (IOException e) {
            throw new VerificationException("Could not write log file: " + logFile, e);
        }
    }

    public String getMavenVersion() throws VerificationException {
        try {
            return executorHelper.mavenVersion();
        } catch (ExecutorException e) {
            throw new VerificationException("Failed to determine Maven version", e);
        }
    }

    private static String getLogContents(File logFile) {
        try {
            return FileUtils.fileRead(logFile);
        } catch (IOException e) {
            // ignore
            return "(Error reading log contents: " + e.getMessage() + ")";
        }
    }

    /**
     * Resolves the effective local repository, in order: an explicit {@link #setLocalRepo(String)} override,
     * a {@code -Dmaven.repo.local=} CLI argument already added to this verifier, the {@code maven.repo.local}
     * system property (set by the run-its.sh/POM invocation), or else {@code ~/.m2/repository}.
     */
    public String getLocalRepository() {
        if (localRepoOverride != null) {
            return localRepoOverride;
        }
        if (resolvedLocalRepo == null) {
            resolvedLocalRepo = resolveLocalRepository();
        }
        return resolvedLocalRepo;
    }

    private String resolveLocalRepository() {
        String prefix = "-Dmaven.repo.local=";
        for (String cliArgument : cliArguments) {
            if (cliArgument.startsWith(prefix)) {
                return cliArgument.substring(prefix.length());
            }
        }

        String repo = System.getProperty("maven.repo.local");
        if (repo != null && !repo.isEmpty()) {
            return repo;
        }

        return USER_HOME + File.separator + ".m2" + File.separator + "repository";
    }

    /**
     * @deprecated will be removed without replacement,
     * for arguments adding please use {@link #addCliArgument(String)}, {@link #addCliArguments(String...)}
     */
    @Deprecated
    public void setCliOptions(List<String> cliOptions) {
        this.cliArguments = cliOptions;
    }

    /**
     * Add a command line argument, each argument must be set separately one by one.
     * <p>
     * <code>${basedir}</code> in argument will be replaced by value of {@link #getBasedir()} during execution.
     *
     * @param cliArgument an argument to add
     */
    public void addCliArgument(String cliArgument) {
        cliArguments.add(cliArgument);
    }

    /**
     * Add a command line arguments, each argument must be set separately one by one.
     * <p>
     * <code>${basedir}</code> in argument will be replaced by value of {@link #getBasedir()} during execution.
     *
     * @param cliArguments an arguments list to add
     */
    public void addCliArguments(String... cliArguments) {
        Collections.addAll(this.cliArguments, cliArguments);
    }

    public Properties getSystemProperties() {
        return systemProperties;
    }

    public void setSystemProperty(String key, String value) {
        if (value != null) {
            systemProperties.setProperty(key, value);
        } else {
            systemProperties.remove(key);
        }
    }

    public void setEnvironmentVariable(String key, String value) {
        if (value != null) {
            environmentVariables.put(key, value);
        } else {
            environmentVariables.remove(key);
        }
    }

    public boolean isAutoclean() {
        return autoclean;
    }

    /**
     * Clean project before execution by adding {@link #CLEAN_CLI_ARGUMENT} to command line.
     * <p>
     * By default, options is enabled.
     *
     * @param autoclean indicate if option is enabled
     */
    public void setAutoclean(boolean autoclean) {
        this.autoclean = autoclean;
    }

    public String getBasedir() {
        return basedir;
    }

    /**
     * Gets the name of the file used to log build output.
     *
     * @return The name of the log file, relative to the base directory, never <code>null</code>.
     * @since 1.2
     */
    public String getLogFileName() {
        return this.logFileName;
    }

    /**
     * Sets the name of the file used to log build output.
     *
     * @param logFileName The name of the log file, relative to the base directory, must not be empty or
     *                    <code>null</code>.
     * @since 1.2
     */
    public void setLogFileName(String logFileName) {
        if (logFileName == null || logFileName.isEmpty()) {
            throw new IllegalArgumentException("log file name unspecified");
        }
        this.logFileName = logFileName;
    }

    private File getLogFile() {
        return new File(getBasedir(), logFileName);
    }

    public void setForkJvm(boolean forkJvm) {
        this.forkJvm = forkJvm;
    }

    public String getLocalRepoLayout() {
        return "default";
    }

    public String getExecutable() {
        return executable;
    }

    public void setExecutable(String executable) {
        this.executable = executable;
    }
}
