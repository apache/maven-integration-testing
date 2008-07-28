package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0100Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test that ${parent.artifactId} resolves correctly. [MNG-2124]
     */
    public void testit0100()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0100" );
        File child = new File( testDir, "parent/child" );
        IntegrationTestRunner itr = new IntegrationTestRunner( child.getAbsolutePath() );
        List options = new ArrayList();
        options.add( "-Doutput=\"" + new File( child, "target/effective-pom.txt" ).getAbsolutePath() + "\"" );
        itr.executeGoal( "org.apache.maven.plugins:maven-help-plugin:2.0.2:effective-pom, verify", options );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }
}

