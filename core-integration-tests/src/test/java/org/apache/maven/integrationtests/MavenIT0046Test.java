package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0046Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test fail-never reactor behavior. Forces an exception to be thrown in
     * the first module, but checks that the second modules is built.
     */
    public void testit0046()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0046" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven.plugins", "maven-it-it-plugin", "1.0", "maven-plugin" );
        List cliOptions = new ArrayList();
        cliOptions.add( "--no-plugin-registry --fail-never" );
        verifier.executeGoal( "org.apache.maven.its.plugins:maven-it-plugin-touch:touch", cliOptions );
        verifier.assertFilePresent( "target/touch.txt" );
        verifier.assertFileNotPresent( "subproject/target/touch.txt" );
        verifier.assertFilePresent( "subproject2/target/touch.txt" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

