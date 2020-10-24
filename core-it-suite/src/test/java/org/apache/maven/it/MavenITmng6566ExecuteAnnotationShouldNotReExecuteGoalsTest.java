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
import java.util.List;

public class MavenITmng6566ExecuteAnnotationShouldNotReExecuteGoalsTest
    extends AbstractMavenIntegrationTestCase
{
    private static final String RESOURCE_PATH = "/mng-6566-execute-annotation-should-not-re-execute-goals";
    private static final String PLUGIN_KEY = "org.apache.maven.its.mng6566:plugin:1.0-SNAPSHOT";

    private File testDir;

    public MavenITmng6566ExecuteAnnotationShouldNotReExecuteGoalsTest()
    {
        super( "[3.6.3,)" );
    }

    public void setUp()
            throws Exception
    {
        testDir = ResourceExtractor.simpleExtractResources( getClass(), RESOURCE_PATH );

        File pluginDir = new File( testDir, "plugin" );
        Verifier verifier = newVerifier( pluginDir.getAbsolutePath() );
        verifier.executeGoal( "install" );
        verifier.resetStreams();
        verifier.verifyErrorFreeLog();
    }

    public void testCompileIsForkedInDirectPluginInvocation()
            throws Exception
    {
        File consumerDir = new File( testDir, "consumer" );

        Verifier verifier = newVerifier( consumerDir.getAbsolutePath() );
        verifier.setLogFileName( "log-direct-plugin-invocation.txt" );
        verifier.executeGoal( PLUGIN_KEY + ":run" );
        verifier.resetStreams();
        verifier.verifyErrorFreeLog();
        verifier.verifyTextInLog( ">>> plugin:1.0-SNAPSHOT:run (default-cli) > compile @ consumer >>>" );
        verifier.verifyTextInLog( "MNG-6566 plugin goal executed" );
    }

    public void testCompileIsNotForkedInPhaseExecution()
            throws Exception
    {
        File consumerDir = new File( testDir, "consumer" );

        Verifier verifier = newVerifier( consumerDir.getAbsolutePath() );
        verifier.setLogFileName( "log-phase-execution.txt" );
        verifier.executeGoal( "compile" );
        verifier.resetStreams();
        verifier.verifyErrorFreeLog();
        verifyTextNotInLog( verifier, ">>> plugin:1.0-SNAPSHOT:run (default) > compile @ consumer >>>" );
        verifier.verifyTextInLog( "--- maven-compiler-plugin:3.1:compile (default-compile) @ consumer ---" );
        verifier.verifyTextInLog( "MNG-6566 plugin goal executed" );
    }

    /**
     * Throws an exception if the text <strong>is</strong> present in the log.
     *
     * @param verifier the verifier to use
     * @param text the text to assert present
     * @throws VerificationException if text is not found in log
     */
    private void verifyTextNotInLog( Verifier verifier, String text )
            throws VerificationException
    {
        List<String> lines = verifier.loadFile( verifier.getBasedir(), verifier.getLogFileName(), false );

        for ( String line : lines )
        {
            if ( Verifier.stripAnsi( line ).contains( text ) )
            {
                throw new VerificationException( "Text found in log: " + text );
            }
        }
    }
}
