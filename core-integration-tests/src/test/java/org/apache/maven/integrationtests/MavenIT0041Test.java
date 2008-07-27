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
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven", "maven-core-it-support", "1.2", "coreit-artifact" );
        verifier.executeGoal( "package" );
        verifier.assertFilePresent( "target/maven-it-it0041-1.0-SNAPSHOT.jar" );
        verifier.assertArtifactPresent( "org.apache.maven", "maven-core-it-support", "1.2", "coreit-artifact" );
        verifier.assertArtifactPresent( "org.apache.maven", "maven-core-it-support", "1.2", "pom" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

