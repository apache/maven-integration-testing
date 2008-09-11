package org.apache.maven.integrationtests;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.IntegrationTestException;
import org.apache.maven.it.IntegrationTestRunner;

public class MavenITmng3220ImportScopeTest
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng3220ImportScopeTest()
        throws InvalidVersionSpecificationException
    {
        super( "(2.0.8,)" ); // only test in 2.0.9+
    }

    public void testitMNG3220a()
        throws Exception
    {
        File testDir = extractTestResources( getClass(),
                                                                 "/mng-3220-importedDepMgmt/imported-pom-depMgmt" );

        File dmDir = new File( testDir, "dm-pom" );
        IntegrationTestRunner itr = new IntegrationTestRunner( dmDir.getAbsolutePath() );

        itr.invoke( "install" );

        itr.verifyErrorFreeLog();
        itr.resetStreams();

        File projectDir = new File( testDir, "project" );
        itr = new IntegrationTestRunner( projectDir.getAbsolutePath() );

        itr.invoke( "package" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }

    public void testitMNG3220b()
        throws Exception
    {
        File testDir = extractTestResources( getClass(),
                                                                 "/mng-3220-importedDepMgmt/depMgmt-pom-module-notImported" );

        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );

        try
        {
            itr.invoke( "install" );
            fail( "Should fail to build with missing junit version." );
        }
        catch ( IntegrationTestException e )
        {
        }

        itr.resetStreams();

        List lines = itr.loadFile( new File( testDir, "log.txt" ), false );

        boolean found = false;
        for ( Iterator it = lines.iterator(); it.hasNext(); )
        {
            String line = (String) it.next();
            if ( line.indexOf( "\'dependencies.dependency.version\' is missing for junit:junit") > -1 )
            {
                found = true;
                break;
            }
        }

        assertTrue( "Should have found validation error line in output.", found );
    }

}
