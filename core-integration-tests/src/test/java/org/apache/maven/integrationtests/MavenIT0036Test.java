package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0036Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test building from release-pom.xml when it's available
     */
    public void testit0036()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0036" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "package" );
        itr.assertFilePresent( "target/maven-it-it0036-1.0.jar" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

