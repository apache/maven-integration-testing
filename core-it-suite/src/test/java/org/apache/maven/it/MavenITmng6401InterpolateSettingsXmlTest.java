package org.apache.maven.it;

import java.io.File;
import java.util.Properties;

import org.apache.maven.it.util.ResourceExtractor;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-6401">MNG-6401</a>:
 * check interpolation for various types fields in settings.xml.
 */
public class MavenITmng6401InterpolateSettingsXmlTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng6401InterpolateSettingsXmlTest()
    {
        super( "[3.5.4,)" );
    }

    public void testInterpolateSettingsXml()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-6401" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.addCliOption( "--settings" );
        verifier.addCliOption( "settings.xml" );
        verifier.setEnvironmentVariable("MAVEN_PROXY_ACTIVE_BOOLEAN", "true");
        verifier.setEnvironmentVariable("MAVEN_PROXY_HOST_STRING", "myproxy.host.net");
        verifier.setEnvironmentVariable("MAVEN_PROXY_PORT_INT", "18080");
        verifier.executeGoal( "validate" );

        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        Properties props = verifier.loadProperties( "target/settings.properties" );
        assertEquals( "true", props.getProperty( "settings.proxies.0.active" ) );
        assertEquals( "myproxy.host.net", props.getProperty( "settings.proxies.0.host" ) );
        assertEquals( "18080", props.getProperty( "settings.proxies.0.port" ) );
    }
}

