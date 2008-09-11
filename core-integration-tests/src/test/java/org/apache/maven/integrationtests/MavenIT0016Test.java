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
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.invoke( "package" );
        itr.assertFilePresent( "target/classes/org/apache/maven/it0016/Person.class" );
        itr.assertFilePresent( "target/maven-it-it0016-1.0/index.html" );
        itr.assertFilePresent( "target/maven-it-it0016-1.0/WEB-INF/classes/org/apache/maven/it0016/Person.class" );
        itr.assertFilePresent( "target/maven-it-it0016-1.0/WEB-INF/lib/commons-logging-1.0.3.jar" );
        itr.assertFileNotPresent( "target/maven-it-it0016-1.0/WEB-INF/lib/servletapi-2.4-20040521.jar" );
        itr.assertFilePresent( "target/maven-it-it0016-1.0.war" );
        itr.assertFilePresent( "target/maven-it-it0016-1.0.war!/index.html" );
        itr.assertFilePresent(
            "target/maven-it-it0016-1.0.war!/WEB-INF/classes/org/apache/maven/it0016/Person.class" );
        itr.assertFilePresent( "target/maven-it-it0016-1.0.war!/WEB-INF/lib/commons-logging-1.0.3.jar" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

