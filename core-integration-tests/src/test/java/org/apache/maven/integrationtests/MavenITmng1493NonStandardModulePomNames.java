package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.IntegrationTestRunner;

public class MavenITmng1493NonStandardModulePomNames
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng1493NonStandardModulePomNames()
        throws InvalidVersionSpecificationException
    {
        super( "[2.1-SNAPSHOT,)" ); // 2.1+
    }

    public void testitMNG1493 ()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/mng-1493-nonstandardModulePomNames" );

        IntegrationTestRunner itr;

        itr = new IntegrationTestRunner( testDir.getAbsolutePath() );

        itr.invoke( "initialize" );

        /*
         * This is the simplest way to check a build
         * succeeded. It is also the simplest way to create
         * an IT test: make the build pass when the test
         * should pass, and make the build fail when the
         * test should fail. There are other methods
         * supported by the itr. They can be seen here:
         * http://maven.apache.org/shared/maven-itr/apidocs/index.html
         */
        itr.verifyErrorFreeLog();
    }
}
