package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.IntegrationTestRunner;

/**
 * #it0107 requires a snapshot version of maven-plugin-plugin, which indicates it doesn't belong here
 */
public class MavenIT0107Test
    extends AbstractMavenIntegrationTestCase
{
    /**
     * Verify that default implementation of an implementation for a complex object works as
     * expected [MNG-2293]
     */
    public void testit0107()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0107" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        List cliOptions = new ArrayList();
        verifier.executeGoal( "validate", cliOptions );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}

