package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.IntegrationTestRunner;

public class MavenITmng3475BaseAlignedDir
    extends AbstractMavenIntegrationTestCase
{
	
	public MavenITmng3475BaseAlignedDir()
		throws InvalidVersionSpecificationException
	{
		super( "(2.0.9,)");
	}

    public void testitMNG3475()
        throws Exception
    {
        File testDir = extractTestResources( getClass(),
                                                                 "/mng-3475-baseAlignedDir" );

        File pluginDir = new File( testDir, "plugin" );
        IntegrationTestRunner itr = new IntegrationTestRunner( pluginDir.getAbsolutePath() );

        itr.invoke( "install" );

        itr.verifyErrorFreeLog();
        itr.resetStreams();

        File projectDir = new File( testDir, "project" );
        itr = new IntegrationTestRunner( projectDir.getAbsolutePath() );

        itr.invoke( "validate" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }

}
