package org.apache.maven.integrationtests;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0001Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Builds upon it0000: we add an application resource that is packaged
     * up in the resultant JAR.
     */
    public void testit0001()
        throws Exception
    {
        IntegrationTestRunner itr = createTestRunner();
        itr.invoke( "package" );        
        itr.assertFilePresent( "target/classes/org/apache/maven/it0001/Person.class" );
        itr.assertFilePresent( "target/test-classes/org/apache/maven/it0001/PersonTest.class" );
        itr.assertFilePresent( "target/maven-it-it0001-1.0.jar" );
        itr.assertFilePresent( "target/maven-it-it0001-1.0.jar!/it0001.properties" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }
}

