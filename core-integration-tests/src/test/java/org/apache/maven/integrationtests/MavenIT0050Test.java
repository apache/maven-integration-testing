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
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "package" );
        itr.assertFilePresent( "target/testTouchFile.txt" );
        itr.assertFilePresent( "target/defaultTestTouchFile.txt" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

