package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0030Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test for injection of dependencyManagement through parents of
     * dependency poms.
     */
    public void testit0030()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0030" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven.it", "maven-it-it0030", "1.0-SNAPSHOT", "jar" );
        verifier.deleteArtifact( "org.apache.maven.it", "maven-it-it0030-child-hierarchy", "1.0-SNAPSHOT", "jar" );
        verifier.deleteArtifact( "org.apache.maven.it", "maven-it-it0030-child-project1", "1.0-SNAPSHOT", "jar" );
        verifier.deleteArtifact( "org.apache.maven.it", "maven-it-it0030-child-project2", "1.0-SNAPSHOT", "jar" );
        verifier.executeGoal( "install" );
        verifier.assertFilePresent( "child-hierarchy/project2/target/classes/org/apache/maven/it0001/Person.class" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

