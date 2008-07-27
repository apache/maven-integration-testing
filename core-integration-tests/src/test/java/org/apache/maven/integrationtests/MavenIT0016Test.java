package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0016Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test a WAR generation
     */
    public void testit0016()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0016" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "package" );
        verifier.assertFilePresent( "target/classes/org/apache/maven/it0016/Person.class" );
        verifier.assertFilePresent( "target/maven-it-it0016-1.0/index.html" );
        verifier.assertFilePresent( "target/maven-it-it0016-1.0/WEB-INF/classes/org/apache/maven/it0016/Person.class" );
        verifier.assertFilePresent( "target/maven-it-it0016-1.0/WEB-INF/lib/commons-logging-1.0.3.jar" );
        verifier.assertFileNotPresent( "target/maven-it-it0016-1.0/WEB-INF/lib/servletapi-2.4-20040521.jar" );
        verifier.assertFilePresent( "target/maven-it-it0016-1.0.war" );
        verifier.assertFilePresent( "target/maven-it-it0016-1.0.war!/index.html" );
        verifier.assertFilePresent(
            "target/maven-it-it0016-1.0.war!/WEB-INF/classes/org/apache/maven/it0016/Person.class" );
        verifier.assertFilePresent( "target/maven-it-it0016-1.0.war!/WEB-INF/lib/commons-logging-1.0.3.jar" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

