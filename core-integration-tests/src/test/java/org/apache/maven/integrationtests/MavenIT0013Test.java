package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0013Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test plugin-plugin, which tests maven-plugin-tools-api and
     * maven-plugin-tools-java. This will generate a plugin descriptor from
     * java-based mojo sources, install the plugin, and then use it.
     */
    public void testit0013()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0013" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven.its.plugins", "maven-it-it0013", "1.0-SNAPSHOT", "maven-plugin" );
        verifier.executeGoal( "install" );
        verifier.assertFilePresent( "target/maven-it-it0013-1.0-SNAPSHOT.jar" );

        verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );        
        verifier.executeGoal( "org.apache.maven.its.it0013:maven-it-it0013:it0013" );
        verifier.assertFilePresent( "target/it0013-verify" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}

