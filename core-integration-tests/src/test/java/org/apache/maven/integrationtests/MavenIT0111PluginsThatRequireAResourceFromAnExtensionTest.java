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

        IntegrationTestRunner itr;

        // Install the parent POM
        itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( "org.apache.maven.its.it0111", "parent", "1.0", "pom" );                
        itr.deleteArtifact( "org.apache.maven.its.it0111", "checkstyle-test", "1.0", "jar" );
        itr.deleteArtifact( "org.apache.maven.its.it0111", "checkstyle-assembly", "1.0", "jar" );
        List cliOptions = new ArrayList();
        cliOptions.add( "-N" );
        itr.invoke( "install" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

        // Install the extension with the resources required for the test
        itr = new IntegrationTestRunner( new File( testDir.getAbsolutePath(), "checkstyle-assembly" ).getAbsolutePath() );
        itr.invoke( "install" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

        // Run the whole test
        itr = new IntegrationTestRunner( new File( testDir.getAbsolutePath(), "checkstyle-test" ).getAbsolutePath() );
        itr.invoke( "install" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }
}
