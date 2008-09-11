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
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( "org.apache.maven.its.plugins", "maven-it-it0013", "1.0-SNAPSHOT", "maven-plugin" );
        itr.invoke( "install" );
        itr.assertFilePresent( "target/maven-it-it0013-1.0-SNAPSHOT.jar" );

        itr = new IntegrationTestRunner( testDir.getAbsolutePath() );        
        itr.invoke( "org.apache.maven.its.it0013:maven-it-it0013:it0013" );
        itr.assertFilePresent( "target/it0013-verify" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }
}

