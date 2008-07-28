package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0063Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test the use of a system scoped dependency to tools.jar.
     */
    public void testit0063()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0063" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "package" );
        itr.assertFilePresent( "target/classes/org/apache/maven/it0063/Person.class" );
        itr.assertFilePresent( "target/test-classes/org/apache/maven/it0063/PersonTest.class" );
        itr.assertFilePresent( "target/maven-it-it0063-1.0.jar" );
        itr.assertFilePresent( "target/maven-it-it0063-1.0.jar!/it0063.properties" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

