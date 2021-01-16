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
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.util.ResourceExtractor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-6772">MNG-6772</a>:
 *
 * The test POM references an import scope POM, which also has a dependency on an import scope POM.
 *
 * Both import POMs can only be found in the repository defined in the test POM.
 * It has a parent POM that defines the same repository with a different location.
 * The test confirms that the dominant repository definition (child) wins while resolving the import POMs.
 *
 */
public class MavenITmng6772NestedImportScopeRepositoryOverride
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng6772NestedImportScopeRepositoryOverride()
    {
        super( "[3.0,)" );
    }

    // This will test the behavior using ProjectModelResolver
    public void testitInProject()
        throws Exception
    {
        final File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-6772-override-in-project" );

        final Verifier verifier = newVerifier( testDir.getAbsolutePath(), null );
        overrideSettings( testDir, verifier );
        verifier.deleteArtifacts( "org.apache.maven.its.mng6772" );

        verifier.filterFile( "pom-template.xml", "pom.xml", "UTF-8", verifier.newDefaultFilterProperties() );

        try
        {
            verifier.executeGoal( "validate" );
            fail( "Shouldn't be able to find b-0.1.pom in Central " );
        }
        catch( VerificationException e )
        {
        }

        List<String> logLines = Files.readAllLines( testDir.toPath().resolve( verifier.getLogFileName() ) );

        List<String> downloadLines = new ArrayList<>( 3 );
        for ( String line : logLines )
        {
            if ( line.startsWith( "[INFO] Downloading from central:" ) )
            {
                downloadLines.add( line );
            }
        }

        assertEquals( 2, downloadLines.size() );

        assertThat( downloadLines.get( 0 ), endsWith( "/a-0.1.pom" ) );
        assertThat( downloadLines.get( 0 ), startsWith( "[INFO] Downloading from central: file" ) );

        assertThat( downloadLines.get( 1 ), endsWith( "/b-0.1.pom" ) );
        assertThat( downloadLines.get( 1 ), startsWith( "[INFO] Downloading from central: http" ) );

        verifier.resetStreams();
    }

    // This will test the behavior using DefaultModelResolver
    public void testitInDependency()
        throws Exception
    {
        final File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-6772-override-in-dependency" );

        final Verifier verifier = newVerifier( testDir.getAbsolutePath(), null );
        overrideSettings( testDir, verifier );
        verifier.deleteArtifacts( "org.apache.maven.its.mng6772" );

        verifier.filterFile( "pom-template.xml", "pom.xml", "UTF-8", verifier.newDefaultFilterProperties() );

        try
        {
            verifier.executeGoal( "compile" );
            fail( "Shouldn't be able to find b-0.1.pom in Central " );
        }
        catch( VerificationException e )
        {
        }

        List<String> logLines = Files.readAllLines( testDir.toPath().resolve( verifier.getLogFileName() ) );

        List<String> downloadLines = new ArrayList<>( 3 );
        for ( String line : logLines )
        {
            if ( line.startsWith( "[INFO] Downloading from central:" ) )
            {
                downloadLines.add( line );
            }
        }

        assertEquals( 3, downloadLines.size() );

        assertThat( downloadLines.get( 0 ), endsWith( "/dependency-0.1.pom" ) );
        assertThat( downloadLines.get( 0 ), startsWith( "[INFO] Downloading from central: file" ) );

        // this might be a bug, shouldn't it be using the repository defined in dependency-0.1.pom, even though it is a BOM
        assertThat( downloadLines.get( 1 ), endsWith( "/a-0.1.pom" ) );
        assertThat( downloadLines.get( 1 ), startsWith( "[INFO] Downloading from central: file" ) );

        assertThat( downloadLines.get( 2 ), endsWith( "/b-0.1.pom" ) );
        assertThat( downloadLines.get( 2 ), startsWith( "[INFO] Downloading from central: http" ) );

        verifier.resetStreams();
    }

    // central must not be defined in any settings.xml or super POM will never be in play.
    private void overrideSettings( final File testDir, final Verifier verifier )
    {
        final File settingsFile = new File( testDir, "settings-override.xml" );
        final String path = settingsFile.getAbsolutePath();

        verifier.getCliOptions().add( "--global-settings" );
        if ( path.indexOf( ' ' ) < 0 )
        {
            verifier.getCliOptions().add( path );
        }
        else
        {
            verifier.getCliOptions().add( '"' + path + '"' );
        }

        verifier.getCliOptions().add( "--settings" );
        if ( path.indexOf( ' ' ) < 0 )
        {
            verifier.getCliOptions().add( path );
        }
        else
        {
            verifier.getCliOptions().add( '"' + path + '"' );
        }
    }

}
