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
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "compile" );
        itr.assertFilePresent( "target/classes/org/apache/maven/it0019/Person.class" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

