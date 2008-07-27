package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0077Test
    extends AbstractMavenIntegrationTestCase
{
    /**
     * Test test jar attachment.
     */
    public void testit0077()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0077" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven.its.it0077", "sub1", "1.0", "test-jar" );
        verifier.executeGoal( "install" );
        verifier.assertArtifactPresent( "org.apache.maven.its.it0077", "sub1", "1.0", "test-jar" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}

