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
import java.util.Arrays;

import org.apache.maven.it.util.ResourceExtractor;

/**
 * Warn about unknown parameters
 */
public class MavenITmng5563UnknownParameters
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng5563UnknownParameters()
    {
        super( "[4.0.0-alpha-1,)" );
    }

    /**
     * One executionblock may contain multiple goals.
     * Ensure that the parameters exist in at least one of the goals 
     * 
     * @throws Exception
     */
    public void testPluginExecutionConfiguration()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-5563-unknown-parameters/execution" );
        Verifier verifier = newVerifier( testDir.getAbsolutePath() );

        verifier.setMavenDebug( false );
        verifier.setAutoclean( false );
        verifier.executeGoals( Arrays.asList( "compile" ) );
        verifier.verifyTextInLog( "Unknown parameters for org.apache.maven.plugins:maven-compiler-plugin:0.1-stub-SNAPSHOT (default-compile):" );
        verifier.verifyTextInLog( "  <unknown> @ org.apache.maven.its.mng5563:mng5563-execution:0.0.1-SNAPSHOT, line 40" );
        verifier.resetStreams();
    }
}
