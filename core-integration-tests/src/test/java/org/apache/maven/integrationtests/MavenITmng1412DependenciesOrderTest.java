package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.IntegrationTestRunner;

/**
 * Test that dependencies order in classpath matches pom.xml.
 *
 * @author <a href="mailto:hboutemy@apache.org">Herve Boutemy</a>
 *
 */
public class MavenITmng1412DependenciesOrderTest
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng1412DependenciesOrderTest()
        throws InvalidVersionSpecificationException
    {
        super( "(2.0.8,)" ); // 2.0.9+
    }

    public void testitMNG1412()
        throws Exception
    {
        // The testdir is computed from the location of this file.
        File testDir = extractTestResources( getClass(), "/mng-1412-DependenciesOrder" );

        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );

        List cliOptions = new ArrayList();
        cliOptions.add( "-X" );
        verifier.executeGoal( "test", cliOptions );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}
