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
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-8181">MNG-8181</a>.
 */
public class MavenITmng8181CentralRepoTest extends AbstractMavenIntegrationTestCase {
    public MavenITmng8181CentralRepoTest() {
        super("[4.0.0-beta-4,)");
    }

    /**
     *  Verify that using the same repo id allows to override "central". This test checks the effective model.
     *
     * @throws Exception in case of failure
     */
    @Test
    public void testitModel() throws Exception {
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/mng-8181-central-repo");

        Verifier verifier = newVerifier(testDir.getAbsolutePath(), null);
        verifier.setAutoclean(false);
        verifier.addCliArgument("--install-settings=install-settings.xml");
        verifier.addCliArgument("--settings=settings.xml");
        verifier.addCliArgument("org.apache.maven.its.plugins:maven-it-plugin-expression:2.1-SNAPSHOT:eval");
        verifier.execute();
        verifier.verifyErrorFreeLog();

        verifier.setAutoclean(false);
        verifier.addCliArgument("--install-settings=install-settings.xml");
        verifier.addCliArgument("--settings=settings.xml");
        verifier.addCliArgument("-X");
        verifier.addCliArgument("-Dmaven.repo.central=http://repo1.maven.org/");
        verifier.addCliArgument("org.apache.maven.its.plugins:maven-it-plugin-expression:2.1-SNAPSHOT:eval");
        verifier.execute();
        verifier.verifyErrorFreeLog();

        verifier.verifyFilePresent("target/expression.properties");
        Properties props = verifier.loadProperties("target/expression.properties");

        assertEquals("1", props.getProperty("session.session.remoteRepositories"));
        assertEquals(
                "http://repo1.maven.org/",
                props.getProperty("session.session.remoteRepositories.0.repository.mirroredRepositories.0.url"));
    }
}
