package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0001Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Builds upon it0000: we add an application resource that is packaged
     * up in the resultant JAR.
     */
    public void testit0001()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0001" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "package" );
        verifier.assertFilePresent( "target/classes/org/apache/maven/it0001/Person.class" );
        verifier.assertFilePresent( "target/test-classes/org/apache/maven/it0001/PersonTest.class" );
        verifier.assertFilePresent( "target/maven-it-it0001-1.0.jar" );
        verifier.assertFilePresent( "target/maven-it-it0001-1.0.jar!/it0001.properties" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

