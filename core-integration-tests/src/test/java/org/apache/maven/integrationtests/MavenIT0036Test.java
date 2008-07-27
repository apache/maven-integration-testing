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
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "package" );
        verifier.assertFilePresent( "target/maven-it-it0036-1.0.jar" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

