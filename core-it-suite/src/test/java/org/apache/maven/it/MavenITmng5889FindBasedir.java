package org.apache.maven.it;

import java.io.File;
import java.util.Properties;

import org.apache.maven.it.util.ResourceExtractor;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-5889">MNG-5889</a>:
 * check that extensions in <code>.mvn/</code> are found when Maven is run with <code>-f path/to/pom.xml</code>.
 */
public class MavenITmng5889FindBasedir
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng5889FindBasedir()
    {
        super( "[3.5.0,3.5.1)" );
    }

    protected MavenITmng5889FindBasedir( String constraint )
    {
        super( constraint );
    }

    /**
     * check that <code>path/to/.mvn/</code> is found when path to POM set by <code>--file path/to/pom.xml</code>
     */
    public void testMvnFileLongOption()
        throws Exception
    {
        runCoreExtensionWithOption( "--file", null );
    }

    /**
     * check that <code>path/to/.mvn/</code> is found when path to POM set by <code>-f path/to/pom.xml</code>
     */
    public void testMvnFileShortOption()
        throws Exception
    {
        runCoreExtensionWithOption( "-f", null );
    }

    /**
     * check that <code>path/to/.mvn/</code> is found when path to POM set by <code>--file path/to/module/pom.xml</code>
     */
    public void testMvnFileLongOptionModule()
        throws Exception
    {
        runCoreExtensionWithOption( "--file", "module" );
    }

    /**
     * check that <code>path/to/.mvn/</code> is found when path to POM set by <code>-f path/to/module/pom.xml</code>
     */
    public void testMvnFileShortOptionModule()
        throws Exception
    {
        runCoreExtensionWithOption( "-f", "module" );
    }

    private void runCoreExtensionWithOption( String option, String subdir )
        throws Exception
    {
        runCoreExtensionWithOption( option, subdir, true );
    }

    protected void runCoreExtensionWithOption( String option, String subdir, boolean pom )
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-5889-find.mvn" );

        File basedir = new File( testDir, "../mng-" + ( pom ? "5889" : "6223" ) + "-find.mvn" + option + ( pom ? "Pom" : "Dir" ) );
        basedir.mkdir();

        if ( subdir != null )
        {
            testDir = new File( testDir, subdir );
            basedir = new File( basedir, subdir );
            basedir.mkdirs();
        }

        Verifier verifier = newVerifier( basedir.getAbsolutePath() );
        verifier.getCliOptions().add( "-Dexpression.outputFile=" + new File( basedir, "expression.properties" ).getAbsolutePath() );
        verifier.getCliOptions().add( option ); // -f/--file client/pom.xml
        verifier.getCliOptions().add( ( pom ? new File( testDir, "pom.xml" ) : testDir ).getAbsolutePath() );
        verifier.setForkJvm( true ); // force forked JVM since we need the shell script to detect .mvn/ location
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        Properties props = verifier.loadProperties( "expression.properties" );
        assertEquals( "ok", props.getProperty( "project.properties.jvm-config" ) );
        assertEquals( "ok", props.getProperty( "project.properties.maven-config" ) );
    }
}
