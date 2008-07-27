package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0024Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test usage of &lt;executions/&gt; inside a plugin rather than &lt;goals/&gt;
     * that are directly inside th plugin.
     */
    public void testit0024()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0024" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "generate-sources" );
        verifier.assertFilePresent( "target/classes/org/apache/maven/it0024/Person.class" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

