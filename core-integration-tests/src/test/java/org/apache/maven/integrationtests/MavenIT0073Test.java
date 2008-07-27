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
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven.its.plugins", "maven-it-plugin-context-passing", "1.0", "maven-plugin" );
        verifier.executeGoal( "org.apache.maven.its.plugins:maven-it-plugin-context-passing:throw, org.apache.maven.its.plugins:maven-it-plugin-context-passing:catch" );
        verifier.assertFilePresent( "target/thrown-value" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}
