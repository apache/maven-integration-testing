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
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.invoke( "test-compile" );
        itr.assertFilePresent( "target/classes/org/apache/maven/it0056/Person.class" );
        itr.assertFilePresent( "target/classes/org/apache/maven/it0056/PersonTwo.class" );
        itr.assertFilePresent( "target/classes/org/apache/maven/it0056/PersonThree.class" );
        itr.assertFilePresent( "target/test-classes/org/apache/maven/it0056/PersonTest.class" );
        itr.assertFilePresent( "target/test-classes/org/apache/maven/it0056/PersonTwoTest.class" );
        itr.assertFilePresent( "target/test-classes/org/apache/maven/it0056/PersonThreeTest.class" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

