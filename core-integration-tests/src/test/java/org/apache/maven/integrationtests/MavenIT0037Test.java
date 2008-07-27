package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0037Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test building with alternate pom file using '-f'
     */
    public void testit0037()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0037" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        List cliOptions = new ArrayList();
        cliOptions.add( "-f pom2.xml" );
        verifier.executeGoal( "package", cliOptions );
        verifier.assertFilePresent( "target/maven-it-it0037-1.0-build2.jar" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

