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
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( "org.apache.maven.its.it0081", "test-plugin", "0.1", "maven-plugin" );
        itr.invoke( "install" );
        itr.assertFilePresent( "test-component-c/target/org.apache.maven.wagon.providers.ftp.FtpWagon" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

