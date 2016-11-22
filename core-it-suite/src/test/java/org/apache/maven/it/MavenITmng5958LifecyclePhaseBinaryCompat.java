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

import org.apache.maven.it.util.ResourceExtractor;

public class MavenITmng5958LifecyclePhaseBinaryCompat
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng5958LifecyclePhaseBinaryCompat()
    {
        super( "(3.3.9,)" );
    }

    public void testGood()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-5958-lifecycle-phases/good" );
        
        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.verifyTextInLog( "CLASS_NAME=java.lang.String" );
        verifier.resetStreams();
    }
    
    public void testBad()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-5958-lifecycle-phases/bad" );
        
        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        try
        {
            verifier.executeGoal( "validate" );
        }
        catch ( VerificationException e )
        {
            verifier.verifyTextInLog( "[ERROR] Internal error: java.lang.ClassCastException: "
                + "org.apache.maven.lifecycle.mapping.LifecyclePhase cannot be cast to java.lang.String -> [Help 1]" );
        }
        verifier.resetStreams();
    }
}
