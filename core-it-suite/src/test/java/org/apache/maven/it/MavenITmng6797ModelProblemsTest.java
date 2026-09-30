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

import java.io.File;
import java.util.Properties;

import org.apache.maven.shared.verifier.Verifier;
import org.apache.maven.shared.verifier.util.ResourceExtractor;
import org.junit.jupiter.api.Test;

/**
 * Verifies that plugins can inspect retained model problems, for example to enforce warning-free models.
 */
public class MavenITmng6797ModelProblemsTest extends AbstractMavenIntegrationTestCase {

    public MavenITmng6797ModelProblemsTest() {
        super("[3.10.0-SNAPSHOT,4.0.0-alpha-1)");
    }

    @Test
    public void testValidModel() throws Exception {
        Verifier verifier = execute("valid", false);

        Properties properties = verifier.loadProperties("target/model-problems.properties");
        assertEquals("0", properties.getProperty("session.modelProblems"));
    }

    @Test
    public void testModelProblems() throws Exception {
        Verifier verifier = execute("warnings", false);

        verifier.verifyTextInLog("duplicate declaration of plugin");
        assertModelProblem(verifier, "target/model-problems.properties", "warnings", "pom.xml");
    }

    @Test
    public void testModelProblemsWithQuietLogging() throws Exception {
        Verifier verifier = execute("warnings", true);

        // Suppressing warning output must not hide model problems from plugins.
        assertFalse(verifier.loadLines(verifier.getLogFileName(), "UTF-8").stream()
                .anyMatch(line -> line.contains("[WARNING]")));
        assertModelProblem(verifier, "target/model-problems.properties", "warnings", "pom.xml");
    }

    @Test
    public void testModelProblemsInChild() throws Exception {
        Verifier verifier = execute("reactor", false);

        verifier.verifyTextInLog("duplicate declaration of plugin");
        // Problems in a child must already be visible to a plugin executing in the clean root project.
        assertModelProblem(verifier, "target/model-problems.properties", "child", "child/pom.xml");
        assertModelProblem(verifier, "child/target/model-problems.properties", "child", "child/pom.xml");
    }

    private Verifier execute(String project, boolean quiet) throws Exception {
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/mng-6797/" + project);
        Verifier verifier = newVerifier(testDir.getAbsolutePath());
        verifier.setAutoclean(false);
        verifier.deleteDirectory("target");
        verifier.deleteDirectory("child/target");
        verifier.setLogFileName(quiet ? "log-quiet.txt" : "log.txt");
        if (quiet) {
            verifier.addCliArgument("-q");
        }
        verifier.addCliArgument("-Dexpression.outputFile=target/model-problems.properties");
        verifier.addCliArgument(
                "-Dexpression.expressions=session/modelProblems,session/modelProblems/*/severity/toString");
        verifier.addCliArgument("org.apache.maven.its.plugins:maven-it-plugin-expression:2.1-SNAPSHOT:eval");
        verifier.execute();
        verifier.verifyErrorFreeLog();
        return verifier;
    }

    private void assertModelProblem(Verifier verifier, String output, String artifactId, String pom) throws Exception {
        Properties properties = verifier.loadProperties(output);
        assertEquals("1", properties.getProperty("session.modelProblems"));
        assertEquals("WARNING", properties.getProperty("session.modelProblems.0.severity.toString"));
        String message = properties.getProperty("session.modelProblems.0.message");
        assertNotNull(message);
        assertTrue(message.contains("duplicate declaration of plugin"));
        assertTrue(message.contains("org.apache.maven.its.plugins:maven-it-plugin-expression"));
        assertEquals(
                "org.apache.maven.its.mng6797:" + artifactId + ":1.0",
                properties.getProperty("session.modelProblems.0.modelId"));
        String source = properties.getProperty("session.modelProblems.0.source");
        assertNotNull(source);
        assertEquals(new File(verifier.getBasedir(), pom).getCanonicalFile(), new File(source).getCanonicalFile());
        assertTrue(Integer.parseInt(properties.getProperty("session.modelProblems.0.lineNumber")) > 0);
        assertTrue(Integer.parseInt(properties.getProperty("session.modelProblems.0.columnNumber")) > 0);
    }
}
