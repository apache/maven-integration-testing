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
import java.util.Properties;

public class MavenIT0113ServerAuthzAvailableToWagonMgrInPluginTest
    extends AbstractMavenIntegrationTestCase
{
    public MavenIT0113ServerAuthzAvailableToWagonMgrInPluginTest()
    {
        super( "[2.0,3.0-alpha-1),[3.0-alpha-7,)" );
    }

    /**
     * Test that the auth infos given in the settings.xml are pushed into the wagon manager and are available
     * to other components/plugins.
     *
     * @throws Exception in case of failure
     */
    public void testit0113()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/it0113" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.addCliOption( "--settings" );
        verifier.addCliOption( "settings.xml" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        Properties props = verifier.loadProperties( "target/auth.properties" );
        assertEquals( "testuser", props.getProperty( "test.username" ) );
        assertEquals( "testtest", props.getProperty( "test.password" ) );
    }

}
