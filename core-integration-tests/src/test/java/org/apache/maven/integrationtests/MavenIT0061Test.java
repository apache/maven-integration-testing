package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0061Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Verify that deployment of artifacts to a legacy-layout repository
     * results in a groupId directory of 'the.full.group.id' instead of
     * 'the/full/group/id'.
     */
    public void testit0061()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0061" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "deploy" );
        itr.assertFilePresent( "target/test-repo/org.apache.maven.its.it0061/jars/maven-it-it0061-1.0.jar" );
        itr.assertFilePresent( "target/test-repo/org.apache.maven.its.it0061/poms/maven-it-it0061-1.0.pom" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

