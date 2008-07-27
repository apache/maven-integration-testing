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
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "package" );
        verifier.assertFilePresent( "target/classes/org/apache/maven/it0054/Person.class" );
        verifier.assertFilePresent( "target/test-classes/org/apache/maven/it0054/PersonTest.class" );
        verifier.assertFilePresent( "target/maven-it-it0054-1.0.jar" );
        verifier.assertFilePresent( "target/maven-it-it0054-1.0.jar!/it0054.properties" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

