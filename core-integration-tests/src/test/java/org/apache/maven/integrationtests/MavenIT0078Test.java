package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0078Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test that configuration for maven-compiler-plugin is injected from
     * PluginManagement section even when it's not explicitly defined in the
     * plugins section.
     */
    public void testit0078()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0078" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "compile" );
        verifier.assertFileNotPresent( "target/classes/Test.class" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

