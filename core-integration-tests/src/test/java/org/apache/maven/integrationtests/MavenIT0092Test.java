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
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven", "maven-core-it-support", "1.0-SNAPSHOT", "jar" );
        verifier.executeGoal( "compile" );
        verifier.assertArtifactPresent( "org.apache.maven", "maven-core-it-support", "1.0-SNAPSHOT", "jar" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

