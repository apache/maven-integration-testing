package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0071Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Verifies that dotted property references work within plugin
     * configurations.
     */
    public void testit0071()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0071" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( "org.apache.maven.plugins", "maven-it-it-plugin", "1.0", "maven-plugin" );
        itr.executeGoal( "org.apache.maven.its.plugins:maven-it-plugin-touch:touch" );
        itr.assertFilePresent( "target/foo2" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

