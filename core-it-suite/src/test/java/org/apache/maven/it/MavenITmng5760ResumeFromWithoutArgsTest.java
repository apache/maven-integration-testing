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

/**
 * An integration test to verify that resume from without args will
 * automatically resume from last failed build.
 *
 * <a href="https://issues.apache.org/jira/browse/MNG-5760">MNG-5760</a>.
 *
 */
public class MavenITmng5760ResumeFromWithoutArgsTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng5760ResumeFromWithoutArgsTest()
    {
        super( "[3.7.0-SNAPSHOT,)" );
    }

    public void testItShouldAutomaticallyResumeFromLastFailingModule()
            throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-5760-resume-from-without-args" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        try
        {
            verifier.executeGoal( "test" );
            fail("Should have failed because of a failing testcase in module-b");
        }
        catch ( VerificationException e ) {
            // expected, as we fail on module-b
        }
        verifier.verifyTextInLog( "Building Maven Integration Test :: MNG-5760 - Parent" );
        verifier.verifyTextInLog( "Building Maven Integration Test :: MNG-5760 - Module A" );
        verifier.verifyTextInLog( "Building Maven Integration Test :: MNG-5760 - Module B" );
        verifier.resetStreams();

        verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.addCliOption( "-DskipTests" ); // This time we allow the test to fail
        verifier.addCliOption( "-rf" );
        verifier.executeGoal( "test" );
        verifier.setLogFileName( "log.txt" );
        List<String> logLines = verifier.loadLines( "log.txt", "UTF-8" );
        for ( String logLine : logLines )
        {
            if ( logLine.contains( "Building Maven Integration Test :: MNG-5760 - Parent") ||
                    logLine.contains( "Building Maven Integration Test :: MNG-5760 - Module A" ) )
            {
                fail( "Did not expect parent or module-a to be built" );
            }
        }
        verifier.verifyTextInLog( "Building Maven Integration Test :: MNG-5760 - Module B" );
    }
}
