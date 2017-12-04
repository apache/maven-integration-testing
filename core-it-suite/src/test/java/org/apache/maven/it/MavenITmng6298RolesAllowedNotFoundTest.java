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
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-6298">MNG-6298</a>.
 * Most of code reused from mng-6084. Bugfix came after release, old test case still valid for its release.
 */
public class MavenITmng6298RolesAllowedNotFoundTest
    extends AbstractMavenIntegrationTestCase
{

    private File testDir;

    public MavenITmng6298RolesAllowedNotFoundTest()
    {
        super( "[3.5.3,)" );
    }

    public void setUp()
        throws Exception
    {
        super.setUp();

        testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-6298-rolesallowed-not-found" );

    }

    protected void tearDown()
        throws Exception
    {

        super.tearDown();
    }

    public void testRolesAllowedShouldNotResultInClassCastException()
        throws Exception
    {
        //
        // Build a plugin that uses JSR 250 annotations
        //
        Verifier v0 = newVerifier( testDir.getAbsolutePath(), "remote" );
        v0.setAutoclean( false );
        v0.deleteDirectory( "target" );
        v0.deleteArtifacts( "org.apache.maven.its.mng6298" );
        v0.executeGoal( "install" );
        v0.verifyErrorFreeLog();
        v0.resetStreams();

        //
        // Execute the JSR 250 plugin
        //
        Verifier v1 = newVerifier( testDir.getAbsolutePath(), "remote" );
        v1.setAutoclean( false );
        v1.executeGoal( "org.apache.maven.its.mng6298:jsr250-maven-plugin:0.0.1-SNAPSHOT:hello" );
        v1.verifyErrorFreeLog();
        v1.resetStreams();
        v1.verifyTextInLog( "Hello! I am a component using JSR 250 with @PostConstruct support" );

    }

}
