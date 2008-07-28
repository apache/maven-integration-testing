package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0066Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test that nonstandard POM files will be installed correctly.
     */
    public void testit0066()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0066" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        List cliOptions = new ArrayList();
        cliOptions.add( "-f other-pom.xml" );
        itr.executeGoal( "install", cliOptions);
        itr.assertFilePresent( "" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }
}

