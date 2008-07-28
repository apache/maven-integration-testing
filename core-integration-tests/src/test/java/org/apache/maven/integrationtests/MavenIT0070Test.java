package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0070Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test a RAR generation.
     */
    public void testit0070()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0070" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "package" );
        itr.assertFilePresent( "target/maven-it-it0070-1.0.rar" );
        itr.assertFilePresent( "target/maven-it-it0070-1.0.rar!/META-INF/ra.xml" );
        itr.assertFilePresent( "target/maven-it-it0070-1.0.rar!/SomeResource.txt" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

