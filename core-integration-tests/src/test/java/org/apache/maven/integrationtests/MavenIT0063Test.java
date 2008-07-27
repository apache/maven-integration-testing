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
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "package" );
        verifier.assertFilePresent( "target/classes/org/apache/maven/it0063/Person.class" );
        verifier.assertFilePresent( "target/test-classes/org/apache/maven/it0063/PersonTest.class" );
        verifier.assertFilePresent( "target/maven-it-it0063-1.0.jar" );
        verifier.assertFilePresent( "target/maven-it-it0063-1.0.jar!/it0063.properties" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

