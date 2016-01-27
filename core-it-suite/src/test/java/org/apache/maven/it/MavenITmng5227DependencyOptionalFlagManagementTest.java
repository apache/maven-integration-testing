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
import java.util.List;
import java.util.Properties;
import org.apache.maven.it.util.ResourceExtractor;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-5227">MNG-5227</a>.
 *
 * @author Christian Schulte
 */
public class MavenITmng5227DependencyOptionalFlagManagementTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng5227DependencyOptionalFlagManagementTest()
    {
        super( "[3.4.0,)" );
    }

    /**
     * Verify that a dependency's optional flag is subject to dependency management. This part of the test checks
     * the effective model.
     */
    public void testitModel()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-5227/model" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        Properties props = verifier.loadProperties( "target/pom.properties" );
        assertEquals( "dep", props.getProperty( "project.dependencies.0.artifactId" ) );
        assertEquals( "true", props.getProperty( "project.dependencies.0.optional" ) );
    }

    /**
     * Verify that a transitive dependency's optional flag is subject to dependency management of the root artifat.
     * This part of the test checks the artifact collector.
     */
    public void testitResolution()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-5227/resolution" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.deleteArtifacts( "org.apache.maven.its.mng5227" );
        verifier.filterFile( "settings-template.xml", "settings.xml", "UTF-8", verifier.newDefaultFilterProperties() );
        verifier.addCliOption( "--settings" );
        verifier.addCliOption( "settings.xml" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        List<String> classpath = verifier.loadLines( "target/classpath.txt", "UTF-8" );
        assertTrue( classpath.toString(), classpath.contains( "direct-0.2.jar" ) );
        assertFalse( classpath.toString(), classpath.contains( "transitive-0.1.jar" ) );
    }

}
