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
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "process-sources" );
        verifier.assertFilePresent( "target/test.txt" );
        verifier.assertFilePresent( "target/test2.txt" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

