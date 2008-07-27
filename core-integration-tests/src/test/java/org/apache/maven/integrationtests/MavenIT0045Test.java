package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0045Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test non-reactor behavior when plugin declares "@requiresProject false"
     */
    public void testit0045()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0045" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven.its.plugins", "maven-it-plugin-no-project", "1.0", "maven-plugin" );
        List cliOptions = new ArrayList();
        cliOptions.add( "--no-plugin-registry" );
        verifier.executeGoal( "org.apache.maven.its.plugins:maven-it-plugin-no-project:light-touch", cliOptions );
        verifier.assertFilePresent( "target/touch.txt" );
        verifier.assertFileNotPresent( "subproject/target/touch.txt" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

