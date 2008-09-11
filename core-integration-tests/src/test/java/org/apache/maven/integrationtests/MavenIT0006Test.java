package org.apache.maven.integrationtests;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0006Test
    extends AbstractMavenIntegrationTestCase
{
    /**
     * Integration test for the itr plugin.
     */
    public void testit0006()
        throws Exception
    {
        IntegrationTestRunner itr = createTestRunner();
        itr.invoke( "integration-test" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }
}

