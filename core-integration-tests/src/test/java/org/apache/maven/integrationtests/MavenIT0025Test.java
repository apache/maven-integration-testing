package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0025Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test multiple goal executions with different execution-level configs.
     */
    public void testit0025()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0025" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "process-sources" );
        itr.assertFilePresent( "target/test.txt" );
        itr.assertFilePresent( "target/test2.txt" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

