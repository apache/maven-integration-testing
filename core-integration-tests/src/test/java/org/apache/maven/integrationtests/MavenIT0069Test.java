package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0069Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test offline mode.
     */
    public void testit0069()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0069" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        List cliOptions = new ArrayList();
        cliOptions.add( "-o" );
        itr.executeGoal( "compile", cliOptions );
        itr.assertFilePresent( "target/classes/org/apache/maven/it0069/ClassworldBasedThing.class" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

