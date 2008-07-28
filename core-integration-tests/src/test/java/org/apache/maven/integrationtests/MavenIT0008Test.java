package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0008Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Simple goal decoration where a plugin binds to a phase and the plugin must
     * be downloaded from a remote repository before it can be executed. This
     * test also checks to make sure that mojo parameters are aligned to the
     * project basedir when their type is "java.io.File".
     */
    public void testit0008()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0008" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( "org.apache.maven.its.plugins", "maven-it-plugin-touch", "1.0", "maven-plugin" );
        itr.executeGoal( "compile" );
        itr.assertFilePresent( "target/touch.txt" );
        itr.assertFilePresent( "target/test-basedir-alignment/touch.txt" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

