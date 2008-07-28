package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0051Test
    extends AbstractMavenIntegrationTestCase
{
    public MavenIT0051Test()                                                                                                                          
        throws InvalidVersionSpecificationException                                                                                                   
    {                                                                                                                                                 
        super( "[,2.1-SNAPSHOT)" );                                                                                                                   
    }    

    /**
     * Test source attachment when -DperformRelease=true is specified.
     */
    public void testit0051()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0051" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        List cliOptions = new ArrayList();
        cliOptions.add( "--no-plugin-registry -DperformRelease=true" );
        itr.executeGoal( "package", cliOptions );
        itr.assertFilePresent( "target/maven-it-it0051-1.0.jar" );
        itr.assertFilePresent( "target/maven-it-it0051-1.0-sources.jar" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

