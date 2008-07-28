package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0103Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Verify that multimodule builds where one project references another as
     * a parent can build, even if that parent is not correctly referenced by
     * &lt;relativePath/&gt; and is not in the local repository. [MNG-2196]
     */
    public void testit0103()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0103" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "package" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

