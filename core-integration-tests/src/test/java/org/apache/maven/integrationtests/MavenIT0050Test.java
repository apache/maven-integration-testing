package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0050Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test surefire inclusion/exclusions
     */
    public void testit0050()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0050" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "package" );
        verifier.assertFilePresent( "target/testTouchFile.txt" );
        verifier.assertFilePresent( "target/defaultTestTouchFile.txt" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

