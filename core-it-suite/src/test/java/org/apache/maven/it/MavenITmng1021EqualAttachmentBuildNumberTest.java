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
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-1021">MNG-1021</a>.
 *
 * @author John Casey
 *
 */
public class MavenITmng1021EqualAttachmentBuildNumberTest
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng1021EqualAttachmentBuildNumberTest()
    {
        super( ALL_MAVEN_VERSIONS );
    }

    /**
     * Test that source attachments have the same build number and timestamp as the main
     * artifact when deployed.
     */
    public void testitMNG1021()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-1021" );
        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "repo" );
        verifier.deleteArtifacts( "org.apache.maven.its.mng1021" );
        verifier.executeGoal( "initialize" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        verifier.assertArtifactPresent( "org.apache.maven.its.mng1021", "test", "SNAPSHOT", "pom" );
        verifier.assertArtifactPresent( "org.apache.maven.its.mng1021", "test", "SNAPSHOT", "jar" );

        String dir = "repo/org/apache/maven/its/mng1021/test/";
        String snapshot = getSnapshotVersion( new File( testDir, dir + "SNAPSHOT" ) );
        assertTrue( snapshot, snapshot.endsWith( "-1" ) );

        verifier.assertFilePresent( dir + "maven-metadata.xml" );
        verifier.assertFilePresent( dir + "maven-metadata.xml.md5" );
        verifier.assertFilePresent( dir + "maven-metadata.xml.sha1" );
        verifier.assertFilePresent( dir + "SNAPSHOT/maven-metadata.xml" );
        verifier.assertFilePresent( dir + "SNAPSHOT/maven-metadata.xml.md5" );
        verifier.assertFilePresent( dir + "SNAPSHOT/maven-metadata.xml.sha1" );
        verifier.assertFilePresent( dir + "SNAPSHOT/test-" + snapshot + ".pom" );
        verifier.assertFilePresent( dir + "SNAPSHOT/test-" + snapshot + ".pom.md5" );
        verifier.assertFilePresent( dir + "SNAPSHOT/test-" + snapshot + ".pom.sha1" );
        verifier.assertFilePresent( dir + "SNAPSHOT/test-" + snapshot + ".jar" );
        verifier.assertFilePresent( dir + "SNAPSHOT/test-" + snapshot + ".jar.md5" );
        verifier.assertFilePresent( dir + "SNAPSHOT/test-" + snapshot + ".jar.sha1" );
        verifier.assertFilePresent( dir + "SNAPSHOT/test-" + snapshot + "-it.jar" );
        verifier.assertFilePresent( dir + "SNAPSHOT/test-" + snapshot + "-it.jar.md5" );
        verifier.assertFilePresent( dir + "SNAPSHOT/test-" + snapshot + "-it.jar.sha1" );
    }

    private String getSnapshotVersion( File artifactDir )
    {
        File[] files = artifactDir.listFiles();
        for ( File file : files )
        {
            String name = file.getName();
            if ( name.endsWith( ".pom" ) )
            {
                return name.substring( "test-".length(), name.length() - ".pom".length() );
            }
        }
        throw new IllegalStateException( "POM not found in " + artifactDir );
    }

}
