package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0011Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test specification of dependency versions via &lt;dependencyManagement/&gt;.
     */
    public void testit0011()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0011" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "compile" );
        itr.assertFilePresent( "target/classes/org/apache/maven/it0011/PersonFinder.class" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

