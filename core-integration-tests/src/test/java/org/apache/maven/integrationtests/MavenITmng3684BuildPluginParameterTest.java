package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.IntegrationTestRunner;

/**
 * Verify that the Build instance injected as a plugin parameter contains
 * interpolated values for things like the various build paths, etc.
 * 
 * @author jdcasey
 */
public class MavenITmng3684BuildPluginParameterTest
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng3684BuildPluginParameterTest()
        throws InvalidVersionSpecificationException
    {
        super( "(2.0.9,)" );
    }
    
    public void testitMNG3684 ()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/mng-3684-buildPluginParameter" );
        File pluginDir = new File( testDir, "maven-mng3684-plugin" );
        File projectDir = new File( testDir, "project" );

        IntegrationTestRunner itr = new IntegrationTestRunner( pluginDir.getAbsolutePath() );
        itr.executeGoal( "install" );
        
        itr.verifyErrorFreeLog();
        itr.resetStreams();
        
        itr = new IntegrationTestRunner( projectDir.getAbsolutePath() );
        itr.executeGoal( "validate" );

        itr.verifyErrorFreeLog();
        itr.resetStreams();
        
        File logFile = new File( projectDir, "log.txt" );
        logFile.renameTo( new File( projectDir, "log-validate.txt" ) );
        
        itr.executeGoal( "site" );

        itr.verifyErrorFreeLog();
        itr.resetStreams();
        
        logFile.renameTo( new File( projectDir, "log-site.txt" ) );
        
    }
}
