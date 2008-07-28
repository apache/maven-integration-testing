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
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.executeGoal( "package" );
        itr.assertFilePresent( "target/classes/org/apache/maven/it0017/Person.class" );
        itr.assertFilePresent( "target/maven-it-it0017-1.0.jar" );
        itr.assertFilePresent( "target/maven-it-it0017-1.0.jar!/META-INF/ejb-jar.xml" );
        itr.assertFilePresent( "target/maven-it-it0017-1.0.jar!/org/apache/maven/it0017/Person.class" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

    }
}

