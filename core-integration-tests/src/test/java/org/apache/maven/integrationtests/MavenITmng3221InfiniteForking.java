package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.IntegrationTestRunner;

public class MavenITmng3221InfiniteForking
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng3221InfiniteForking()
        throws InvalidVersionSpecificationException
    {
        super( "(2.0.8,2.1-SNAPSHOT)" ); // only test in 2.0.9+
    }

    public void testitMNG3221a()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/mng-3221" );
        File logBackupDir = testDir.getAbsoluteFile().getParentFile();

        File reportDir = new File( testDir, "report" );
        File projectDir = new File( testDir, "user" );

        IntegrationTestRunner itr = null;

        try
        {
            itr = new IntegrationTestRunner( reportDir.getAbsolutePath() );

            itr.deleteArtifact( "tests", "maven-forking-report-plugin", "1", "jar" );

            itr.executeGoal( "install" );
            itr.verifyErrorFreeLog();
            itr.resetStreams();

            itr = new IntegrationTestRunner( projectDir.getAbsolutePath() );

            List cliOptions = new ArrayList();
            cliOptions.add( "-Psite" );
            itr.executeGoal( "site", cliOptions );
            itr.verifyErrorFreeLog();
        }
        finally
        {
            if ( itr != null )
            {
                itr.resetStreams();
            }

            File logFile = new File( projectDir, "log.txt" );
            File logFileBackup = new File( logBackupDir, "mng-3221-a-log.txt" );

            logFile.renameTo( logFileBackup );
        }
   }

    public void testitMNG3221b()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/mng-3221" );
        File logBackupDir = testDir.getAbsoluteFile().getParentFile();

        File pluginDir = new File( testDir, "plugin" );
        File projectDir = new File( testDir, "user" );

        IntegrationTestRunner itr = null;

        try
        {
            itr = new IntegrationTestRunner( pluginDir.getAbsolutePath() );

            itr.deleteArtifact( "tests", "maven-forking-test-plugin", "1", "jar" );

            itr.executeGoal( "install" );
            itr.verifyErrorFreeLog();
            itr.resetStreams();

            itr = new IntegrationTestRunner( projectDir.getAbsolutePath() );

            List cliOptions = new ArrayList();
            cliOptions.add( "-Pplugin" );
            itr.executeGoal( "package", cliOptions );
            itr.verifyErrorFreeLog();
        }
        finally
        {
            if ( itr != null )
            {
                itr.resetStreams();
            }

            File logFile = new File( projectDir, "log.txt" );
            File logFileBackup = new File( logBackupDir, "mng-3221-b-log.txt" );

            logFile.renameTo( logFileBackup );
        }
    }
}
