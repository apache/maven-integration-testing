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
        IntegrationTestRunner itr = new IntegrationTestRunner( pluginDir.getAbsolutePath() );

        itr.executeGoal( "install" );

        itr.verifyErrorFreeLog();
        itr.resetStreams();

        File projectDir = new File( testDir, "project" );
        itr = new IntegrationTestRunner( projectDir.getAbsolutePath() );

        itr.executeGoal( "verify" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }
}
