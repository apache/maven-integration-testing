package org.apache.maven.integrationtests;

import org.apache.maven.it.DefaultInvocationRequest;
import org.apache.maven.it.InvocationRequest;
import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

import java.io.File;
import java.util.Properties;

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
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/it0021" );
        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
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

