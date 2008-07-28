package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0004Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * The simplest of pom installation. We have a pom and we install it in
     * local repository.
     */
    public void testit0004()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0004" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( "org.apache.maven", "maven-it-it0004", "1.0", "pom" );
        itr.executeGoal( "install:install" );
        itr.assertArtifactPresent( "org.apache.maven.its.it0004", "maven-it-it0004", "1.0", "pom" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

