package org.apache.maven.it;

import java.io.File;

import org.apache.maven.shared.verifier.Verifier;
import org.apache.maven.shared.verifier.util.ResourceExtractor;
import org.junit.jupiter.api.Test;

public class MavenITmng5805PkgTypeMojoConfiguration
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng5805PkgTypeMojoConfiguration()
    {
        super( "(3.3.3,3.5.0-alpha)" );
    }

    @Test
    public void testPkgTypeMojoConfiguration()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-5805-pkg-type-mojo-configuration" );

        Verifier verifier;

        verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.verifyTextInLog( "CLASS_NAME=org.apache.maven.its.mng5805.TestClass1" );
        verifier.resetStreams();
    }
}
