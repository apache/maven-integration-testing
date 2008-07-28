package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.DefaultInvocationRequest;
import org.apache.maven.it.IntegrationTestRunner;
import org.apache.maven.it.InvocationRequest;

public class MavenIT0022Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test profile inclusion from profiles.xml (this one is activated by system property).
     */
    public void testit0022()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0022" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        
        InvocationRequest r = new DefaultInvocationRequest()
            .addSystemProperty( "includeProfile", "true" )
            .setGoals( "org.apache.maven.its.plugins:maven-it-plugin-touch:touch" );
        
        itr.invoke( r );
        itr.assertFilePresent( "target/test.txt" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }
}
