package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

/**
 * Checks that a plugin execution with an id that contains an expression will 
 * still execute without a problem.
 * 
 * @author jdcasey
 */
public class MavenITmng3679PluginExecIdInterpolationTest
    extends AbstractMavenIntegrationTestCase
{
    public void testitMNG3679 ()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/mng-3679-pluginExecIdInterpolation" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "validate" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();       
        assertTrue( new File( testDir, "target/check.txt" ).exists() );
    }
}
