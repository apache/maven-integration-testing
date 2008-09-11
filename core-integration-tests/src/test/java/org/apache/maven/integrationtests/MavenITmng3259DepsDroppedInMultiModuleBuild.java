package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenITmng3259DepsDroppedInMultiModuleBuild
    extends AbstractMavenIntegrationTestCase
{
    public void testitMNG3259 ()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/mng-3259-depsDroppedInMultiModuleBuild" );
        IntegrationTestRunner itr;
        itr = new IntegrationTestRunner( new File( testDir, "parent" ).getAbsolutePath() );
        List cliOptions = new ArrayList();
        itr.executeGoal( "install", cliOptions );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
        itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.invoke( "install" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }
}
