package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0040Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test the use of a packaging from a plugin
     */
    public void testit0040()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0040" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "package" );
        itr.assertFilePresent( "target/maven-it-it0040-1.0-it.jar" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

