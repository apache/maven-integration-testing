package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0076Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test that plugins in pluginManagement aren't included in the build
     * unless they are referenced by groupId/artifactId within the plugins
     * section of a pom.
     */
    public void testit0076()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0076" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "install" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

