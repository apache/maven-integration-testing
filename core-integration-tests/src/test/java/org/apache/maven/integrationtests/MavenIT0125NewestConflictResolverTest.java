package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0125NewestConflictResolverTest
    extends AbstractMavenIntegrationTestCase
{
    /**
     * Test that ensures the newest-wins conflict resolver is used.
     * 
     * @throws Exception
     * @see <a href="http://jira.codehaus.org/browse/MNG-612">MNG-612</a>
     */
    public void testit0125() throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0125-newestConflictResolver/dependency" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "install" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        testDir = extractTestResources( getClass(), "/it0125-newestConflictResolver/plugin" );
        verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "install" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        testDir = extractTestResources( getClass(), "/it0125-newestConflictResolver/project" );
        verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "verify" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}
