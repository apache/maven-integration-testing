package org.apache.maven.integrationtests;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0003Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Builds upon it0001: we add a jar installation step. We delete the JAR
     * from the local repository to make sure it is there post build.
     */
    public void testit0003()
        throws Exception
    {
        IntegrationTestRunner itr = createTestRunner();
        itr.deleteArtifact( "org.apache.maven", "maven-it-it0003", "1.0", "jar" );
        itr.invoke( "install" );        
        itr.assertFilePresent( "target/classes/org/apache/maven/it0003/Person.class" );
        itr.assertFilePresent( "target/test-classes/org/apache/maven/it0003/PersonTest.class" );
        itr.assertFilePresent( "target/maven-it-it0003-1.0.jar" );
        itr.assertFilePresent( "target/maven-it-it0003-1.0.jar!/it0003.properties" );
        itr.assertArtifactPresent( "org.apache.maven.its.it0003", "maven-it-it0003", "1.0", "jar" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }
}

