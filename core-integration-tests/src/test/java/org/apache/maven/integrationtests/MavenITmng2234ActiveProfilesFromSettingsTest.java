package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.IntegrationTestRunner;

public class MavenITmng2234ActiveProfilesFromSettingsTest
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng2234ActiveProfilesFromSettingsTest()
        throws InvalidVersionSpecificationException
    {
        super( "(2.0.8,)" );
    }

    public void testitMNG2234 ()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/mng-2234-activeProfilesFromSettings" );
        IntegrationTestRunner verifier;
        verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        List cliOptions = new ArrayList();
        cliOptions.add( "-s" );
        cliOptions.add( "settings.xml" );
        verifier.executeGoal( "install", cliOptions );
        verifier.verifyErrorFreeLog();
        verifier.assertFilePresent( "target/touch.txt" );
        verifier.resetStreams();
    }
}
