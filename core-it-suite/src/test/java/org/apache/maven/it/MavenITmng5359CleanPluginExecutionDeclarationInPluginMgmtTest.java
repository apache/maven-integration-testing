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
 * This is a test set for <a href="http://jira.codehaus.org/browse/MNG-5359">MNG-5359</a>.
 *
 * @author Anders Hammar
 */
public class MavenITmng5359CleanPluginExecutionDeclarationInPluginMgmtTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng5359CleanPluginExecutionDeclarationInPluginMgmtTest()
    {
        // Might work with versions before 2.0.11, but not verified
        super( "[2.0.11,3.0-alpha-1),[3.4,)" );
    }

    public void testit()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-5359" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath(), "remote" );

        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.deleteArtifacts( "org.apache.maven.its.mng5359" );

        verifier.executeGoal( "install" ); // will fail if clean plugin is executed
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
        
        verifier.assertArtifactPresent( "org.apache.maven.its.mng5359", "test", "0.1-SNAPSHOT", "jar" );
    }
}
