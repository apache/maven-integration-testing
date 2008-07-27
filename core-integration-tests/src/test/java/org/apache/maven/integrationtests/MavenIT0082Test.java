package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0082Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test that the reactor can establish the artifact location of known projects for dependencies
     * using process-sources to see that it works even when they aren't compiled
     */
    public void testit0082()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0082" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "process-sources" );
        verifier.assertFilePresent( "test-component-c/target/my-test" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

