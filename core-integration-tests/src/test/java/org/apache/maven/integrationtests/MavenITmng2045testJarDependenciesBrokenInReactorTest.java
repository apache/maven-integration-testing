package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.IntegrationTestRunner;

/**
 * Simple IT test invoking maven in a reactor with 2 projects.
 * First project produced a test-jar, which is required to 
 * compile second project. 
 *
 * @author <a href="mailto:brianf@apache.org">Brian Fox</a>
 * @author mikko.koponen@ri.fi
 */
public class MavenITmng2045testJarDependenciesBrokenInReactorTest extends AbstractMavenIntegrationTestCase {

    public MavenITmng2045testJarDependenciesBrokenInReactorTest()
        throws InvalidVersionSpecificationException
    {
        super( "(2.0.7,)" ); // 2.0.8+
    }

    public void testitMNG2045()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/mng-2045-testJarDependenciesBrokenInReactor" );
        IntegrationTestRunner itr;

        itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( "testing", "mng-2045-test", "1.0-SNAPSHOT", "pom" );
        itr.deleteArtifact( "testing", "first-project", "1.0-SNAPSHOT", "jar" );
        itr.deleteArtifact( "testing", "second-project", "1.0-SNAPSHOT", "jar" );

        itr.invoke( "install" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}
