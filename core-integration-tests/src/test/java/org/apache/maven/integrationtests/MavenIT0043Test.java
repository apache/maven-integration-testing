package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0043Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test for repository inheritence - ensure using the same id overrides the defaults
     */
    public void testit0043()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0043" );

        File child1 = new File( testDir, "child1" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( child1.getAbsolutePath() );

        verifier.deleteArtifact( "org.apache.maven.plugins", "maven-help-plugin", "2.0.2", "jar" );

        verifier.executeGoal( "org.apache.maven.plugins:maven-help-plugin:2.0.2:effective-pom" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        File child2 = new File( testDir, "child2" );
        verifier = new IntegrationTestRunner( child2.getAbsolutePath() );

        verifier.executeGoal( "org.apache.maven.plugins:maven-help-plugin:2.0.2:effective-pom" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

