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

import org.apache.maven.shared.verifier.Verifier;
import org.apache.maven.shared.verifier.util.ResourceExtractor;
import org.junit.jupiter.api.Test;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-8041">MNG-8041</a>.
 */
class MavenITmng8041DependencyResolutionScope extends AbstractMavenIntegrationTestCase {

    MavenITmng8041DependencyResolutionScope() {
        super("[4.0.0-beta-5,)");
    }

    /**
     *  Verify the dependency tree resolution
     */
    @Test
    void testScopeResolution() throws Exception {
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/mng-8041-resolution-scope");

        Verifier verifier = newVerifier(testDir.getAbsolutePath());
        verifier.addCliArgument("install");
        verifier.execute();
        verifier.verifyErrorFreeLog();

        List<String> lines = verifier.loadFile(verifier.getBasedir(), verifier.getLogFileName(), false);
        assertEquals(
                2l,
                lines.stream()
                        .filter(l -> l.contains("aopalliance:aopalliance:jar:1.0"))
                        .count());
    }
}
