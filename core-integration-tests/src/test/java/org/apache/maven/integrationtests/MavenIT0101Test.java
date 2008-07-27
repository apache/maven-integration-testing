package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0101Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test that properties defined in an active profile in the user's
     * settings are available for interpolation of systemPath in a dependency.
     * [MNG-2052]
     */
    public void testit0101()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0101" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        List cliOptions = new ArrayList();
        cliOptions.add( "--settings settings.xml" );
        verifier.executeGoal( "compile", cliOptions );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}

