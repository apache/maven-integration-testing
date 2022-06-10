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
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-2486">MNG-2486</a>.
 *
 * @author Benjamin Bentmann
 */
public class MavenITmng2486TimestampedDependencyVersionInterpolationTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng2486TimestampedDependencyVersionInterpolationTest()
    {
        super( "[2.0.5,)" );
    }

    /**
     * Verify that the expression ${project.version} gets resolved to X-SNAPSHOT and not the actual timestamp
     * during transitive dependency resolution. In part, this depends on the deployed SNAPSHOT POMs to retain their
     * X-SNAPSHOT project version and not having it replaced with the timestamp version.
     *
     * @throws Exception in case of failure
     */
    public void testit()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-2486" );

        Verifier verifier;

        verifier = newVerifier( new File( testDir, "dep-a" ).getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.deleteArtifacts( "org.apache.maven.its.mng2486" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        verifier = newVerifier( new File( testDir, "parent" ).getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        verifier = newVerifier( new File( testDir, "dep-b" ).getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        verifier = newVerifier( new File( testDir, "test" ).getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        // enforce remote resolution
        verifier.deleteArtifacts( "org.apache.maven.its.mng2486" );
        verifier.filterFile( "settings-template.xml", "settings.xml", "UTF-8", verifier.newDefaultFilterProperties() );
        verifier.addCliOption( "--settings" );
        verifier.addCliOption( "settings.xml" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        List<String> files = verifier.loadLines( "target/classpath.txt", "UTF-8" );
        assertTrue( files.toString(), files.contains( "dep-a-0.1-SNAPSHOT.jar" ) );
    }

}
