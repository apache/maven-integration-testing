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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Verify that the debug level of extensions is respected.
 * <a href="https://github.com/apache/maven/issues/12668">gh-12668</a>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MavenITgh12668ExtensionDebugLevel extends AbstractMavenIntegrationTestCase {

    public MavenITgh12668ExtensionDebugLevel() {
        super("[3.9.0,)");
    }

    @Test
    @Order(1)
    public void installExtension() throws Exception {
        File projectDir = ResourceExtractor.simpleExtractResources(getClass(), "/gh-12668-extension-debug-level");

        Verifier extensionVerifier = newVerifier(new File(projectDir, "extension").getAbsolutePath());
        extensionVerifier.addCliArgument("install");
        extensionVerifier.execute();
        extensionVerifier.verifyErrorFreeLog();
    }

    @Test
    public void projectBuildExtensionDebug() throws Exception {
        File projectDir = ResourceExtractor.simpleExtractResources(getClass(), "/gh-12668-extension-debug-level");

        Verifier verifier = newVerifier(new File(projectDir, "project-build").getAbsolutePath());
        verifier.addCliArgument("validate");
        verifier.addCliArgument("-X");
        verifier.setLogFileName("debug.log");
        verifier.execute();
        verifier.verifyErrorFreeLog();

        verifier.verifyTextInLog("[DEBUG] extension afterProjectsRead called");
        verifier.verifyTextInLog("[INFO] extension afterProjectsRead called");

        verifier.verifyTextInLog("[DEBUG] extension afterSessionEnd called");
        verifier.verifyTextInLog("[INFO] extension afterSessionEnd called");
    }

    @Test
    public void projectBuildExtensionInfo() throws Exception {
        File projectDir = ResourceExtractor.simpleExtractResources(getClass(), "/gh-12668-extension-debug-level");

        Verifier verifier = newVerifier(new File(projectDir, "project-build").getAbsolutePath());
        verifier.addCliArgument("validate");
        verifier.setLogFileName("info.log");
        verifier.execute();
        verifier.verifyErrorFreeLog();

        verifyTextNotInLog(verifier, "[DEBUG] extension afterProjectsRead called");
        verifier.verifyTextInLog("[INFO] extension afterProjectsRead called");

        verifyTextNotInLog(verifier, "[DEBUG] extension afterSessionEnd called");
        verifier.verifyTextInLog("[INFO] extension afterSessionEnd called");
    }

    @Test
    public void coreExtensionDebug() throws Exception {
        File projectDir = ResourceExtractor.simpleExtractResources(getClass(), "/gh-12668-extension-debug-level");

        Verifier verifier = newVerifier(new File(projectDir, "project-core").getAbsolutePath());
        verifier.addCliArgument("validate");
        verifier.addCliArgument("-X");
        verifier.execute();
        verifier.verifyErrorFreeLog();

        verifier.verifyTextInLog("[DEBUG] extension afterSessionStart called");
        verifier.verifyTextInLog("[INFO] extension afterSessionStart called");

        verifier.verifyTextInLog("[DEBUG] extension afterProjectsRead called");
        verifier.verifyTextInLog("[INFO] extension afterProjectsRead called");

        verifier.verifyTextInLog("[DEBUG] extension afterSessionEnd called");
        verifier.verifyTextInLog("[INFO] extension afterSessionEnd called");
    }

    @Test
    public void coreExtensionInfo() throws Exception {
        File projectDir = ResourceExtractor.simpleExtractResources(getClass(), "/gh-12668-extension-debug-level");

        Verifier verifier = newVerifier(new File(projectDir, "project-core").getAbsolutePath());
        verifier.addCliArgument("validate");
        verifier.execute();
        verifier.verifyErrorFreeLog();

        verifyTextNotInLog(verifier, "[DEBUG] extension afterSessionStart called");
        verifier.verifyTextInLog("[INFO] extension afterSessionStart called");

        verifyTextNotInLog(verifier, "[DEBUG] extension afterProjectsRead called");
        verifier.verifyTextInLog("[INFO] extension afterProjectsRead called");

        verifyTextNotInLog(verifier, "[DEBUG] extension afterSessionEnd called");
        verifier.verifyTextInLog("[INFO] extension afterSessionEnd called");
    }

    private void verifyTextNotInLog(Verifier verifier, String text) throws VerificationException {
        List<String> lines = verifier.loadFile(verifier.getBasedir(), verifier.getLogFileName(), false);

        boolean textFound = false;
        for (String line : lines) {
            if (line.contains(text)) {
                textFound = true;
                break;
            }
        }
        if (textFound) {
            throw new VerificationException("Text found in log: " + text);
        }
    }
}
