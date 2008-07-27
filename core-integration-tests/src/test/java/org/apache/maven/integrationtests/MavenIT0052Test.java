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
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        List cliOptions = new ArrayList();
        cliOptions.add( "--no-plugin-registry" );
        verifier.executeGoal( "package", cliOptions );
        verifier.assertFilePresent( "target/maven-it-it0052-1.0.jar" );
        verifier.assertFileNotPresent( "target/maven-it-it0052-1.0-sources.jar" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

