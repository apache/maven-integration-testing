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
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven.plugins", "maven-it-it-plugin", "1.0", "maven-plugin" );
        verifier.executeGoal( "org.apache.maven.its.plugins:maven-it-plugin-touch:touch" );
        verifier.assertFilePresent( "target/foo2" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

