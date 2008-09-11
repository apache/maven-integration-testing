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
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.invoke( "package" );
        itr.assertFilePresent( "test-component-a/target/test-component-a-0.1.jar" );
        itr.assertFilePresent( "test-component-b/target/test-component-b-0.1.jar" );
        itr.assertFilePresent( "test-component-c/target/test-component-c-0.1.war" );
        itr.assertFilePresent(
            "test-component-c/target/test-component-c-0.1.war!/WEB-INF/lib/test-component-b-0.1.jar" );
        itr.assertFileNotPresent(
            "test-component-c/target/test-component-c-0.1/WEB-INF/lib/test-component-a-0.1.jar" );
        itr.assertFilePresent(
            "test-component-c/target/test-component-c-0.1/WEB-INF/lib/test-component-b-0.1.jar" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

