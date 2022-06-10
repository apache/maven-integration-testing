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

import org.apache.maven.it.util.ResourceExtractor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-3710">MNG-3710</a>.
 *
 * todo Fill in a better description of what this test verifies!
 *
 * @author <a href="mailto:brianf@apache.org">Brian Fox</a>
 * @author jdcasey
 *
 */
public class MavenITmng3710PollutedClonedPluginsTest
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng3710PollutedClonedPluginsTest()
    {
        super( "(2.0.8,)" ); // only test in 2.0.9+
    }

    public void testitMNG3710_POMInheritance()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-3710/pom-inheritance" );
        File pluginDir = new File( testDir, "maven-mng3710-pomInheritance-plugin" );
        File projectsDir = new File( testDir, "projects" );

        Verifier verifier;

        verifier = newVerifier( pluginDir.getAbsolutePath(), "remote" );
        verifier.executeGoal( "install" );

        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        verifier = newVerifier( projectsDir.getAbsolutePath() );
        verifier.executeGoal( "validate" );

        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        File topLevelTouchFile = new File( projectsDir, "target/touch.txt" );
        assertFalse( "Top-level touch file should NOT be created in projects tree.", topLevelTouchFile.exists() );

        File midLevelTouchFile = new File( projectsDir, "middle/target/touch.txt" );
        assertTrue( "Mid-level touch file should have been created in projects tree.", midLevelTouchFile.exists() );

        File childLevelTouchFile = new File( projectsDir, "middle/child/target/touch.txt" );
        assertTrue( "Child-level touch file should have been created in projects tree.", childLevelTouchFile.exists() );

    }

    public void testitMNG3710_OriginalModel()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-3710/original-model" );
        File pluginsDir = new File( testDir, "plugins" );
        File projectDir = new File( testDir, "project" );

        Verifier verifier;

        verifier = newVerifier( pluginsDir.getAbsolutePath(), "remote" );
        verifier.executeGoal( "install" );

        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        verifier = newVerifier( projectDir.getAbsolutePath() );

        List<String> goals = new ArrayList<>();
        goals.add( "org.apache.maven.its.mng3710:mavenit-mng3710-directInvoke-plugin:1:run" );
        goals.add( "validate" );

        verifier.executeGoals( goals );

        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}
