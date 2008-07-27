package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0095Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test URL calculation when modules are in sibling dirs of parent. (MNG-2006)
     */
    public void testit0095()
        throws Exception
    {
        // TODO: This is WRONG! Need to run only sub1 to effective-pom, then run all to verify.
        File testDir = extractTestResources( getClass(), "/it0095" );
        File sub1 = new File( testDir, "sub1" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( sub1.getAbsolutePath() );
        List options = new ArrayList();
        options.add( "-Doutput=\"" + new File( sub1, "target/effective-pom.xml" ).getAbsolutePath() + "\"" );
        verifier.executeGoal( "org.apache.maven.plugins:maven-help-plugin:2.0.2:effective-pom, verify", options );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}

