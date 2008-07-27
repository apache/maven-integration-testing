package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0029Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test for pluginManagement injection of plugin configuration.
     */
    public void testit0029()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0029" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven.it", "maven-it-it0029", "1.0-SNAPSHOT", "jar" );
        verifier.deleteArtifact( "org.apache.maven.it", "maven-it-it0029-child", "1.0-SNAPSHOT", "jar" );
        verifier.executeGoal( "install" );
        verifier.assertFilePresent( "child-project/target/classes/org/apache/maven/it0029/Person.class" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

