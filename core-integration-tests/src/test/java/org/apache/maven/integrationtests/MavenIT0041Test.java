package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0041Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test the use of a new type from a plugin
     */
    public void testit0041()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0041" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( "org.apache.maven", "maven-core-it-support", "1.2", "coreit-artifact" );
        itr.invoke( "package" );
        itr.assertFilePresent( "target/maven-it-it0041-1.0-SNAPSHOT.jar" );
        itr.assertArtifactPresent( "org.apache.maven", "maven-core-it-support", "1.2", "coreit-artifact" );
        itr.assertArtifactPresent( "org.apache.maven", "maven-core-it-support", "1.2", "pom" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

