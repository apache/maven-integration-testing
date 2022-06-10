package org.apache.maven.it;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;

import org.apache.maven.it.util.ResourceExtractor;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-3704">MNG-3704</a>.
 *
 * todo Fill in a better description of what this test verifies!
 *
 * @author <a href="mailto:brianf@apache.org">Brian Fox</a>
 * @author jdcasey
 *
 */
public class MavenITmng3704LifecycleExecutorWrapperTest
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng3704LifecycleExecutorWrapperTest()
    {
        super( "[2.0.9,3.0-alpha-1)" ); // 2.0.9+, lifecycle switching not supported in the same way in 3.0
    }

    public void testitMNG3704 ()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-3704" );
        File pluginDir = new File( testDir, "maven-mng3704-plugin" );
        File projectDir = new File( testDir, "project" );

        Verifier verifier;
        verifier = newVerifier( pluginDir.getAbsolutePath(), "remote" );

        verifier.executeGoal( "install" );

        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        verifier = newVerifier( projectDir.getAbsolutePath(), "remote" );

        verifier.executeGoal( "package" );

        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}
