package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0056Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test that multiple executions of the compile goal with different
     * includes/excludes will succeed.
     */
    public void testit0056()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0056" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "test-compile" );
        verifier.assertFilePresent( "target/classes/org/apache/maven/it0056/Person.class" );
        verifier.assertFilePresent( "target/classes/org/apache/maven/it0056/PersonTwo.class" );
        verifier.assertFilePresent( "target/classes/org/apache/maven/it0056/PersonThree.class" );
        verifier.assertFilePresent( "target/test-classes/org/apache/maven/it0056/PersonTest.class" );
        verifier.assertFilePresent( "target/test-classes/org/apache/maven/it0056/PersonTwoTest.class" );
        verifier.assertFilePresent( "target/test-classes/org/apache/maven/it0056/PersonThreeTest.class" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

