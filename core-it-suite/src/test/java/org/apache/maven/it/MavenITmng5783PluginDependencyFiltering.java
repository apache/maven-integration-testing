package org.apache.maven.it;

import java.io.File;
import java.util.List;

import org.apache.maven.it.util.ResourceExtractor;

public class MavenITmng5783PluginDependencyFiltering
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng5783PluginDependencyFiltering()
    {
        super( "[3.0,)" );
    }

    public void testSLF4j()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-5783-plugin-dependency-filtering" );
        Verifier verifier = newVerifier( new File( testDir, "plugin" ).getAbsolutePath(), "remote" );
        verifier.executeGoal( "install" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        verifier = newVerifier( new File( testDir, "slf4j" ).getAbsolutePath(), "remote" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        final List<String> dependencies = verifier.loadLines( "target/dependencies.txt", "UTF-8" );
        assertTrue( contains( dependencies, "org.slf4j:slf4j-api:jar:1.7.5" ) );
    }

    private static boolean contains( final List<String> lines, final String pattern )
    {
        for ( int i = 0, l0 = lines.size(); i < l0; i++ )
        {
            if ( lines.get( i ).contains( pattern ) )
            {
                return true;
            }
        }

        return false;
    }
}
