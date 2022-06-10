package org.apache.maven.it;

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

import org.apache.maven.it.util.ResourceExtractor;

import java.io.File;
import java.util.Arrays;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-3372">MNG-3372</a>.
 *
 *
 */
public class MavenITmng3372DirectInvocationOfPluginsTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng3372DirectInvocationOfPluginsTest()
    {
        super( "(2.0.5,)" );
    }

    public void testitMNG3372()
        throws Exception
    {
        // The testdir is computed from the location of this
        // file.
        File testBaseDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-3372/direct-using-prefix" );
        File plugin = new File( testBaseDir, "plugin" );
        File project = new File( testBaseDir, "project" );
        File settingsFile = new File( testBaseDir, "settings.xml" );

        Verifier verifier = newVerifier( plugin.getAbsolutePath(), "remote" );

        verifier.deleteArtifacts( "org.apache.maven.its.mng3372" );

        verifier.getSystemProperties().setProperty( "updateReleaseInfo", "true" );

        verifier.executeGoals( Arrays.asList( new String[]{ "clean", "install" } ) );

        verifier = newVerifier( project.getAbsolutePath() );

        verifier.addCliOption( "-s" );
        verifier.addCliOption( "\"" + settingsFile.getAbsolutePath() + "\"" );

        verifier.executeGoal( "mng3372:test" );

        verifier.verifyErrorFreeLog();

        verifier.resetStreams();
    }

    public void testDependencyTreeInvocation()
        throws Exception
    {
        // The testdir is computed from the location of this
        // file.
        File testBaseDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-3372/dependency-tree" );

        Verifier verifier = newVerifier( testBaseDir.getAbsolutePath(), "remote" );

        verifier.addCliOption( "-U" );

        verifier.executeGoal( "dependency:tree" );

        verifier.verifyErrorFreeLog();

        verifier.resetStreams();
    }
}
