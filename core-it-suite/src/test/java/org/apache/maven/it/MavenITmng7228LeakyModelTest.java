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

import org.apache.commons.io.FileUtils;
import org.apache.maven.shared.verifier.Verifier;
import org.apache.maven.shared.verifier.util.ResourceExtractor;
import org.junit.jupiter.api.Test;

class MavenITmng7228LeakyModelTest extends AbstractMavenIntegrationTestCase {

    protected MavenITmng7228LeakyModelTest() {
        // broken: 4.0.0-alpha-3 - 4.0.0-alpha-6
        super("[,4.0.0-alpha-3),(4.0.0-alpha-6,]");
    }

    @Test
    void testLeakyModel() throws Exception {
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/mng-7228-leaky-model");

        Verifier verifier = newVerifier(testDir.getAbsolutePath());
        verifier.setForkJvm(true);

        verifier.addCliArgument("-e");
        verifier.addCliArgument("-s");
        verifier.addCliArgument(new File(testDir, "settings.xml").getAbsolutePath());
        verifier.addCliArgument("-Pmanual-profile");

        verifier.addCliArgument("install");

        verifier.execute();

        verifier.verifyErrorFreeLog();

        String pom = FileUtils.readFileToString(
                new File(verifier.getArtifactPath("org.apache.maven.its.mng7228", "test", "1.0.0-SNAPSHOT", "pom")));

        assertTrue(pom.contains("projectProperty"));
        assertFalse(pom.contains("activeProperty"));
        assertFalse(pom.contains("manualProperty"));

        assertTrue(pom.contains("project-repo"));
        assertFalse(pom.contains("active-repoP"));
        assertFalse(pom.contains("manual-repo"));

        assertTrue(pom.contains("project-plugin-repo"));
        assertFalse(pom.contains("active-plugin-repoP"));
        assertFalse(pom.contains("manual-plugin-repo"));
    }
}
