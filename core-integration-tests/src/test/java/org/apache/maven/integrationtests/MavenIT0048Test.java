package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0048Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Verify that default values for mojo parameters are working (indirectly,
     * by verifying that the Surefire mojo is functioning correctly).
     */
    public void testit0048()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0048" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "test" );
        verifier.assertFilePresent( "target/testFileOutput.txt" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

