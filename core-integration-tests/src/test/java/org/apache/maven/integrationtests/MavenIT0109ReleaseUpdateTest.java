package org.apache.maven.integrationtests;

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
import java.io.IOException;

import org.apache.maven.it.IntegrationTestRunner;
import org.codehaus.plexus.util.FileUtils;

/**
 * Test to verify that a release can be updated.
 */
public class MavenIT0109ReleaseUpdateTest
    extends AbstractMavenIntegrationTestCase
{
    private static final String GROUPID = "org.apache.maven.it";

    private static final String ARTIFACTID = "maven-it-release-update";

    private static final String TYPE = "jar";

    private static final String POM_TYPE = "pom";

    private static final String OLD_VERSION = "1.0";

    private static final String NEW_VERSION = "1.1";

    public void testReleaseUpdated()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0109-releaseUpdate" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( GROUPID, ARTIFACTID, OLD_VERSION, TYPE );
        itr.deleteArtifact( GROUPID, ARTIFACTID, OLD_VERSION, POM_TYPE );
        itr.deleteArtifact( GROUPID, ARTIFACTID, NEW_VERSION, TYPE );
        itr.deleteArtifact( GROUPID, ARTIFACTID, NEW_VERSION, POM_TYPE );

        writeMetadata( new File( itr.localRepo ), "maven-metadata-it.releases.xml", OLD_VERSION,
                       "20051230031110" );

        // create a repository (TODO: into itr)
        File repository = new File( testDir, "repository" );
        repository.mkdirs();

        // create artifact in repository (TODO: into itr)
        writeArtifact( repository, OLD_VERSION );
        writeArtifact( repository, NEW_VERSION );

        writeMetadata( repository, "maven-metadata.xml", NEW_VERSION, "20061230031110" );

        itr.assertArtifactNotPresent( GROUPID, ARTIFACTID, OLD_VERSION, TYPE );
        itr.assertArtifactNotPresent( GROUPID, ARTIFACTID, NEW_VERSION, TYPE );

        itr.executeGoal( "package" );

        itr.assertArtifactNotPresent( GROUPID, ARTIFACTID, OLD_VERSION, TYPE );
        itr.assertArtifactPresent( GROUPID, ARTIFACTID, NEW_VERSION, TYPE );

        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }

    private static void writeMetadata( File repository, String fileName, String version, String timestamp )
        throws IOException
    {
        File metadata = new File( repository, GROUPID.replace( '.', '/' ) + "/" + ARTIFACTID + "/" + fileName );
        metadata.getParentFile().mkdirs();

        StringBuffer content = new StringBuffer();
        content.append( "<?xml version=\"1.0\"?><metadata>\n" );
        content.append( "  <groupId>" + GROUPID + "</groupId>\n" );
        content.append( "  <artifactId>" + ARTIFACTID + "</artifactId>\n" );
        content.append( "  <versioning>\n" );
        content.append( "    <latest>" + version + "</latest>\n" );
        content.append( "    <release>" + version + "</release>\n" );
        content.append( "    <versions>\n" );
        content.append( "      <version>" + OLD_VERSION + "</version>\n" );
        content.append( "      <version>" + NEW_VERSION + "</version>\n" );
        content.append( "    </versions>\n" );
        content.append( "    <lastUpdated>" + timestamp + "</lastUpdated>\n" );
        content.append( "  </versioning>\n" );
        content.append( "</metadata>" );

        FileUtils.fileWrite( metadata.getAbsolutePath(), content.toString() );
    }

    private static void writeArtifact( File repository, String version )
        throws IOException
    {
/*
        File artifact = new File( repository, GROUPID.replace( '.', '/' ) + "/" + ARTIFACTID + "/" + version + "/" +
            ARTIFACTID + "-" + version + "." + TYPE );
        artifact.getParentFile().mkdirs();
        FileUtils.fileWrite( artifact.getAbsolutePath(), version );

        StringBuffer content = new StringBuffer();
        content.append( "<?xml version=\"1.0\"?><project>\n" );
        content.append( "  <modelVersion>4.0.0</modelVersion>\n" );
        content.append( "  <groupId>" + GROUPID + "</groupId>\n" );
        content.append( "  <artifactId>" + ARTIFACTID + "</artifactId>\n" );
        content.append( "  <version>" + version + "</version>\n" );
        content.append( "  <packaging>maven-plugin</packaging>\n" );
        content.append( "</project>" );

        artifact = new File( artifact.getParentFile(), ARTIFACTID + "-" + version + "." + POM_TYPE );
        FileUtils.fileWrite( artifact.getAbsolutePath(), content.toString() );
*/
    }
}
