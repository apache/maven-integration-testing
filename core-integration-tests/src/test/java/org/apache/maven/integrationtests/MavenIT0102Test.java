package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0102Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test that &lt;activeByDefault/&gt; calculations for profile activation only
     * use profiles defined in the POM. [MNG-2136]
     */
    public void testit0102()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0102" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        List options = new ArrayList();
        options.add( "-Doutput=" + new File( testDir, "target/effective-pom.txt" ).getAbsolutePath() );
        verifier.executeGoal( "org.apache.maven.plugins:maven-help-plugin:2.0.2:effective-pom, verify", options );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}

