package org.apache.maven.it;

import java.io.File;
import java.util.HashMap;

import org.apache.maven.it.util.ResourceExtractor;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-7470">MNG-7470</a>:
 * check that Maven two bundled transports works as expected.
 */
public class MavenITmng7470ResolverTransportTest
        extends AbstractMavenIntegrationTestCase
{
    public MavenITmng7470ResolverTransportTest()
    {
        super( "[3.9.0,)" );
    }

    private void performTest( final String transport, final String logSnippet ) throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-7470-resolver-transport" );

        HttpServer server = HttpServer.builder()
                .port( 0 )
                .source( new File( testDir, "repo" ) )
                .build();
        server.start();
        try
        {
            Verifier verifier = newVerifier( testDir.getAbsolutePath() );
            HashMap<String, String> properties = new HashMap<>();
            properties.put( "@port@", Integer.toString( server.port() ) );
            verifier.filterFile( "settings-template.xml", "settings.xml", "UTF-8", properties );

            verifier = newVerifier( new File( testDir, "project" ).getAbsolutePath() );
            verifier.setLogFileName( transport + "-transport.log" );
            verifier.deleteDirectory( "target" );
            verifier.deleteArtifacts( "org.apache.maven.its.resolver-transport" );
            verifier.addCliOption( "-X" );
            verifier.addCliOption( "-s" );
            verifier.addCliOption( new File( testDir, "settings.xml" ).getAbsolutePath() );
            verifier.addCliOption( "-Pmaven-core-it-repo" );
            verifier.addCliOption( "-Dmaven.resolver.transport=" + transport );
            // Maven will fail if project dependencies cannot be resolved.
            // As dependency exists ONLY in HTTP repo, it MUST be reached using selected transport and
            // successfully resolved from it.
            verifier.executeGoal( "verify" );
            verifier.verifyErrorFreeLog();
            // verify maven console output contains "[DEBUG] Using transporter XXXTransporter"
            verifier.verifyTextInLog( logSnippet );
            verifier.resetStreams();
        }
        finally
        {
            server.stop();
        }
    }

    public void testResolverTransportWagon()
            throws Exception
    {
        performTest( "wagon", "[DEBUG] Using transporter WagonTransporter" );
    }

    public void testResolverTransportNative()
            throws Exception
    {
        performTest( "native", "[DEBUG] Using transporter HttpTransporter" );
    }
}
