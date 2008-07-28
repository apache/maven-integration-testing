package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.IntegrationTestException;
import org.apache.maven.it.IntegrationTestRunner;

/**
 * Tests that the PluginDescriptor.getArtifacts() call returns all of the dependencies of the plugin,
 * not just those that made it past the filter excluding Maven's core artifacts.
 */
public class MavenITmng3473PluginReportCrash
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng3473PluginReportCrash()
        throws InvalidVersionSpecificationException
    {
        super( "(2.0.8,)" ); // >2.0.8
    }
    public void testitMNG3473 ()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/mng-3473PluginReportCrash" );

        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );


        File logFile = new File( testDir, "log.txt" );

        // force the use of the 2.4.1 plugin version via a profile here...
        List cliOptions = new ArrayList();
        cliOptions.add( "-Pplugin-2.4.1" );
        itr.executeGoal( "install", cliOptions );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

        logFile.renameTo( new File( testDir, "log-2.4.1-preinstall.txt" ) );

        //should succeed with 2.4.1
        itr.executeGoal( "org.apache.maven.plugins:maven-help-plugin:2.0.2:effective-pom, site" );

        // NOTE: Velocity prints an [ERROR] line pertaining to an incorrect macro usage when run in 2.1, so this doesn't work.
//        itr.verifyErrorFreeLog();
        itr.resetStreams();

        logFile.renameTo( new File( testDir, "log-2.4.1.txt" ) );

        //should fail with 2.4
        cliOptions.clear();
        cliOptions.add( "-Pplugin-2.4" );
        try
        {
          itr.executeGoal( "site", cliOptions );
        }
        catch (IntegrationTestException e)
        {
          //expected this but don't require it cause some os's don't return the correct error code
        }
        itr.verifyTextInLog( "org/apache/maven/doxia/module/site/manager/SiteModuleNotFoundException" );
        itr.resetStreams();

        logFile.renameTo( new File( testDir, "log-2.4.txt" ) );
    }
}
