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
        IntegrationTestRunner itr = new IntegrationTestRunner( child1.getAbsolutePath() );

        itr.deleteArtifact( "org.apache.maven.plugins", "maven-help-plugin", "2.0.2", "jar" );

        itr.executeGoal( "org.apache.maven.plugins:maven-help-plugin:2.0.2:effective-pom" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

        File child2 = new File( testDir, "child2" );
        itr = new IntegrationTestRunner( child2.getAbsolutePath() );

        itr.executeGoal( "org.apache.maven.plugins:maven-help-plugin:2.0.2:effective-pom" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

