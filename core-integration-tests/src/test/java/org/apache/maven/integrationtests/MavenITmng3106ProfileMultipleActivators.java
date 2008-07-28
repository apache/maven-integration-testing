package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.IntegrationTestRunner;

/**
 * Test that profiles with multiple activators are activated 
 * when any of the activators are on.
 * 
 */
public class MavenITmng3106ProfileMultipleActivators
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng3106ProfileMultipleActivators()
        throws InvalidVersionSpecificationException
    {
        super( "(2.0.9,)" );
    }

    /**
     * Test build with two profiles, each with more than one activator.
     * The profiles should be activated even though only one of the activators 
     * returns true.
     * 
     */
    public void testProfilesWithMultipleActivators()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/mng-3106-ProfileMultipleActivators" );

        IntegrationTestRunner itr;

        itr = new IntegrationTestRunner( testDir.getAbsolutePath() );

        List cliOptions = new ArrayList();
        cliOptions.add( "-Dprofile1.on=true" );

        itr.executeGoal( "package", cliOptions );
        itr.verifyErrorFreeLog();
        itr.assertFilePresent( "target/profile1/touch.txt" );
        itr.assertFilePresent( "target/profile2/touch.txt" );
        itr.resetStreams();

    }

}
