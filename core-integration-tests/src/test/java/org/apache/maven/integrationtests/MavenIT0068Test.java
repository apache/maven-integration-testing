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
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( "org.codehaus.modello", "modello-core", "1.0-alpha-3", "jar" );
        itr.invoke( "generate-sources" );
        itr.assertFilePresent( "target/generated-sources/modello/org/apache/maven/settings/Settings.java" );
        itr.resetStreams();
    }
}

