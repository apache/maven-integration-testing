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
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "test" );
        verifier.assertFilePresent( "target/classes/test.properties" );
        verifier.assertFilePresent( "target/mojo-generated.properties" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

