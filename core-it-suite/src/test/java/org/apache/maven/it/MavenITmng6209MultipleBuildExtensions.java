package org.apache.maven.it;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import java.io.File;

import org.apache.maven.it.util.ResourceExtractor;
import org.apache.maven.shared.utils.io.FileUtils;

public class MavenITmng6209MultipleBuildExtensions extends AbstractMavenIntegrationTestCase {

  public MavenITmng6209MultipleBuildExtensions() {
    super("(3.5.0,)");
  }

  public void testBuildExtensionClassloader() throws Exception {
    File testDir =
        ResourceExtractor.simpleExtractResources(getClass(), "/mng-6209-multiple-build-extensions");
    File pluginADir = new File(testDir, "plugin-a");
    File pluginBDir = new File(testDir, "plugin-b");
    File projectDir = new File(testDir, "project");

    Verifier verifier;

    // install the test plugins
    verifier = newVerifier(pluginADir.getAbsolutePath(), "remote");
    verifier.executeGoal("install");
    verifier.resetStreams();
    verifier.verifyErrorFreeLog();
    //
    verifier = newVerifier(pluginBDir.getAbsolutePath(), "remote");
    verifier.executeGoal("install");
    verifier.resetStreams();
    verifier.verifyErrorFreeLog();

    // build the test project
    verifier = newVerifier(projectDir.getAbsolutePath(), "remote");
    verifier.executeGoal("validate");
    verifier.resetStreams();
    verifier.verifyErrorFreeLog();
    verifier.assertFilePresent("target/executions.txt");

    String[] executions = FileUtils.fileReadArray(new File(projectDir, "target/executions.txt"));
    assertEquals(2, executions.length);
    assertEquals(
        "mng-6209-multiple-build-extensions:mng-6209-multiple-build-extensions-plugin-a:0.1:test {execution: test}",
        executions[0]);
    assertEquals(
        "mng-6209-multiple-build-extensions:mng-6209-multiple-build-extensions-plugin-b:0.1:test {execution: test}",
        executions[1]);
  }
}
