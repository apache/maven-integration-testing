package org.apache.maven.it;

import java.io.File;
import java.util.HashMap;
import java.util.List;

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

    public void testResolverTransportWagon()
            throws Exception
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
            verifier.deleteDirectory( "target" );
            verifier.deleteArtifacts( "org.apache.maven.its.resolver-transport" );
            verifier.addCliOption( "-X" );
            verifier.addCliOption("-s" );
            verifier.addCliOption( new File( testDir, "settings.xml" ).getAbsolutePath() );
            verifier.addCliOption( "-Pmaven-core-it-repo" );
            verifier.addCliOption( "-Dmaven.resolver.transport=wagon" );
            // Maven will fail if project dependencies cannot be resolved.
            // As dependency exists ONLY in HTTP repo, it MUST be reached using selected transport and
            // successfully resolved from it.
            verifier.executeGoal( "verify" );
            verifier.verifyErrorFreeLog();
            // verify maven console output contains "[DEBUG] Using transporter WagonTransporter"
            verifyLogHasLine( verifier, "[DEBUG] Using transporter WagonTransporter" );
            verifier.resetStreams();
        }
        finally
        {
            server.stop();
        }
    }

    public void testResolverTransportNative()
            throws Exception
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
            verifier.deleteDirectory( "target" );
            verifier.deleteArtifacts( "org.apache.maven.its.resolver-transport" );
            verifier.addCliOption( "-X" );
            verifier.addCliOption("-s" );
            verifier.addCliOption( new File( testDir, "settings.xml" ).getAbsolutePath() );
            verifier.addCliOption( "-Pmaven-core-it-repo" );
            verifier.addCliOption( "-Dmaven.resolver.transport=native" );
            // Maven will fail if project dependencies cannot be resolved.
            // As dependency exists ONLY in HTTP repo, it MUST be reached using selected transport and
            // successfully resolved from it.
            verifier.executeGoal( "verify" );
            verifier.verifyErrorFreeLog();
            // verify maven console output contains "[DEBUG] Using transporter HttpTransporter"
            verifyLogHasLine( verifier, "[DEBUG] Using transporter HttpTransporter" );
            verifier.resetStreams();
        }
        finally
        {
            server.stop();
        }
    }

    public void verifyLogHasLine( final Verifier verifier, final String logline )
            throws VerificationException
    {
        List<String> lines = verifier.loadFile( verifier.getBasedir(), verifier.getLogFileName(), false );

        for ( String line : lines )
        {
            if ( !line.contains( logline ) )
            {
                return;
            }
        }
        throw new VerificationException( "Expected snippet not present in log: " + logline );
    }
}
