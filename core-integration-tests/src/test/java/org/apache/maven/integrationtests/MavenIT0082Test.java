package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0082Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test that the reactor can establish the artifact location of known projects for dependencies
     * using process-sources to see that it works even when they aren't compiled
     */
    public void testit0082()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0082" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.invoke( "process-sources" );
        itr.assertFilePresent( "test-component-c/target/my-test" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

