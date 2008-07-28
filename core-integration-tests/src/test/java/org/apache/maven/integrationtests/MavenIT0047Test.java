package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0047Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test the use case for having a compile time dependency be transitive:
     * when you extend a class you need its dependencies at compile time.
     */
    public void testit0047()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0047" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "compile" );
        itr.assertFilePresent( "target/classes/org/apache/maven/it0047/Person.class" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

