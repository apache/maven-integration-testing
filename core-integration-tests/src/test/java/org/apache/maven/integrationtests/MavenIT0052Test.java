package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0052Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test that source attachment doesn't take place when
     * -DperformRelease=true is missing.
     */
    public void testit0052()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0052" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        List cliOptions = new ArrayList();
        cliOptions.add( "--no-plugin-registry" );
        itr.executeGoal( "package", cliOptions );
        itr.assertFilePresent( "target/maven-it-it0052-1.0.jar" );
        itr.assertFileNotPresent( "target/maven-it-it0052-1.0-sources.jar" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

