package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0053Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test that attached artifacts have the same buildnumber and timestamp
     * as the main artifact. This will not correctly verify until we have
     * some way to pattern-match the buildnumber/timestamp...
     */
    public void testit0053()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0053" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        List cliOptions = new ArrayList();
        cliOptions.add( "--no-plugin-registry" );
        itr.executeGoal( "package", cliOptions );
        itr.assertFilePresent( "target/maven-it-it0053-1.0-SNAPSHOT.jar" );
        itr.assertFileNotPresent( "target/maven-it-it0053-1.0-SNAPSHOT-sources.jar" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

