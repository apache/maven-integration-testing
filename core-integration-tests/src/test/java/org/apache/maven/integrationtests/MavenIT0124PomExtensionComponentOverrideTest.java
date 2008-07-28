package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0124PomExtensionComponentOverrideTest
    extends AbstractMavenIntegrationTestCase
{
    /**
     * Test that ensures the POM extensions can override default component implementations.
     * 
     * @throws Exception
     * @see <a href="http://jira.codehaus.org/browse/MNG-2771">MNG-2771</a>
     */
    public void testit0124() throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0124-pomExtensionComponentOverride/extension" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "install" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

        testDir = extractTestResources( getClass(), "/it0124-pomExtensionComponentOverride/plugin" );
        itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "install" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

        testDir = extractTestResources( getClass(), "/it0124-pomExtensionComponentOverride/project" );
        itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "verify" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }
}
