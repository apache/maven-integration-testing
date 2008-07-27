package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0081Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test per-plugin dependencies.
     */
    public void testit0081()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0081" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven.its.it0081", "test-plugin", "0.1", "maven-plugin" );
        verifier.executeGoal( "install" );
        verifier.assertFilePresent( "test-component-c/target/org.apache.maven.wagon.providers.ftp.FtpWagon" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

