package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0028Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test that unused configuration parameters from the POM don't cause the
     * mojo to fail...they will show up as warnings in the -X output instead.
     */
    public void testit0028()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0028" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "test" );
        itr.assertFilePresent( "target/classes/org/apache/maven/it0001/Person.class" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

