package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0118AttachedArtifactsInReactor
    extends AbstractMavenIntegrationTestCase
{
    public void testit0118()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0118-attachedartifactinreactor" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven.its.it0118", "parent", "1.0", "pom" );
        verifier.deleteArtifact( "org.apache.maven.its.it0118", "one", "1.0", "jar" );
        verifier.deleteArtifact( "org.apache.maven.its.it0118", "two", "1.0", "pom" );
        verifier.executeGoal( "package" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}
