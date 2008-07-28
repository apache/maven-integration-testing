package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0032Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Tests that a specified Maven version requirement that is lower doesn't cause any problems
     */
    public void testit0032()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0032" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "package" );
        itr.assertFilePresent( "target/classes/org/apache/maven/it0032/Person.class" );
        itr.assertFilePresent( "target/test-classes/org/apache/maven/it0032/PersonTest.class" );
        itr.assertFilePresent( "target/maven-it-it0032-1.0.jar" );
        itr.assertFilePresent( "target/maven-it-it0032-1.0.jar!/it0032.properties" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

