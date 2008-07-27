package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0111PluginsThatRequireAResourceFromAnExtensionTest
    extends AbstractMavenIntegrationTestCase
{
    public MavenIT0111PluginsThatRequireAResourceFromAnExtensionTest()
        throws InvalidVersionSpecificationException
    {
        super( "(,2.1-SNAPSHOT)" );
    }

    public void testit0111()
        throws Exception
    {
        File testDir =
            extractTestResources( getClass(), "/it0111-pluginThatRequiresResourceFromAnExtension" );

        IntegrationTestRunner verifier;

        // Install the parent POM
        verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven.its.it0111", "parent", "1.0", "pom" );                
        verifier.deleteArtifact( "org.apache.maven.its.it0111", "checkstyle-test", "1.0", "jar" );
        verifier.deleteArtifact( "org.apache.maven.its.it0111", "checkstyle-assembly", "1.0", "jar" );
        List cliOptions = new ArrayList();
        cliOptions.add( "-N" );
        verifier.executeGoal( "install" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        // Install the extension with the resources required for the test
        verifier = new IntegrationTestRunner( new File( testDir.getAbsolutePath(), "checkstyle-assembly" ).getAbsolutePath() );
        verifier.executeGoal( "install" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        // Run the whole test
        verifier = new IntegrationTestRunner( new File( testDir.getAbsolutePath(), "checkstyle-test" ).getAbsolutePath() );
        verifier.executeGoal( "install" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}
