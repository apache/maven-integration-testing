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

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-5889">MNG-5889</a>:
 * check that extensions in <code>.mvn/</code> are found when Maven is run with <code>-f path/to/pom.xml</code>.
 * Reuses MNG-5771 core extensions IT to run the test in new conditions.
 * 
 * @see MavenITmng5771CoreExtensionsTest
 */
public class MavenITmng5889CoreExtensionsTest
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng5889CoreExtensionsTest()
    {
        super( "(3.4.0-alpha,)" );
    }
    /**
     * check that <code>.mvn/</code> is found when current dir does not contain <code>pom.xml</code>
     * but path to POM set by <code>--file path/to/pom.xml</code>
     */
    public void testCoreExtensionFile()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-5771-core-extensions" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() ); // not client directory
        verifier.filterFile( "settings-template.xml", "settings.xml", "UTF-8", verifier.newDefaultFilterProperties() );

        verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.deleteDirectory( "client/target" );
        verifier.deleteArtifacts( "org.apache.maven.its.it-core-extensions" );
        verifier.getCliOptions().add( "-s" );
        verifier.getCliOptions().add( new File( testDir, "settings.xml" ).getAbsolutePath() );
        verifier.getCliOptions().add( "-f" ); // --file client/pom.xml
        verifier.getCliOptions().add( new File( testDir, "client/pom.xml" ).getAbsolutePath() );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}
