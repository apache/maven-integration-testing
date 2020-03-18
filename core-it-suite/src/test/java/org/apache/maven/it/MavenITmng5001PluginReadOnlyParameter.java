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

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-5001">MNG-5001</a>:
 * fail build if a configuring a read-only plugin parameter.
 */
public class MavenITmng5001PluginReadOnlyParameter
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng5001PluginReadOnlyParameter()
    {
        super( "[2.0.5,3.0-alpha-1),[3.7.0,)" );
    }

    /**
     * Verify that a project configuring a read-only plugin parameter just fails at build time.
     */
    public void testit()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-5001_readonly" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        try
        {
            verifier.executeGoal( "validate" );
            throw new Exception( "configuring a plugin's read-only parameter should fail" );
        }
        catch ( VerificationException ve )
        {
            // expected failure
        }
        verifier.resetStreams();
    }

}
