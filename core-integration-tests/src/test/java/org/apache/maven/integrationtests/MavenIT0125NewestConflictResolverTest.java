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
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.invoke( "install" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

        testDir = extractTestResources( getClass(), "/it0125-newestConflictResolver/plugin" );
        itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.invoke( "install" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

        testDir = extractTestResources( getClass(), "/it0125-newestConflictResolver/project" );
        itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.invoke( "verify" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }
}
