package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0067Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test activation of a profile from the command line.
     */
    public void testit0067()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0067" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( "org.apache.maven", "maven-core-it-support", "1.0", "jar" );
        List cliOptions = new ArrayList();
        cliOptions.add( "-P test-profile" );
        itr.executeGoal( "compile", cliOptions );
        itr.assertFilePresent( "target/classes/org/apache/maven/it0067/Person.class" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

