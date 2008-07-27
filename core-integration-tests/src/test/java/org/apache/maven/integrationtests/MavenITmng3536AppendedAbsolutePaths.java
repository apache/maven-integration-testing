package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.IntegrationTestRunner;

public class MavenITmng3536AppendedAbsolutePaths extends AbstractMavenIntegrationTestCase {
	
	public MavenITmng3536AppendedAbsolutePaths()
		throws InvalidVersionSpecificationException
	{
		super( "(2.0.9,)");
	}

    public void testitMNG3536() throws Exception {
        File testDir = extractTestResources( getClass(),
                                                                 "/mng-3536-appendedAbsolutePaths" );
        File pluginDir = new File( testDir, "plugin" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( pluginDir.getAbsolutePath() );

        verifier.executeGoal( "install" );

        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        File projectDir = new File( testDir, "project" );
        verifier = new IntegrationTestRunner( projectDir.getAbsolutePath() );

        verifier.executeGoal( "verify" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}
