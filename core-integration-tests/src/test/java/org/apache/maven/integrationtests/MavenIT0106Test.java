package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

/**
 * #it0106 MNG-2318 not yet fixed
 */
public class MavenIT0106Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * When a project has modules and its parent is not preinstalled [MNG-2318]
     */
    public void testit0106()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0106" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "clean" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

