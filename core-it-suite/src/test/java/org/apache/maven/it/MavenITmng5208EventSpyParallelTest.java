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

public class MavenITmng5208EventSpyParallelTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng5208EventSpyParallelTest()
    {
        super( "[3.0.5,)" );
    }

    /**
     * Verify spy signals correct module for failure
     *
     * @throws Exception in case of failure
     */
    public void testCorrectModuleFails()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-5208" );

        Verifier spy = newVerifier( testDir.getAbsolutePath() + "/spy");
        spy.executeGoal( "install" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() + "/project" );
        verifier.setForkJvm( true );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.setSystemProperty( "maven.ext.class.path", "../spy/target/event-spy-0.0.1-SNAPSHOT.jar" );
        verifier.addCliOption( "-X" );
        verifier.addCliOption( "-T 2" );
        verifier.addCliOption( "-fn" );
        verifier.executeGoal( "compile" );
        verifier.verifyTextInLog( "ProjectFailed/org.apache.maven.its.mng5208:sub-2" );
        verifier.resetStreams();
    }

}
