package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.IntegrationTestRunner;

public class MavenITmng3391ImportScopeErrorScenariosTest
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng3391ImportScopeErrorScenariosTest()
        throws InvalidVersionSpecificationException
    {
        super( "(2.0.8,)" ); // only test in 2.0.9+
    }

    public void testitMNG3391a()
        throws Exception
    {
        File testDir = extractTestResources( getClass(),
                                                                 "/mng-3391-importScopeErrorScenarios/depMgmt-importPom-noParentCycle" );

        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );

        itr.deleteArtifact( "org.apache.maven.its.mng3391.2", "dm-pom", "1", "pom" );

        itr.invoke( "install" );

        itr.verifyErrorFreeLog();

        itr.resetStreams();
    }

    public void testitMNG3391b()
        throws Exception
    {
        File testDir = extractTestResources( getClass(),
                                                                 "/mng-3391-importScopeErrorScenarios/depMgmt-importPom-noParentCycle" );

        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );

        itr.deleteArtifact( "org.apache.maven.its.mng3391.2", "dm-pom", "1", "pom" );

        IntegrationTestRunner v2 = new IntegrationTestRunner( new File( testDir, "dm-pom" ).getAbsolutePath() );
        v2.invoke( "install" );
        v2.verifyErrorFreeLog();
        v2.resetStreams();

        itr.invoke( "install" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }

    public void testitMNG3391c()
        throws Exception
    {
        File testDir = extractTestResources( getClass(),
                                                                 "/mng-3391-importScopeErrorScenarios/depMgmt-importPom-parentCycle" );

        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );

        itr.deleteArtifact( "org.apache.maven.its.mng3391.1", "dm-pom", "1", "pom" );

        itr.invoke( "install" );

        itr.verifyErrorFreeLog();

        itr.resetStreams();
    }

    public void testitMNG3391d()
        throws Exception
    {
        File testDir = extractTestResources( getClass(),
                                                                 "/mng-3391-importScopeErrorScenarios/depMgmt-importPom-parentCycle" );

        IntegrationTestRunner itr = new IntegrationTestRunner( new File( testDir, "dm-pom" ).getAbsolutePath() );
        itr.invoke( "install" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }

}
