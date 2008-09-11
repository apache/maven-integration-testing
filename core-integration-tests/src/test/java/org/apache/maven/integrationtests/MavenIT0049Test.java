package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0049Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test parameter alias usage.
     */
    public void testit0049()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0049" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        //todo: i don't think we need to delete this plugin
        //itr.deleteArtifact( "org.apache.maven.its.plugins", "maven-it-plugin-touch", "1.0", "maven-plugin" );
        itr.invoke( "org.apache.maven.its.plugins:maven-it-plugin-touch:touch" );
        itr.assertFilePresent( "target/touchFile.txt" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

