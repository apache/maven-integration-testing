package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0033Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test an EAR generation
     */
    public void testit0033()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0033" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "package" );
        itr.assertFilePresent( "target/maven-it-it0033-1.0.ear" );
        itr.assertFilePresent( "target/maven-it-it0033-1.0.ear!/META-INF/application.xml" );
        itr.assertFilePresent( "target/maven-it-it0033-1.0.ear!/META-INF/appserver-application.xml" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

