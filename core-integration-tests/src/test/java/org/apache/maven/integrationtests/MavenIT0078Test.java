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
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.invoke( "compile" );
        itr.assertFileNotPresent( "target/classes/Test.class" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

