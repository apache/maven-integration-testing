package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0119PluginPrefixOrder
    extends AbstractMavenIntegrationTestCase
{
    public void testit0119()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0119-pluginprefixorder" );

        IntegrationTestRunner verifier;

        // Install the parent POM, extension and the plugin
        verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );

        verifier.executeGoal( "install" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
        

        // now run the test. Since we have apache and codehaus, i should get the apache one first
        testDir = extractTestResources( getClass(), "/it0119-pluginprefixorder/test-project" );
        verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "it0119:apache" );
        verifier.verifyErrorFreeLog();

        
//      now run the test. Since we have apache and codehaus and a prefix in my settings, i should get the custom one first
        testDir = extractTestResources( getClass(), "/it0119-pluginprefixorder/test-project" );
        verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        
        //use my custom settings upon invocation.
        ArrayList cli = new ArrayList();
        cli.add("-s '" +testDir.getAbsolutePath()+"/settings.xml'");
        verifier.executeGoal( "it0119:custom", cli );
        verifier.verifyErrorFreeLog();
    }
}
