package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0123SnapshotRangeRepositoryTest
    extends AbstractMavenIntegrationTestCase
{
    /**
     * Test that snapshot repositories are checked for ranges with snapshot boundaries.
     * 
     * @throws Exception
     * @see <a href="http://jira.codehaus.org/browse/MNG-2994">MNG-2994</a>
     */
    public void testit0123() throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0123-snapshotRangeRepository" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "compile" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }
}
