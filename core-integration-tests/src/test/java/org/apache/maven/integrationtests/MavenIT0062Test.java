package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0062Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test that a deployment of a snapshot falls back to a non-snapshot repository if no snapshot repository is
     * specified.
     */
    public void testit0062()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0062" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven", "maven-it-it0062-SNAPSHOT", "1.0", "jar" );
        verifier.executeGoal( "deploy" );
        verifier.assertFilePresent( "target/classes/org/apache/maven/it0062/Person.class" );
        verifier.assertFilePresent( "target/maven-it-it0062-1.0-SNAPSHOT.jar" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

