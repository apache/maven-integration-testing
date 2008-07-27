package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.maven.it.IntegrationTestRunner;

/**
 * This is a sample integration test. The IT tests typically
 * operate by having a sample project in the
 * /src/test/resources folder along with a junit test like
 * this one. The junit test uses the verifier (which uses
 * the invoker) to invoke a new instance of Maven on the
 * project in the resources folder. It then checks the
 * results. This is a non-trivial example that shows two
 * phases. See more information inline in the code.
 *
 * @author <a href="mailto:brianf@apache.org">Brian Fox</a>
 *
 */
public class MavenITmng3372DirectInvocationOfPlugins
    extends AbstractMavenIntegrationTestCase
{

    public void testitMNG3372()
        throws Exception
    {
        // The testdir is computed from the location of this
        // file.
        File testBaseDir = extractTestResources( getClass(), "/mng-3372-directInvocationOfPlugins/direct-using-prefix" );
        File plugin = new File( testBaseDir, "plugin" );
        File project = new File( testBaseDir, "project" );
        File settingsFile = new File( testBaseDir, "settings.xml" );

        IntegrationTestRunner verifier = new IntegrationTestRunner( plugin.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven.its.mng3372", "mng3372-maven-plugin", "1", "jar" );
        verifier.executeGoal( "clean, install" );
        verifier = new IntegrationTestRunner( project.getAbsolutePath() );

        List cliOptions = new ArrayList();
        cliOptions.add( "-s" );
        cliOptions.add( "\"" + settingsFile.getAbsolutePath() + "\"" );

        verifier.executeGoal( "mng3372:test", cliOptions );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }

    public void testDependencyTreeInvocation()
        throws Exception
    {
        // The testdir is computed from the location of this
        // file.
        File testBaseDir = extractTestResources( getClass(), "/mng-3372-directInvocationOfPlugins/dependency-tree" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testBaseDir.getAbsolutePath() );
        List cliOptions = new ArrayList();
        cliOptions.add( "-U" );
        verifier.executeGoal( "dependency:tree", cliOptions );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}
