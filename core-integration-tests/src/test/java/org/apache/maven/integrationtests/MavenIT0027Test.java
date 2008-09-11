package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0027Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test @execute with a custom lifecycle, including configuration
     */
    public void testit0027()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0027" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.invoke( "org.apache.maven.its.plugins:maven-it-plugin-fork:fork, org.apache.maven.its.plugins:maven-it-plugin-fork:fork-goal" );
        itr.assertFilePresent( "target/forked/touch.txt" );
        itr.assertFilePresent( "target/forked2/touch.txt" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

