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
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( "org.apache.maven", "maven-core-it-support", "1.4", "jar" );
        itr.deleteArtifact( "junit", "junit", "3.8", "jar" );
        itr.executeGoal( "package" );
        itr.assertArtifactPresent( "junit", "junit", "3.8", "jar" );
        itr.assertArtifactPresent( "org.apache.maven", "maven-core-it-support", "1.4", "jar" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

