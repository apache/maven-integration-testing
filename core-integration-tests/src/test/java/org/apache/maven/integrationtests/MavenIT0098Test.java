package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.IntegrationTestRunner;

/**
 * # it0098 - something started failing here, not yet identified. MNG-2322
 */
public class MavenIT0098Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test that quoted system properties are processed correctly. [MNG-1415]
     */
    public void testit0098()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0098" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        List cliOptions = new ArrayList();
        cliOptions.add( "-Dtest.property=\"Test Property\"" );
        itr.executeGoal( "test", cliOptions );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

