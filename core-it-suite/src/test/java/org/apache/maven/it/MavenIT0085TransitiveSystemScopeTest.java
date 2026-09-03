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
import java.util.Collection;

import org.apache.maven.shared.verifier.VerificationException;
import org.apache.maven.shared.verifier.Verifier;
import org.apache.maven.shared.verifier.util.ResourceExtractor;
import org.junit.jupiter.api.Test;

public class MavenIT0085TransitiveSystemScopeTest extends AbstractMavenIntegrationTestCase {
    public MavenIT0085TransitiveSystemScopeTest() {
        super(ALL_MAVEN_VERSIONS);
    }

    /**
     * Verify that system-scoped dependencies get resolved with system scope
     * when they are resolved transitively via another (non-system)
     * dependency. Inherited scope should not apply in the case of
     * system-scoped dependencies, no matter where they are.
     * <p>
     * Since 3.10.0-rc-2, repository-resolved models restrict property
     * interpolation by default, so the opt-out flag is required for the
     * legacy behavior and the default behavior causes a build failure.
     *
     * @throws Exception in case of failure
     */
    @Test
    public void testit0085() throws Exception {
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/it0085");

        Verifier verifier = newVerifier(testDir.getAbsolutePath());
        verifier.setAutoclean(false);
        verifier.deleteDirectory("target");
        verifier.deleteArtifacts("org.apache.maven.its.it0085");
        verifier.getSystemProperties().setProperty("test.home", testDir.getAbsolutePath());
        verifier.filterFile("settings-template.xml", "settings.xml", "UTF-8");
        verifier.addCliArgument("--settings");
        verifier.addCliArgument("settings.xml");
        verifier.addCliArgument("validate");

        String mavenVersion = getMavenVersion() != null ? getMavenVersion().toString() : "";
        if (mavenVersion.equals("3.10.0-rc-1") || matchesVersionRange("(,3.10.0)")) {
            // Before restricted interpolation: full interpolation works without opt-out
            verifier.execute();
            verifier.verifyErrorFreeLog();
        } else {
            // With restricted interpolation: build fails without opt-out
            try {
                verifier.execute();
                verifier.verifyErrorFreeLog();
                fail("Build should not succeed without -Dmaven.model.dependencyInterpolation.full=true");
            } catch (VerificationException e) {
                verifier.verifyTextInLog("must specify an absolute path but is ${test.home}/system.jar");
            }
        }
    }

    /**
     * Verify that system-scoped dependencies work correctly when the opt-out
     * property {@code -Dmaven.model.dependencyInterpolation.full=true} is set.
     *
     * @throws Exception in case of failure
     */
    @Test
    public void testit0085WithFullInterpolation() throws Exception {
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/it0085");

        Verifier verifier = newVerifier(testDir.getAbsolutePath());
        verifier.setAutoclean(false);
        verifier.deleteDirectory("target");
        verifier.deleteArtifacts("org.apache.maven.its.it0085");
        verifier.getSystemProperties().setProperty("test.home", testDir.getAbsolutePath());
        verifier.filterFile("settings-template.xml", "settings.xml", "UTF-8");
        verifier.addCliArgument("--settings");
        verifier.addCliArgument("settings.xml");
        verifier.addCliArgument("validate");
        verifier.addCliArgument("-Dmaven.model.dependencyInterpolation.full=true");
        verifier.execute();
        verifier.verifyErrorFreeLog();

        Collection<String> lines = verifier.loadLines("target/test.txt", "UTF-8");
        assertTrue(lines.toString(), lines.contains("system.jar"));
    }
}
