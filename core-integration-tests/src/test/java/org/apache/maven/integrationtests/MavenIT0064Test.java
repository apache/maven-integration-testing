package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0064Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test the use of a mojo that uses setters instead of private fields
     * for the population of configuration values.
     */
    public void testit0064()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0064" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( "org.apache.maven.its.plugins", "maven-it-plugin-setter", "1.0", "maven-plugin" );
        itr.executeGoal( "org.apache.maven.its.plugins:maven-it-plugin-setter:setter-touch" );
        itr.assertFilePresent( "target/fooValue" );
        itr.assertFilePresent( "target/barValue.baz" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

