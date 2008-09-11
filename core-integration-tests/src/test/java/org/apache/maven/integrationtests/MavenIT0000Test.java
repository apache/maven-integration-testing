package org.apache.maven.integrationtests;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0000Test
    extends AbstractMavenIntegrationTestCase
{
    /**
     * The simplest of builds. We have one application class and one test
     * class. There are no resources, no source generation, no resource
     * generation and a the super model is employed to provide the build
     * information.
     */
    public void testit0000()
        throws Exception
    {
        IntegrationTestRunner itr = createTestRunner();
        itr.invoke( "package" );
        itr.assertFilePresent( "target/classes/org/apache/maven/it0000/Person.class" );
        itr.assertFilePresent( "target/test-classes/org/apache/maven/it0000/PersonTest.class" );
        itr.assertFilePresent( "target/maven-it-it0000-1.0.jar" );
        itr.assertFilePresent( "target/surefire-reports/org.apache.maven.it0000.PersonTest.txt" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }
}

