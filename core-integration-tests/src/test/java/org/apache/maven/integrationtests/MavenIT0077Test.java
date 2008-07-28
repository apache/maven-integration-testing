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
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( "org.apache.maven.its.it0077", "sub1", "1.0", "test-jar" );
        itr.executeGoal( "install" );
        itr.assertArtifactPresent( "org.apache.maven.its.it0077", "sub1", "1.0", "test-jar" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }
}

