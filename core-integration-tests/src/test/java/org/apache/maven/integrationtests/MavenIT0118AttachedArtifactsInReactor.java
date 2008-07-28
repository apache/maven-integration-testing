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
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( "org.apache.maven.its.it0118", "parent", "1.0", "pom" );
        itr.deleteArtifact( "org.apache.maven.its.it0118", "one", "1.0", "jar" );
        itr.deleteArtifact( "org.apache.maven.its.it0118", "two", "1.0", "pom" );
        itr.executeGoal( "package" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }
}
