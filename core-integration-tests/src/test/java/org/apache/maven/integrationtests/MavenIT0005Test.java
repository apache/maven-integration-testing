package org.apache.maven.integrationtests;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0005Test
    extends AbstractMavenIntegrationTestCase
{
    /**
     * The simplest of pom installation. We have a snapshot pom and we install
     * it in local repository.
     */
    public void testit0005()
        throws Exception
    {
        IntegrationTestRunner itr = createTestRunner();
        itr.deleteArtifact( "org.apache.maven", "maven-it-it0005", "1.0-SNAPSHOT", "pom" );
        itr.invoke( "install:install" );        
        itr.assertArtifactPresent( "org.apache.maven.its.it0005", "maven-it-it0005", "1.0-SNAPSHOT", "pom" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }
}

