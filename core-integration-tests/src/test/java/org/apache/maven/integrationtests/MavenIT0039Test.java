package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0039Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test reactor for projects that have release-pom.xml in addition to
     * pom.xml. The release-pom.xml file should be chosen above pom.xml for
     * these projects in the build.
     */
    public void testit0039()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0039" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        List cliOptions = new ArrayList();
        cliOptions.add( "-r" );
        itr.executeGoal( "package", cliOptions );
        itr.assertFilePresent( "project/target/maven-it-it0039-p1-1.0.jar" );
        itr.assertFilePresent( "project2/target/maven-it-it0039-p2-1.0.jar" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

