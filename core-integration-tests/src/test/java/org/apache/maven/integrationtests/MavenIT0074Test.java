package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0074Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test that plugin-level configuration instances are not nullified by
     * execution-level configuration instances.
     */
    public void testit0074()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0074" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "eclipse:eclipse" );
        verifier.assertFilePresent( ".classpath" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

