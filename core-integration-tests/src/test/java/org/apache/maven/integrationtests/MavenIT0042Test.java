package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0042Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test that the reactor can establish the artifact location of known projects for dependencies
     */
    public void testit0042()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0042" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "package" );
        verifier.assertFilePresent( "test-component-a/target/test-component-a-0.1.jar" );
        verifier.assertFilePresent( "test-component-b/target/test-component-b-0.1.jar" );
        verifier.assertFilePresent( "test-component-c/target/test-component-c-0.1.war" );
        verifier.assertFilePresent(
            "test-component-c/target/test-component-c-0.1.war!/WEB-INF/lib/test-component-a-0.1.jar" );
        verifier.assertFilePresent(
            "test-component-c/target/test-component-c-0.1.war!/WEB-INF/lib/test-component-b-0.1.jar" );
        verifier.assertFilePresent( "test-component-c/target/my-test" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

