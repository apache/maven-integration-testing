package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0012Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test simple POM interpolation
     */
    public void testit0012()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0012" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "org.apache.maven.its.plugins:maven-it-plugin-touch:touch" );
        verifier.assertFilePresent( "target/touch-3.8.1.txt" );
        verifier.assertFilePresent( "child-project/target/child-touch-3.0.3.txt" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

