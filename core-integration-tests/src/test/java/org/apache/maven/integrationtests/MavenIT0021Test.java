package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.DefaultInvocationRequest;
import org.apache.maven.it.IntegrationTestRunner;
import org.apache.maven.it.InvocationRequest;

public class MavenIT0021Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test pom-level profile inclusion (this one is activated by system
     * property).
     */
    public void testit0021()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0021" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven", "maven-core-it-support", "1.0", "jar" );
        
        InvocationRequest r = new DefaultInvocationRequest()
            .addSystemProperty( "includeProfile", "true" )
            .setGoals( "compile" );
        
        verifier.invoke( r );
        verifier.assertArtifactPresent( "org.apache.maven", "maven-core-it-support", "1.0", "jar" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

