package org.apache.maven.integrationtests;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0002Test
    extends AbstractMavenIntegrationTestCase
{
    /**
     * Builds upon it0001: we add the download of a dependency. We delete
     * the JAR from the local repository and make sure it is there post build.
     */
    public void testit0002()
        throws Exception
    {
        IntegrationTestRunner itr = createTestRunner();
        itr.deleteArtifact( "org.apache.maven.its", "maven-core-it-support", "1.0", "jar" );
        itr.invoke( "package" );        
        itr.assertFilePresent( "target/classes/org/apache/maven/it0002/Person.class" );
        itr.assertFilePresent( "target/test-classes/org/apache/maven/it0002/PersonTest.class" );
        itr.assertFilePresent( "target/maven-it-it0002-1.0.jar" );
        itr.assertFilePresent( "target/maven-it-it0002-1.0.jar!/it0002.properties" );
        itr.assertArtifactPresent( "org.apache.maven.its", "maven-core-it-support", "1.0", "jar" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }
}

