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
import java.io.IOException;

/**
 * Testing whether a .mvn/maven.config file can relatively reference a .mvn/settings.xml file.
 */
public class MavenITmng6762MultiModuleRelativeSettings extends AbstractMavenIntegrationTestCase
{
    private static final String SETTINGS_RESOURCE_PATH = "/mng-6762-multi-module-relative-settings";
    private final File testDir;

    public MavenITmng6762MultiModuleRelativeSettings() throws IOException
    {
        super( "[4.0.0-alpha-1,)" );
        testDir = ResourceExtractor.simpleExtractResources( getClass(), SETTINGS_RESOURCE_PATH );
    }

    /**
     * This test verifies whether an invocation from root resolves .mvn/settings.xml correctly.
     */
    public void testInRoot() throws VerificationException
    {
        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setLogFileName( "log-inroot.txt" );

        verifier.executeGoal( "validate" );

        verifier.assertFilePresent( "target/profile-activated.txt" );
    }

    /**
     * This test verifies whether an invocation from a submodule resolves ../.mvn/settings.xml correctly.
     */
    public void testInSubModule() throws VerificationException
    {
        final File submoduleDirectory = new File( testDir, "submodule" );
        Verifier verifier = newVerifier( submoduleDirectory.getAbsolutePath() );
        verifier.setLogFileName( "log-insubmodule.txt" );

        verifier.executeGoal( "validate" );

        verifier.assertFilePresent( "target/profile-activated.txt" );
    }
}
