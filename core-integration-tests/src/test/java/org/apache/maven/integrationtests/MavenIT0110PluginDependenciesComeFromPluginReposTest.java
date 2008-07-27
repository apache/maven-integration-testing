package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0110PluginDependenciesComeFromPluginReposTest
    extends AbstractMavenIntegrationTestCase
{
    public void testit0110()
        throws Exception
    {
        File testDir =
            extractTestResources( getClass(), "/it0110-pluginDependenciesComeFromPluginRepos" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven.it", "mail", "1.3.2", "jar" );        
        verifier.executeGoal( "clean" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}
