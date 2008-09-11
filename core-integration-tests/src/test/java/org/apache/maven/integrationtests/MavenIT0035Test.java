package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0035Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test artifact relocation.
     */
    public void testit0035()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0035" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( "org.apache.maven", "maven-core-it-support", "1.1", "jar" );
        itr.deleteArtifact( "org.apache.maven", "maven-core-it-support", "1.1", "pom" );
        itr.deleteArtifact( "org.apache.maven", "maven-core-it-support-old-location", "1.1", "pom" );
        itr.invoke( "package" );
        itr.assertArtifactPresent( "org.apache.maven", "maven-core-it-support", "1.1", "jar" );
        itr.assertArtifactPresent( "org.apache.maven", "maven-core-it-support", "1.1", "pom" );
        itr.assertArtifactPresent( "org.apache.maven", "maven-core-it-support-old-location", "1.1", "pom" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

