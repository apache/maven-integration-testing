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
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "compile" );
        verifier.assertFilePresent( "target/classes/org/apache/maven/it0011/PersonFinder.class" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

