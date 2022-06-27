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
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.it.util.ResourceExtractor;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-7474">MNG-7474</a>:
 * check that Session scope beans are actually singletons for the session.
 */
public class MavenITmng7474SessionScopeTest
        extends AbstractMavenIntegrationTestCase
{
    private File projectDir;

    public MavenITmng7474SessionScopeTest()
    {
        super( "[3.9.0,)" );
    }

    @Override
    protected void setUp()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-7474-session-scope" );
        projectDir = new File( testDir, "project" );
    }

    public void testSessionScope()
            throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-7474-session-scope" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        List<String> logLines = verifier.loadFile( verifier.getBasedir(), verifier.getLogFileName(), false );
    }

}
