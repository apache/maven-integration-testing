package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0099Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test that parent-POMs cached during a build are available as parents
     * to other POMs in the multimodule build. [MNG-2130]
     */
    public void testit0099()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0099" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( "org.apache.maven.it0099", "maven-it-it0099-parent", "1", "pom" );
        itr.invoke( "package" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

