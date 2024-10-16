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
import java.nio.file.Path;

import org.apache.maven.shared.verifier.Verifier;
import org.apache.maven.shared.verifier.util.ResourceExtractor;
import org.junit.jupiter.api.Test;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-8311">MNG-8311</a>.
 */
class MavenITmng8311EmptyLocalRepositoryTest extends AbstractMavenIntegrationTestCase {

    MavenITmng8311EmptyLocalRepositoryTest() {
        super("[4.0.0-beta-5,)");
    }

    /**
     *  Ensure that setting an empty {@code <localRepository>} in the settings.xml results the default local repository being used.
     */
    @Test
    void testitemptyLocalRepository() throws Exception {
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/mng-8311-empty-local-repository");

        Verifier verifier = newVerifier(new File(testDir, "empty-repository").getAbsolutePath());
        verifier.addCliArguments("--settings", new File(testDir, "settings.xml").toString(), "-X", "validate");
        verifier.execute();
        Path expectedLocalRepository = Path.of(System.getProperty("user.home"), ".m2", "repository");
        verifier.verifyTextInLog("Using local repository at " + expectedLocalRepository.normalize());
    }
}
