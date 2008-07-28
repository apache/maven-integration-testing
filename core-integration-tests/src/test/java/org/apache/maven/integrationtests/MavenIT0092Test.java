package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0092Test
    extends AbstractMavenIntegrationTestCase
{
    public MavenIT0092Test()                                                                                                                                 
        throws InvalidVersionSpecificationException                                                                                                          
    {                                                                                                                                                        
        super( "[,2.1-SNAPSHOT)" );                                                                                                                          
    }    
    
    /**
     * Test that legacy repositories with legacy snapshots download correctly.
     */
    public void testit0092()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0092" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( "org.apache.maven", "maven-core-it-support", "1.0-SNAPSHOT", "jar" );
        itr.executeGoal( "compile" );
        itr.assertArtifactPresent( "org.apache.maven", "maven-core-it-support", "1.0-SNAPSHOT", "jar" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

