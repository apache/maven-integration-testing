package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0044Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test --settings CLI option
     */
    public void testit0044()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0044" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        List cliOptions = new ArrayList();
        cliOptions.add( "--settings settings.xml" );
        itr.executeGoal( "org.apache.maven.its.plugins:maven-it-plugin-touch:touch", cliOptions );
        itr.assertFilePresent( "target/test.txt" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

