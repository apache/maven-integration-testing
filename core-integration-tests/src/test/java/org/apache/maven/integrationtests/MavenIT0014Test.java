package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0014Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test POM configuration by settings the -source and -target for the
     * compiler to 1.4
     */
    public void testit0014()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0014" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "test" );
        itr.assertFilePresent( "target/classes/org/apache/maven/it0014/Person.class" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

