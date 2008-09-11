package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0128DistMgmtSiteUrlParentCalculationTest
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test DistributionManagement Site-URL calculation when modules are in sibling dirs of parent. (MNG-3134)
     */
    public void testit0128()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0128-distMgmtSiteUrlParentCalc" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.invoke( "integration-test" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

