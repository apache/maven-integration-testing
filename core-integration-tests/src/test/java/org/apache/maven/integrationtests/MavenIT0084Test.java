package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0084Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Verify that the collector selecting a particular version gets the correct subtree
     */
    public void testit0084()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0084" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "package" );
        itr.assertFilePresent( "test-component-a/target/test-component-a-0.1.jar" );
        itr.assertFilePresent( "test-component-b/target/test-component-b-0.1.jar" );
        itr.assertFilePresent( "test-component-c/target/test-component-c-0.1.war" );
        itr.assertFilePresent(
            "test-component-c/target/test-component-c-0.1.war!/WEB-INF/lib/test-component-a-0.1.jar" );
        itr.assertFilePresent(
            "test-component-c/target/test-component-c-0.1.war!/WEB-INF/lib/test-component-b-0.1.jar" );
        itr.assertFilePresent(
            "test-component-c/target/test-component-c-0.1.war!/WEB-INF/lib/maven-core-it-support-1.4.jar" );
        itr.assertFilePresent(
            "test-component-c/target/test-component-c-0.1.war!/WEB-INF/lib/commons-io-1.0.jar" );
        itr.assertFilePresent(
            "test-component-c/target/test-component-c-0.1/WEB-INF/lib/test-component-a-0.1.jar" );
        itr.assertFilePresent(
            "test-component-c/target/test-component-c-0.1/WEB-INF/lib/test-component-b-0.1.jar" );
        itr.assertFilePresent(
            "test-component-c/target/test-component-c-0.1/WEB-INF/lib/maven-core-it-support-1.4.jar" );
        itr.assertFilePresent( "test-component-c/target/test-component-c-0.1/WEB-INF/lib/commons-io-1.0.jar" );
        itr.assertFileNotPresent(
            "test-component-c/target/test-component-c-0.1/WEB-INF/lib/commons-lang-1.0.jar" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

