package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0006Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Integration test for the verifier plugin.
     */
    public void testit0006()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0006" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "integration-test" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

