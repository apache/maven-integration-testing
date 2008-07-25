package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.DefaultInvocationRequest;
import org.apache.maven.it.InvocationRequest;
import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

public class MavenIT0022Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test profile inclusion from profiles.xml (this one is activated by system property).
     */
    public void testit0022()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/it0022" );
        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        
        InvocationRequest r = new DefaultInvocationRequest()
            .addSystemProperty( "includeProfile", "true" )
            .setGoals( "org.apache.maven.its.plugins:maven-it-plugin-touch:touch" );
        
        verifier.invoke( r );
        verifier.assertFilePresent( "target/test.txt" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}
