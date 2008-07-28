package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0088Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test path translation.
     */
    public void testit0088()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0088" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "test" );
        itr.assertFilePresent( "target/classes/test.properties" );
        itr.assertFilePresent( "target/mojo-generated.properties" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

