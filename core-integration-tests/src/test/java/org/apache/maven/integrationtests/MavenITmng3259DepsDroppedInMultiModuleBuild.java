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
        IntegrationTestRunner verifier;
        verifier = new IntegrationTestRunner( new File( testDir, "parent" ).getAbsolutePath() );
        List cliOptions = new ArrayList();
        verifier.executeGoal( "install", cliOptions );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
        verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "install" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}
