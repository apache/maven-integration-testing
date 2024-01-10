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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.apache.maven.shared.verifier.Verifier;
import org.apache.maven.shared.verifier.util.ResourceExtractor;
import org.junit.jupiter.api.Test;

public class MavenITmng5862EntitiesAndXInclude extends AbstractMavenIntegrationTestCase {

    public MavenITmng5862EntitiesAndXInclude() {
        super("(4.0.0-alpha-7,)");
    }

    @Test
    public void testRelativePathPointsToWrongVersion() throws Exception {
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/mng-5862-entities-xinclude");

        Verifier verifier = newVerifier(testDir.getPath());
        verifier.addCliArgument("install");
        verifier.addCliArgument("-Dmaven.xinclude=true");
        verifier.execute();
        verifier.verifyErrorFreeLog();

        String path = verifier.getArtifactPath("org.apache.maven.it5862", "parent", "1.0-SNAPSHOT", "pom");
        String content = Files.lines(Paths.get(path)).collect(Collectors.joining(System.lineSeparator()));
        assertFalse(content.contains("&version;"));
        assertFalse(content.contains("xi:include"));

        path = verifier.getArtifactPath("org.apache.maven.it5862", "child", "1.0-SNAPSHOT", "pom");
        content = Files.lines(Paths.get(path)).collect(Collectors.joining(System.lineSeparator()));
        assertFalse(content.contains("&version;"));
        assertFalse(content.contains("xi:include"));
    }
}
