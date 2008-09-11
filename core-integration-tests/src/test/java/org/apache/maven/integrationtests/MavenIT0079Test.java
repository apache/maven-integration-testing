package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0079Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test that source attachments have the same build number as the main
     * artifact when deployed.
     */
    public void testit0079()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0079" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.invoke( "deploy" );
        itr.assertFilePresent(
            "target/test-repo/org/apache/maven/its/it0079/maven-it-it0079/SNAPSHOT/maven-it-it0079-*-1.jar" );
        itr.assertFilePresent(
            "target/test-repo/org/apache/maven/its/it0079/maven-it-it0079/SNAPSHOT/maven-it-it0079-*-1-sources.jar" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

