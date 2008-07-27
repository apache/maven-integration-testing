package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0055Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test that source includes/excludes with in the compiler plugin config.
     * This will test excludes and testExcludes...
     */
    public void testit0055()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0055" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "test-compile" );
        verifier.assertFilePresent( "target/classes/org/apache/maven/it0055/Person.class" );
        verifier.assertFilePresent( "target/test-classes/org/apache/maven/it0055/PersonTest.class" );
        verifier.assertFileNotPresent( "target/classes/org/apache/maven/it0055/PersonTwo.class" );
        verifier.assertFileNotPresent( "target/test-classes/org/apache/maven/it0055/PersonTwoTest.class" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

