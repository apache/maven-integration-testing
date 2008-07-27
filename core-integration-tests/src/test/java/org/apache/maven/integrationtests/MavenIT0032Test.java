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
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "package" );
        verifier.assertFilePresent( "target/classes/org/apache/maven/it0032/Person.class" );
        verifier.assertFilePresent( "target/test-classes/org/apache/maven/it0032/PersonTest.class" );
        verifier.assertFilePresent( "target/maven-it-it0032-1.0.jar" );
        verifier.assertFilePresent( "target/maven-it-it0032-1.0.jar!/it0032.properties" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

