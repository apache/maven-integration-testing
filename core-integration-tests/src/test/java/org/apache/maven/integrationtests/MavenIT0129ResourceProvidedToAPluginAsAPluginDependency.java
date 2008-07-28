package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0129ResourceProvidedToAPluginAsAPluginDependency
    extends AbstractMavenIntegrationTestCase
{
    public void testit0129()
        throws Exception
    {
        File testDir =
            extractTestResources( getClass(), "/it0129-resourcesForAPluginProvidedAsAPluginDependency" );

        IntegrationTestRunner itr;

        // Install the parent POM, extension and the plugin
        itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( "org.apache.maven.its.it0129", "it0129-plugin-runner", "1.0", "pom" );
        itr.deleteArtifact( "org.apache.maven.its.it0129", "it0129-extension", "1.0", "jar" );
        itr.deleteArtifact( "org.apache.maven.its.it0129", "it0129-plugin", "1.0", "jar" );
        itr.deleteArtifact( "org.apache.maven.its.it0129", "it0129-parent", "1.0", "pom" );

        List cliOptions = new ArrayList();
        itr.executeGoal( "install" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

        //now run the test
        testDir = extractTestResources( getClass(), "/it0129-resourcesForAPluginProvidedAsAPluginDependency/test-project" );
        itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        cliOptions = new ArrayList();
        itr.executeGoal( "verify" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}
