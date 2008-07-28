package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0097Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test that the implied relative path for the parent POM works, even two
     * levels deep.
     */
    public void testit0097()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0097" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "package" );
        itr.assertFilePresent( "project/project-level2/project-level3/target/it0097.txt" );
        itr.assertFilePresent( "project/project-sibling-level2/target/it0097.txt" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

