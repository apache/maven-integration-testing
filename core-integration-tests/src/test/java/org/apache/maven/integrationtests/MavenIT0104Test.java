package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0104Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Verify that plugin configurations are resolved correctly.
     */
    public void testit0104()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0104" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "test" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

