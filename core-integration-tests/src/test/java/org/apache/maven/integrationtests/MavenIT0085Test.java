package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0085Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Verify that system-scoped dependencies get resolved with system scope
     * when they are resolved transitively via another (non-system)
     * dependency. Inherited scope should not apply in the case of
     * system-scoped dependencies, no matter where they are.
     */
    public void testit0085()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0085" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.invoke( "package" );
        itr.assertFileNotPresent( "war/target/war-1.0/WEB-INF/lib/pom.xml" );
        itr.assertFileNotPresent( "war/target/war-1.0/WEB-INF/lib/it0085-dep-1.0.jar" );
        itr.assertFilePresent( "war/target/war-1.0/WEB-INF/lib/junit-3.8.1.jar" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

