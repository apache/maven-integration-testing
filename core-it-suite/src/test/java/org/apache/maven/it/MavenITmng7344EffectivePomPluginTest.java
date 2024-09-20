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

public class MavenITmng7344EffectivePomPluginTest extends AbstractMavenIntegrationTestCase {
    public MavenITmng7344EffectivePomPluginTest() {
        super("[4.0.0-beta-4-SNAPSHOT,)");
    }

    @Test
    public void testIt() throws Exception {
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/mng-7344-effective-pom");

        Verifier verifier = newVerifier(new File(testDir, "plugin").getAbsolutePath(), "remote");
        verifier.addCliArgument("install");
        verifier.execute();
        verifier.verifyErrorFreeLog();

        for (String childFolder : new String[] {"dep-x", "bom-parent", "bom-intermediate"}) {
            verifier = newVerifier(new File(testDir, childFolder).getAbsolutePath());
            verifier.addCliArgument("install");
            verifier.execute();
            verifier.verifyErrorFreeLog();
        }

        verifier = newVerifier(new File(testDir, "consumer").getAbsolutePath());
        verifier.setForkJvm(true);
        verifier.addCliArgument("compile");
        verifier.execute();

        verifier.verifyTextInLog("[IMPORTED FROM] bom-intermediate-1.0-SNAPSHOT.pom");
    }
}