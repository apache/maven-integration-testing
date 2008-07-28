package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0080Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test that depending on a WAR doesn't also get its dependencies
     * transitively.
     */
    public void testit0080()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0080" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "package" );
        itr.assertFilePresent( "test-component-a/target/test-component-a-0.1.jar" );
        itr.assertFilePresent( "test-component-b/target/test-component-b-0.1.war" );
        itr.assertFilePresent(
            "test-component-b/target/test-component-b-0.1.war!/WEB-INF/lib/test-component-a-0.1.jar" );
        itr.assertFilePresent( "test-component-c/target/test-component-c-0.1.ear" );
        itr.assertFilePresent( "test-component-c/target/test-component-c-0.1.ear!/test-component-b-0.1.war" );
        itr.assertFilePresent( "test-component-c/target/test-component-c-0.1/test-component-b-0.1.war" );
        itr.assertFileNotPresent( "test-component-c/target/test-component-c-0.1/test-component-a-0.1.jar" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

