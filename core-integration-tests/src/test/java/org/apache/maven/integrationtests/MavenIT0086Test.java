package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0086Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Verify that a plugin dependency class can be loaded from both the plugin classloader and the
     * context classloader available to the plugin.
     */
    public void testit0086()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0086" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( "org.apache.maven.plugins", "maven-it-it-plugin", "1.0", "maven-plugin" );
        itr.invoke( "validate" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

