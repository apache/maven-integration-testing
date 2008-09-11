package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0073Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Tests context passing between mojos in the same plugin.
     */
    public void testit0073()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0073" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( "org.apache.maven.its.plugins", "maven-it-plugin-context-passing", "1.0", "maven-plugin" );
        itr.invoke( "org.apache.maven.its.plugins:maven-it-plugin-context-passing:throw, org.apache.maven.its.plugins:maven-it-plugin-context-passing:catch" );
        itr.assertFilePresent( "target/thrown-value" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }
}
