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
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "install" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        testDir = extractTestResources( getClass(), "/it0122-reactorDependencyResolution/project" );
        verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "org.apache.maven.its.it0122:maven-it-it0122-plugin:1.0:test" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}
