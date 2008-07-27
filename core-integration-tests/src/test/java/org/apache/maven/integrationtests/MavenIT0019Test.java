package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0019Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test that a version is managed by pluginManagement in the super POM
     */
    public void testit0019()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0019" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "compile" );
        verifier.assertFilePresent( "target/classes/org/apache/maven/it0019/Person.class" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

