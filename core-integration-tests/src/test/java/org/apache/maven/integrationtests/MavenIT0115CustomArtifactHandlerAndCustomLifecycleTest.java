package org.apache.maven.integrationtests;

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

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0115CustomArtifactHandlerAndCustomLifecycleTest
    extends AbstractMavenIntegrationTestCase
{
    public void testit0115()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0115-customArtifactHandlerAndCustomLifecycle" );

        IntegrationTestRunner itr;

        // Install the parent POM
        itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( "org.apache.maven.its.it0115", "test-extension", "1.0-SNAPSHOT", "jar" );
        itr.deleteArtifact( "org.apache.maven.its.it0115", "test-project", "1.0-SNAPSHOT", "jar" );

        // Install the plugin to test for Authz info in the WagonManager
        itr = new IntegrationTestRunner( new File( testDir.getAbsolutePath(), "test-extension" ).getAbsolutePath() );
        itr.executeGoal( "install" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

        // Build the test project that uses the plugin.
        File testProject = new File( testDir.getAbsolutePath(), "test-project" );
        itr = new IntegrationTestRunner( testProject.getAbsolutePath() );
        itr.executeGoal( "package" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

        itr.assertFilePresent( new File( testProject, "target/test-project.xar" ).getAbsolutePath() );
    }
}
