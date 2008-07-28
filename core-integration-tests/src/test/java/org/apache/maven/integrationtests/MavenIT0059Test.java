package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0059Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Verify that maven-1 POMs will be ignored but not stop the resolution
     * process.
     */
    public void testit0059()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0059" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "package" );
        itr.assertFilePresent( "target/maven-it-it0059-1.0.jar" );
        // don't verify error free log
        itr.resetStreams();
    }
}

