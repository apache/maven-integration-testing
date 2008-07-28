package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.IntegrationTestRunner;

/**
 * Tests that the PluginDescriptor.getArtifacts() call returns all of the dependencies of the plugin,
 * not just those that made it past the filter excluding Maven's core artifacts.
 */
public class MavenITmng3428PluginDescriptorArtifactsIncompleteTest
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng3428PluginDescriptorArtifactsIncompleteTest()
        throws InvalidVersionSpecificationException
    {
        super( "(2.0.8,)" ); // 2.0.8+
    }
    public void testitMNG3428 ()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/mng-3428-pluginDescriptorArtifactsIncomplete" );

        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );

        // First, build the plugin we'll use to test the PluginDescriptor artifact collection.
        itr.executeGoal( "install" );

        /*
         * This is the simplest way to check a build
         * succeeded. It is also the simplest way to create
         * an IT test: make the build pass when the test
         * should pass, and make the build fail when the
         * test should fail. There are other methods
         * supported by the itr. They can be seen here:
         * http://maven.apache.org/shared/maven-itr/apidocs/index.html
         */
        itr.verifyErrorFreeLog();

        /*
         * Reset the streams before executing the itr
         * again.
         */
        itr.resetStreams();

        // This should only succeed if commons-cli is part of ${plugin.artifacts}. The
        // commons-cli library is part of Maven's core classpath, so if this mojo succeeds
        // it means the PluginDescriptor.getArtifacts() call returns an unfiltered collection.
        itr.executeGoal( "tests:test-cli-maven-plugin:1:test" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }
}
