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
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "test-compile" );
        itr.assertFilePresent( "target/classes/org/apache/maven/it0055/Person.class" );
        itr.assertFilePresent( "target/test-classes/org/apache/maven/it0055/PersonTest.class" );
        itr.assertFileNotPresent( "target/classes/org/apache/maven/it0055/PersonTwo.class" );
        itr.assertFileNotPresent( "target/test-classes/org/apache/maven/it0055/PersonTwoTest.class" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

