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
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

public class MavenITmng6754TimestampInMultimoduleProject
        extends AbstractMavenIntegrationTestCase
{
    private static final Pattern LAST_UPDATED_LINE = Pattern.compile( "<lastUpdated>(\\d*)</lastUpdated>" );
    private static final String RESOURCE_PATH = "/mng-6754-version-timestamp-in-multimodule-build";


    public MavenITmng6754TimestampInMultimoduleProject()
    {
//        super( "[3.7.0,)" );
        super( "[3.6.0,)" );
    }

    public void testArtifactsHaveSameTimestamp()
            throws Exception
    {
        final File testDir = ResourceExtractor.simpleExtractResources( getClass(), RESOURCE_PATH );
        final Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        final Path baseDir = Paths.get( System.getProperty("maven.repo.local") );
        final Path repoDir = baseDir.resolve( "repo" );
        verifier.addCliOption( "-Drepodir=" + repoDir.toString() );

        verifier.executeGoals( asList( "install", "deploy" ) );
        verifier.verifyErrorFreeLog();

        final String parentLastUpdatedLocal = getLastUpdatedFromMetadata( getLocalMetadataPath( baseDir, "parent" ) );
        final String aLastUpdatedLocal = getLastUpdatedFromMetadata( getLocalMetadataPath( baseDir, "child-a" ) );
        final String bLastUpdatedLocal = getLastUpdatedFromMetadata( getLocalMetadataPath( baseDir, "child-b" ) );

        final String parentLastUpdatedRemote = getLastUpdatedFromMetadata( getRemoteMetadataPath( repoDir, "parent" ) );
        final String aLastUpdatedRemote = getLastUpdatedFromMetadata( getRemoteMetadataPath( repoDir, "child-a" ) );
        final String bLastUpdatedRemote = getLastUpdatedFromMetadata( getRemoteMetadataPath( repoDir, "child-b" ) );

        assertEquals( "Installed child modules should have equal lastUpdated in maven-metadata-local.xml",
                aLastUpdatedLocal, bLastUpdatedLocal );
        assertEquals( "Installed parent module should have equal lastUpdated in maven-metadata-local.xml as their children",
                aLastUpdatedLocal, parentLastUpdatedLocal );
        assertEquals( "Deployed child modules should have equal lastUpdated in maven-metadata.xml",
                aLastUpdatedRemote, bLastUpdatedRemote );
        assertEquals( "Deployed parent module should have equal lastUpdated in maven-metadata.xml as their children",
                aLastUpdatedRemote, parentLastUpdatedRemote );
        assertEquals( "Installed parent module should have equal lastUpdated as deployed counterparts",
                parentLastUpdatedLocal, parentLastUpdatedRemote );
        assertEquals( "Installed child-a module should have equal lastUpdated as deployed counterparts",
                aLastUpdatedLocal, aLastUpdatedRemote );
        assertEquals( "Installed child-b module should have equal lastUpdated as deployed counterparts",
                bLastUpdatedLocal, bLastUpdatedRemote );
    }

    private Path getLocalMetadataPath( final Path projectDir, final String moduleName )
    {
        final Path mng6754Path = Paths.get( "org", "apache", "maven", "its", "mng6754" );
        final Path modulePath = projectDir.resolve( mng6754Path.resolve( moduleName ) );
        return modulePath.resolve( "maven-metadata-local.xml" );
    }

    private Path getRemoteMetadataPath( final Path repoDir, final String moduleName )
    {
        final Path mng6754Path = Paths.get( "org", "apache", "maven", "its", "mng6754" );
        final Path modulePath = repoDir.resolve( mng6754Path.resolve( moduleName ) );
        return modulePath.resolve( "maven-metadata.xml" );
    }

    private String getLastUpdatedFromMetadata( final Path metadataFile ) throws IOException
    {
        final List<String> lines = Files.readAllLines( metadataFile, Charset.defaultCharset() );
        for (final String line : lines )
        {
            final Matcher matcher = LAST_UPDATED_LINE.matcher( line );
            if ( matcher.find() )
            {
                return matcher.group(1);
            }
        }

        // just in case, make sure the test will fail if there's no <lastUpdated>
        // inside "maven-metadata.xml"
        return "" + Math.random();
    }
}
