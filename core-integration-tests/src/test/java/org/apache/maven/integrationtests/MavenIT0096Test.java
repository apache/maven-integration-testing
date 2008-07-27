package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0096Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test that plugin executions from &gt;1 step of inheritance don't run multiple times.
     */
    public void testit0096()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0096" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "package" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

