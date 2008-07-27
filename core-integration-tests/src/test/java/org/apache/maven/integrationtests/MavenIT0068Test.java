package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0068Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test repository accumulation.
     */
    public void testit0068()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0068" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.codehaus.modello", "modello-core", "1.0-alpha-3", "jar" );
        verifier.executeGoal( "generate-sources" );
        verifier.assertFilePresent( "target/generated-sources/modello/org/apache/maven/settings/Settings.java" );
        verifier.resetStreams();
    }
}

