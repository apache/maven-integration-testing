package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0105Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * MRESOURCES-18
     */
    public void testit0105()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0105" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        List cliOptions = new ArrayList();
        cliOptions.add( "-Dparam=PARAM" );
        verifier.executeGoal( "test", cliOptions );
        verifier.assertFilePresent( "target/classes/test.properties" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}

