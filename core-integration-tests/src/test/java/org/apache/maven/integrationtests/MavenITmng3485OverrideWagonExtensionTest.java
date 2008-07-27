package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.IntegrationTestRunner;

public class MavenITmng3485OverrideWagonExtensionTest
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng3485OverrideWagonExtensionTest()
        throws InvalidVersionSpecificationException
    {
        super( "(2.0.8, 2.1-SNAPSHOT)" ); // only test in 2.0.9+
    }

    public void testitMNG3485 ()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/mng-3485-overrideWagonExtension" );
        IntegrationTestRunner verifier;
        verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        List cliOptions = new ArrayList();
        verifier.executeGoal( "deploy", cliOptions );
        verifier.assertFilePresent( "target/wagon-data" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}
