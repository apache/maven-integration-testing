package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0094Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test classloading issues with mojos after 2.0 (MNG-1898).
     */
    public void testit0094()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0094" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "install" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

