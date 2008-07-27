package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.DefaultInvocationRequest;
import org.apache.maven.it.IntegrationTestRunner;
import org.apache.maven.it.InvocationRequest;

public class MavenITmng3535SelfReferentialProperties
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng3535SelfReferentialProperties()
        throws InvalidVersionSpecificationException
    {
        super( "(2.0.9,)" );
    }

    public void testitMNG3535()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/mng-3535-selfReferentialProperties" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        List opts = new ArrayList();
        opts.add( "-X" );

        InvocationRequest r = new DefaultInvocationRequest()
            .setGoals( "verify" )
            .setCliOptions( opts )
            .setAutoclean( false );

        verifier.invoke( r );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}
