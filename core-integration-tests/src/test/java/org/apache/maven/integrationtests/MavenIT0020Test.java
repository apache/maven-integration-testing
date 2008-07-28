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
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( "org.apache.maven.its.plugins", "maven-it-it0020", "1.0-SNAPSHOT", "maven-plugin" );
        itr.executeGoal( "install" );

        itr = new IntegrationTestRunner( testDir.getAbsolutePath() );        
        itr.executeGoal( "org.apache.maven.its.it0020:maven-it-it0020:it0020" );
        itr.assertFilePresent( "target/out.txt" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }
}

