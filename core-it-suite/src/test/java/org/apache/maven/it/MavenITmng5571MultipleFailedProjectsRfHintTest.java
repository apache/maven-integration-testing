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

/**
 * Verifies that the -rf hint is useful when multiple modules in the build fail.
 * Test project is based on the reproduction case by the original issue creator.
 * @see <a href="https://issues.apache.org/jira/browse/MNG-5571">MNG-5571</a>
 */
public class MavenITmng5571MultipleFailedProjectsRfHintTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng5571MultipleFailedProjectsRfHintTest()
    {
        super( "[3.7.0,)" );
    }

    /**
     * This test verifies that the first topologically sorted project is hinted with the -rf flag.
     */
    public void testFirstTopologicallySortedProjectIsHinted()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-5571-multiple-failed-projects-rf-hint" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( true );
        verifier.addCliOption( "-Dfail.b" ); // This ensures module b fails.
        verifier.addCliOption( "-Dfail.c" ); // This ensures module c fails.
        verifier.addCliOption( "-T3" );
        try
        {
            verifier.executeGoal( "verify" );
            fail("Expected this testcase to fail");
        }
        catch (VerificationException e)
        {
            // Module b has a 2 second sleep before failing, to ensure it fails after module c.
            // Maven used to suggest "-rf :c", which does not make sense since it should list the first failed project in the ordered list.
            // This log line is subject to change, depending on the implementation of MNG-5760.
            verifier.verifyTextInLog( "mvn <args> -rf :b" );
        }
    }
}
