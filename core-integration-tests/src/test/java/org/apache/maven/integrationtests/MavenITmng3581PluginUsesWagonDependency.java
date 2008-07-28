package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.IntegrationTestRunner;

public class MavenITmng3581PluginUsesWagonDependency
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng3581PluginUsesWagonDependency()
        throws InvalidVersionSpecificationException
    {
        // Not 2.0.9
        super( "(,2.0.9),(2.0.9,)" );
    }

    /**
     * Test that a plugin using a wagon directly works
     */
    public void testmng3581()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/mng-3581-useWagonDependency" );
        File pluginDir = new File( testDir, "plugin" );
        File projectDir = new File( testDir, "project" );

        IntegrationTestRunner itr = new IntegrationTestRunner( pluginDir.getAbsolutePath() );
        itr.executeGoal( "install" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

        itr = new IntegrationTestRunner( projectDir.getAbsolutePath() );
        itr.executeGoal( "validate" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

