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

import org.apache.maven.shared.verifier.Verifier;
import org.apache.maven.shared.verifier.util.ResourceExtractor;
import org.junit.jupiter.api.Test;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-5135">MNG-5135</a>.
 *
 * @author Benjamin Bentmann
 */
public class MavenITmng5102MixinsTest extends AbstractMavenIntegrationTestCase {

    public MavenITmng5102MixinsTest() {
        super("(4.0.0-alpha-7,)");
    }

    /**
     * Verify that mixins can be loaded from the file system.
     *
     * @throws Exception in case of failure
     */
    @Test
    public void testWithPath() throws Exception {
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/mng-5102-mixins/path");

        Verifier verifier = newVerifier(testDir.getAbsolutePath());
        verifier.setAutoclean(false);
        verifier.deleteDirectory("target");
        verifier.deleteArtifacts("org.apache.maven.its.mng5102");
        verifier.addCliArgument("install");
        verifier.execute();
        verifier.verifyErrorFreeLog();
    }

    /**
     * Verify that mixins can be loaded from the repositories.
     *
     * @throws Exception in case of failure
     */
    @Test
    public void testWithGav() throws Exception {
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/mng-5102-mixins/gav");

        Verifier verifier = newVerifier(new File(testDir, "mixin-2").getAbsolutePath());

        verifier.setAutoclean(false);
        verifier.deleteDirectory("target");
        verifier.deleteArtifacts("org.apache.maven.its.mng5102");
        verifier.addCliArgument("install");
        verifier.execute();
        verifier.verifyErrorFreeLog();

        verifier = newVerifier(new File(testDir, "project").getAbsolutePath());
        verifier.setAutoclean(false);
        verifier.deleteDirectory("target");
        verifier.addCliArgument("install");
        verifier.execute();
        verifier.verifyErrorFreeLog();
    }
}
