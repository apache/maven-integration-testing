package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0017Test
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Test an EJB generation
     */
    public void testit0017()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/it0017" );
        IntegrationTestRunner verifier = new IntegrationTestRunner( testDir.getAbsolutePath() );
        verifier.executeGoal( "package" );
        verifier.assertFilePresent( "target/classes/org/apache/maven/it0017/Person.class" );
        verifier.assertFilePresent( "target/maven-it-it0017-1.0.jar" );
        verifier.assertFilePresent( "target/maven-it-it0017-1.0.jar!/META-INF/ejb-jar.xml" );
        verifier.assertFilePresent( "target/maven-it-it0017-1.0.jar!/org/apache/maven/it0017/Person.class" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

    }
}

