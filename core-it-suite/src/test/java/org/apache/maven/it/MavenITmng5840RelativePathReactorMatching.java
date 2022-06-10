package org.apache.maven.it;

import org.apache.maven.it.util.ResourceExtractor;

import java.io.File;

public class MavenITmng5840RelativePathReactorMatching
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng5840RelativePathReactorMatching()
    {
        super( ALL_MAVEN_VERSIONS );
    }

    public void testRelativePathPointsToWrongVersion()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-5840-relative-path-reactor-matching" );

        Verifier verifier = newVerifier( new File( testDir, "parent-1" ).getAbsolutePath(), "remote" );
        verifier.executeGoal( "install" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        verifier = newVerifier( new File( testDir, "child" ).getAbsolutePath(), "remote" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}
