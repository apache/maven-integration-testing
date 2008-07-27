package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0035Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test artifact relocation.
     */
    public void testit0035()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0035" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven", "maven-core-it-support", "1.1", "jar" );
        verifier.deleteArtifact( "org.apache.maven", "maven-core-it-support", "1.1", "pom" );
        verifier.deleteArtifact( "org.apache.maven", "maven-core-it-support-old-location", "1.1", "pom" );
        verifier.executeGoal( "package" );
        verifier.assertArtifactPresent( "org.apache.maven", "maven-core-it-support", "1.1", "jar" );
        verifier.assertArtifactPresent( "org.apache.maven", "maven-core-it-support", "1.1", "pom" );
        verifier.assertArtifactPresent( "org.apache.maven", "maven-core-it-support-old-location", "1.1", "pom" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

