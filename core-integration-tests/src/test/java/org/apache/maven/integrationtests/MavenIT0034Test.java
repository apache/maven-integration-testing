package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0034Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test version range junit [3.7,) resolves to 3.8.1
     */
    public void testit0034()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0034" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven", "maven-core-it-support", "1.4", "jar" );
        verifier.deleteArtifact( "junit", "junit", "3.8", "jar" );
        verifier.executeGoal( "package" );
        verifier.assertArtifactPresent( "junit", "junit", "3.8", "jar" );
        verifier.assertArtifactPresent( "org.apache.maven", "maven-core-it-support", "1.4", "jar" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

