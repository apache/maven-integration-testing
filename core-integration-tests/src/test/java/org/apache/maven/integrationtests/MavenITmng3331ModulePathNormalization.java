package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.it.IntegrationTestRunner;

/**
 * This is a sample integration test. The IT tests typically
 * operate by having a sample project in the
 * /src/test/resources folder along with a junit test like
 * this one. The junit test uses the itr (which uses
 * the invoker) to invoke a new instance of Maven on the
 * project in the resources folder. It then checks the
 * results. This is a non-trivial example that shows two
 * phases. See more information inline in the code.
 * 
 * @author <a href="mailto:brianf@apache.org">Brian Fox</a>
 * 
 */
public class MavenITmng3331ModulePathNormalization
    extends AbstractMavenIntegrationTestCase
{
    public void testitMNG3331a ()
        throws Exception
    {
        //testMNG3331ModuleWithSpaces
        File testDir = extractTestResources( getClass(), "/mng-3331-modulePathNormalization/with-spaces" );

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

        /*
         * Reset the streams before executing the itr
         * again.
         */
        itr.resetStreams();
    }

    public void testitMNG3331b ()
        throws Exception
    {
        //testMNG3331ModuleWithRelativeParentDirRef
        File testDir = extractTestResources( getClass(), "/mng-3331-modulePathNormalization/with-relative-parentDir-ref" );

        IntegrationTestRunner itr;

        itr = new IntegrationTestRunner( new File( testDir, "parent" ).getAbsolutePath() );

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

        /*
         * Reset the streams before executing the itr
         * again.
         */
        itr.resetStreams();
    }

}
