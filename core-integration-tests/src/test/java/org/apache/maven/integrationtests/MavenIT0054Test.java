package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0054Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test resource filtering.
     */
    public void testit0054()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0054" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "package" );
        itr.assertFilePresent( "target/classes/org/apache/maven/it0054/Person.class" );
        itr.assertFilePresent( "target/test-classes/org/apache/maven/it0054/PersonTest.class" );
        itr.assertFilePresent( "target/maven-it-it0054-1.0.jar" );
        itr.assertFilePresent( "target/maven-it-it0054-1.0.jar!/it0054.properties" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

