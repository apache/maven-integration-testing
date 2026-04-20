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
 * This is a test check that color always works.
 */
class MavenITgh11969ColorAlwaysTest extends AbstractMavenIntegrationTestCase {

    MavenITgh11969ColorAlwaysTest() {
        super(ALL_MAVEN_VERSIONS);
    }

    @Test
    void testIt() throws Exception {
        File basedir = ResourceExtractor.simpleExtractResources(getClass(), "/gh-11969-color-always");

        Verifier verifier = newVerifier(basedir.getAbsolutePath());
        verifier.addCliArguments("-color=always");
        verifier.addCliArguments("validate");
        verifier.addCliArguments("-X");
        // only fork mode supports color output
        verifier.setForkJvm(true);
        verifier.execute();
        verifier.verifyErrorFreeLog();

        List<String> lines = verifier.loadLines(verifier.getLogFileName(), "UTF-8");
        assertTrue(
                String.join(System.lineSeparator(), lines),
                lines.stream().anyMatch(l -> l.contains("\u001B[1mApache Maven")));
        assertTrue(
                String.join(System.lineSeparator(), lines),
                lines.stream().anyMatch(l -> l.contains("[\u001B[36;1mDEBUG\u001B[0m]")));
        assertTrue(
                String.join(System.lineSeparator(), lines),
                lines.stream().anyMatch(l -> l.contains("[\u001B[34;1mINFO\u001B[0m]")));
    }
}
