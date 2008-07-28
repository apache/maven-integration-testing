package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0122ReactorDependencyResolutionTest
    extends AbstractMavenIntegrationTestCase
{
    /**
     * Test that reactor projects are included in dependency resolution.
     * 
     * @throws Exception
     */
    public void testit0122() throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0122-reactorDependencyResolution/plugin" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "install" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

        testDir = extractTestResources( getClass(), "/it0122-reactorDependencyResolution/project" );
        itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "org.apache.maven.its.it0122:maven-it-it0122-plugin:1.0:test" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }
}
