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
import java.util.List;

import org.apache.maven.shared.verifier.VerificationException;
import org.apache.maven.shared.verifier.Verifier;
import org.apache.maven.shared.verifier.util.ResourceExtractor;
import org.junit.jupiter.api.Test;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-4379">MNG-4379</a>.
 *
 * @author Benjamin Bentmann
 */
public class MavenITmng4379TransitiveSystemPathInterpolatedWithEnvVarTest extends AbstractMavenIntegrationTestCase {

    public MavenITmng4379TransitiveSystemPathInterpolatedWithEnvVarTest() {
        super("[2.0.3,2.1.0),[3.0-alpha-6,3.10.0-alpha-1)");
    }

    /**
     * Test that the path of a system-scope dependency gets interpolated using environment variables during
     * transitive dependency resolution.
     * <p>
     * Since 3.10.0-rc-2, repository-resolved models restrict property
     * interpolation by default, so the opt-out flag is required for the
     * legacy behavior and the default behavior causes a build failure.
     *
     * @throws Exception in case of failure
     */
    @Test
    public void testit() throws Exception {
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/mng-4379");

        Verifier verifier = newVerifier(testDir.getAbsolutePath());
        verifier.setAutoclean(false);
        verifier.deleteDirectory("target");
        verifier.deleteArtifacts("org.apache.maven.its.mng4379");
        verifier.filterFile("settings-template.xml", "settings.xml", "UTF-8");
        verifier.setEnvironmentVariable("MNG_4379_HOME", testDir.getAbsolutePath());
        verifier.addCliArgument("-s");
        verifier.addCliArgument("settings.xml");
        verifier.addCliArguments("validate");

        String mavenVersion = getMavenVersion() != null ? getMavenVersion().toString() : "";
        if (mavenVersion.equals("3.10.0-rc-1") || matchesVersionRange("(,3.10.0)")) {
            // Before restricted interpolation: env var interpolation works without opt-out
            verifier.execute();
            verifier.verifyErrorFreeLog();

            List<String> classpath = verifier.loadLines("target/classpath.txt", "UTF-8");
            assertTrue(classpath.toString(), classpath.contains("pom.xml"));
        } else {
            // With restricted interpolation: build fails without opt-out
            try {
                verifier.execute();
                verifier.verifyErrorFreeLog();
                fail("Build should not succeed without -Dmaven.model.dependencyInterpolation.full=true");
            } catch (VerificationException e) {
                verifier.verifyTextInLog("must specify an absolute path but is ${env.MNG_4379_HOME}/pom.xml");
            }
        }
    }

    /**
     * Test that the path of a system-scope dependency gets interpolated using environment variables when
     * the opt-out property {@code -Dmaven.model.dependencyInterpolation.full=true} is set.
     *
     * @throws Exception in case of failure
     */
    @Test
    public void testitWithFullInterpolation() throws Exception {
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/mng-4379");

        Verifier verifier = newVerifier(testDir.getAbsolutePath());
        verifier.setAutoclean(false);
        verifier.deleteDirectory("target");
        verifier.deleteArtifacts("org.apache.maven.its.mng4379");
        verifier.filterFile("settings-template.xml", "settings.xml", "UTF-8");
        verifier.setEnvironmentVariable("MNG_4379_HOME", testDir.getAbsolutePath());
        verifier.addCliArgument("-s");
        verifier.addCliArgument("settings.xml");
        verifier.addCliArguments("validate");
        verifier.addCliArgument("-Dmaven.model.dependencyInterpolation.full=true");
        verifier.execute();
        verifier.verifyErrorFreeLog();

        List<String> classpath = verifier.loadLines("target/classpath.txt", "UTF-8");
        assertTrue(classpath.toString(), classpath.contains("pom.xml"));
    }
}
