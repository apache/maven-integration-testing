package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.IntegrationTestRunner;

/**
 * Tests that artifact checksums are properly verified.
 */
public class MavenITmng2744checksumVerificationTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng2744checksumVerificationTest()
        throws InvalidVersionSpecificationException
    {
        super( "(2.0.8,)" ); // only test in 2.0.9+
    }

    /**
     * Tests that hex digits are compared without regard to case.
     */
    public void testitMNG2744()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/mng-2744-checksumVerification" );

        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );

        itr.deleteArtifact( "org.apache.maven.its.mng2744", "a", "1", "pom" );
        itr.deleteArtifact( "org.apache.maven.its.mng2744", "a", "1", "jar" );
        itr.deleteArtifact( "org.apache.maven.its.mng2744", "b", "1", "pom" );
        itr.deleteArtifact( "org.apache.maven.its.mng2744", "b", "1", "jar" );

        itr.invoke( "compile" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }

}
