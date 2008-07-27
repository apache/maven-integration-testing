package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0083Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Verify that overriding a compile time dependency as provided in a WAR ensures it is not included.
     */
    public void testit0083()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0083" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "package" );
        verifier.assertFilePresent( "test-component-a/target/test-component-a-0.1.jar" );
        verifier.assertFilePresent( "test-component-b/target/test-component-b-0.1.jar" );
        verifier.assertFilePresent( "test-component-c/target/test-component-c-0.1.war" );
        verifier.assertFilePresent(
            "test-component-c/target/test-component-c-0.1.war!/WEB-INF/lib/test-component-b-0.1.jar" );
        verifier.assertFileNotPresent(
            "test-component-c/target/test-component-c-0.1/WEB-INF/lib/test-component-a-0.1.jar" );
        verifier.assertFilePresent(
            "test-component-c/target/test-component-c-0.1/WEB-INF/lib/test-component-b-0.1.jar" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

