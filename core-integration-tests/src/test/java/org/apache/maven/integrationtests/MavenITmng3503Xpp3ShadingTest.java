/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.IntegrationTestRunner;

/**
 * This is a test set for <a href="http://jira.codehaus.org/browse/MNG-xxxx">MNG-xxxx</a>.
 *
 * @todo Fill in a better description of what this test verifies!
 * 
 * @author <a href="mailto:brianf@apache.org">Brian Fox</a>
 * @author jdcasey
 * 
 */
public class MavenITmng3503Xpp3ShadingTest
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng3503Xpp3ShadingTest()
        throws InvalidVersionSpecificationException
    {
        super( "(2.0.9,)" ); // only test in 2.0.9+
    }

    public void testitMNG3503 ()
        throws Exception
    {
        // The testdir is computed from the location of this
        // file.
        File testDir = extractTestResources( getClass(), "/mng-3503-xpp3Shading" );

        IntegrationTestRunner itr;

        /*
         * We must first make sure that any artifact created
         * by this test has been removed from the local
         * repository. Failing to do this could cause
         * unstable test results. Fortunately, the itr
         * makes it easy to do this.
         */
        itr = new IntegrationTestRunner( testDir.getAbsolutePath() );

        itr.executeGoal( "install" );

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
