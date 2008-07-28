package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestException;
import org.apache.maven.it.IntegrationTestRunner;

public class MavenITmng1491ReactorArtifactIdCollision
    extends AbstractMavenIntegrationTestCase
{
    public void testitMNG1491 ()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/mng-1491-reactorArtifactIdCollision" );

        IntegrationTestRunner itr;

        itr = new IntegrationTestRunner( testDir.getAbsolutePath() );

        try
        {
            itr.executeGoal( "initialize" );

            itr.verifyErrorFreeLog();

            fail( "Build should fail due to duplicate artifactId's in the reactor." );
        }
        catch( IntegrationTestException e )
        {
            // expected.
        }
    }
}
