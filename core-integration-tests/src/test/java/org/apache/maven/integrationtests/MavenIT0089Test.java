package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0089Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test that Checkstyle PackageNamesLoader.loadModuleFactory(..) method will complete as-is with
     * the context classloader available to the plugin.
     */
    public void testit0089()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0089" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven.plugins", "maven-it-it-plugin", "1.0", "maven-plugin" );
        verifier.executeGoal( "install" );
        verifier.assertFilePresent( "project/target/output.txt" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

