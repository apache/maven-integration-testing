package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0070Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test a RAR generation.
     */
    public void testit0070()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0070" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "package" );
        verifier.assertFilePresent( "target/maven-it-it0070-1.0.rar" );
        verifier.assertFilePresent( "target/maven-it-it0070-1.0.rar!/META-INF/ra.xml" );
        verifier.assertFilePresent( "target/maven-it-it0070-1.0.rar!/SomeResource.txt" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

