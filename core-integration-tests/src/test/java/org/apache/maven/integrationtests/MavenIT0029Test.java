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
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( "org.apache.maven.it", "maven-it-it0029", "1.0-SNAPSHOT", "jar" );
        itr.deleteArtifact( "org.apache.maven.it", "maven-it-it0029-child", "1.0-SNAPSHOT", "jar" );
        itr.executeGoal( "install" );
        itr.assertFilePresent( "child-project/target/classes/org/apache/maven/it0029/Person.class" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

