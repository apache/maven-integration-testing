package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0065Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test that the basedir of the parent is set correctly.
     */
    public void testit0065()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0065" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven.its.it0065", "plugin", "1.0", "maven-plugin" );
        verifier.executeGoal( "install" );
        verifier.assertFilePresent( "subproject/target/child-basedir" );
        verifier.assertFilePresent( "parent-basedir" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

