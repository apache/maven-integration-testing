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
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        List cliOptions = new ArrayList();
        cliOptions.add( "-o" );
        verifier.executeGoal( "compile", cliOptions );
        verifier.assertFilePresent( "target/classes/org/apache/maven/it0069/ClassworldBasedThing.class" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

