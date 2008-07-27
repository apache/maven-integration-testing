package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0005Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * The simplest of pom installation. We have a snapshot pom and we install
     * it in local repository.
     */
    public void testit0005()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0005" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven", "maven-it-it0005", "1.0-SNAPSHOT", "pom" );
        verifier.executeGoal( "install:install" );
        verifier.assertArtifactPresent( "org.apache.maven.its.it0005", "maven-it-it0005", "1.0-SNAPSHOT", "pom" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

