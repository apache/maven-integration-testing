package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.DefaultInvocationRequest;
import org.apache.maven.it.IntegrationTestRunner;
import org.apache.maven.it.InvocationRequest;

public class MavenIT0026Test
    extends AbstractMavenIntegrationTestCase
{
    public MavenIT0026Test()
        throws InvalidVersionSpecificationException
    {
        super( "[,2.1-SNAPSHOT)" );
    }

    /**
     * Test merging of global- and user-level settings.xml files.
     */
    public void testit0026()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0026" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        
        InvocationRequest r = new DefaultInvocationRequest()
            .addSystemProperty( "org.apache.maven.user-settings", "user-settings.xml" )
            .addSystemProperty( "org.apache.maven.global-settings", "global-settings.xml" )
            .setGoals( "org.apache.maven.its.plugins:maven-it-plugin-touch:touch" );
        
        verifier.invoke(  r );
        verifier.assertFilePresent( "target/test.txt" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}
