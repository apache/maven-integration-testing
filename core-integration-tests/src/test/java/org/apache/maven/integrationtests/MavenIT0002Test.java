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
        IntegrationTestRunner verifier = createTestRunner( "it002" );
        verifier.deleteArtifact( "org.apache.maven.its", "maven-core-it-support", "1.0", "jar" );
        verifier.invoke( createInvocationRequest( "package" ) );        
        verifier.assertFilePresent( "target/classes/org/apache/maven/it0002/Person.class" );
        verifier.assertFilePresent( "target/test-classes/org/apache/maven/it0002/PersonTest.class" );
        verifier.assertFilePresent( "target/maven-it-it0002-1.0.jar" );
        verifier.assertFilePresent( "target/maven-it-it0002-1.0.jar!/it0002.properties" );
        verifier.assertArtifactPresent( "org.apache.maven.its", "maven-core-it-support", "1.0", "jar" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}

