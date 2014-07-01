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

import java.io.File;

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

/**
 * This is a test set for <a href="http://jira.codehaus.org/browse/MNG-4565">MNG-3106</a>:
 * 
 * When multiple activators are present in a profile they should be AND'd. All activator must
 * conditions must be satisfied in order for the profile to be activated.
 */
public class MavenITmng4565MultiConditionProfileActivationTest
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng4565MultiConditionProfileActivationTest()
    {
        super( "(3.2.2,)" );
    }

    /**
     * Test build with two profiles, each with more than one activator.
     * The profiles should be activated even though only one of the activators 
     * returns true.
     * 
     */
    public void testProfilesWithMultipleActivators()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-4565-multi-condition-profile-activation" );

        Verifier verifier;

        verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.addCliOption( "-Dprofile1.on=true" );
        verifier.executeGoal( "validate" );

        //
        // The property profile1.on = true so only profile1 should be activated. The profile2.on property is not true so profile2 
        // should not be activated. Only the profile1/touch.txt file should be generated.
        //
        verifier.verifyErrorFreeLog();
        verifier.assertFilePresent( "target/profile1/touch.txt" );
        verifier.assertFileNotPresent( "target/profile2/touch.txt" );
        verifier.resetStreams();
    }

}
