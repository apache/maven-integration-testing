package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0060Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test aggregation of list configuration items when using
     * 'combine.children=append' attribute. Specifically, merge the list of
     * excludes for the testCompile mojo.
     */
    public void testit0060()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0060" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "test" );
        itr.assertFilePresent( "subproject/target/classes/org/apache/maven/it0060/Person.class" );
        itr.assertFilePresent( "subproject/target/test-classes/org/apache/maven/it0060/PersonTest.class" );
        itr.assertFileNotPresent( "subproject/target/test-classes/org/apache/maven/it0060/PersonTwoTest.class" );
        itr.assertFileNotPresent( "subproject/target/test-classes/org/apache/maven/it0060/PersonThreeTest.class" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

