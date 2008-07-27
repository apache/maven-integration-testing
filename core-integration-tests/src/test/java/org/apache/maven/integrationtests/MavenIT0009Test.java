package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0009Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test plugin configuration and goal configuration that overrides what the
     * mojo has specified.
     */
    public void testit0009()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0009" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven.its.plugins", "maven-it-plugin-touch", "1.0", "maven-plugin" );
        verifier.executeGoal( "generate-resources" );
        verifier.assertFilePresent( "target/pluginItem" );
        verifier.assertFilePresent( "target/goalItem" );
        verifier.assertFileNotPresent( "target/bad-item" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

