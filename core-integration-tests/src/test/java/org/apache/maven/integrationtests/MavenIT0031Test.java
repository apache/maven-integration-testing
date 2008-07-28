package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.DefaultInvocationRequest;
import org.apache.maven.it.IntegrationTestRunner;
import org.apache.maven.it.InvocationRequest;

public class MavenIT0031Test
    extends AbstractMavenIntegrationTestCase
{
    public MavenIT0031Test()
        throws InvalidVersionSpecificationException
    {
        // we still support pluginGroups in 2.1-SNAPSHOT
        //super( "[,2.1-SNAPSHOT)" );
    }            

    /**
     * Test usage of plugins.xml mapping file on the repository to resolve plugin artifactId from it's prefix using the
     * pluginGroups in the provided settings.xml.
     */
    public void testit0031()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0031" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        List cliOptions = new ArrayList();
        cliOptions.add( "--settings settings.xml" );
        
        InvocationRequest r = new DefaultInvocationRequest()
            .addSystemProperty( "model", "src/main/mdo/test.mdo" )
            .addSystemProperty( "version", "1.0.0" )
            .setGoals( "modello:java" )
            .addCliOption( "--settings settings.xml" );
        
        itr.assertFilePresent( "target/generated-sources/modello/org/apache/maven/it/it0031/Root.java" );
        itr.resetStreams();
    }
}
