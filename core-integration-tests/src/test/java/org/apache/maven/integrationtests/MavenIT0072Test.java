package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0072Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Verifies that property references with dotted notation work within
     * POM interpolation.
     */
    public void testit0072()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0072" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "package" );
        verifier.assertFilePresent( "target/maven-it-it0072-1.0-SNAPSHOT.jar" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

