package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0020Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test beanshell mojo support.
     */
    public void testit0020()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0020" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven.its.plugins", "maven-it-it0020", "1.0-SNAPSHOT", "maven-plugin" );
        verifier.executeGoal( "install" );

        verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );        
        verifier.executeGoal( "org.apache.maven.its.it0020:maven-it-it0020:it0020" );
        verifier.assertFilePresent( "target/out.txt" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}

