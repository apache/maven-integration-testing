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
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "install" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        testDir = extractTestResources( getClass(), "/it0124-pomExtensionComponentOverride/plugin" );
        verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "install" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        testDir = extractTestResources( getClass(), "/it0124-pomExtensionComponentOverride/project" );
        verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "verify" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}
