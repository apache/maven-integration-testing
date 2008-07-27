package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0018Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Ensure that managed dependencies for dependency POMs are calculated
     * correctly when resolved. Removes commons-logging-1.0.3 and checks it is
     * redownloaded.
     */
    public void testit0018()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0018" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "commons-logging", "commons-logging", "1.0.3", "jar" );
        verifier.executeGoal( "package" );
        verifier.assertArtifactPresent( "commons-logging", "commons-logging", "1.0.3", "jar" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}

