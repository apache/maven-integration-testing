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
 * This is a collection of test cases for <a href="https://issues.apache.org/jira/browse/MNG-6511">MNG-6511</a>,
 * selecting and deselecting optional projects.
 *
 * @author Maarten Mulders
 * @author Martin Kanters
 */
public class MavenITmng6511OptionalProjectSelectionTest extends AbstractMavenIntegrationTestCase
{
    private static final String RESOURCE_PATH = "/mng-6511-optional-project-selection";
    private final File testDir;

    public MavenITmng6511OptionalProjectSelectionTest() throws IOException
    {
        super( "[4.0.0-alpha-1,)" );
        testDir = ResourceExtractor.simpleExtractResources( getClass(), RESOURCE_PATH );
    }

    public void testSelectExistingOptionalProfile() throws VerificationException
    {
        newVerifier( testDir.getAbsolutePath() ).executeGoal( "clean" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setLogFileName( "log-select-existing.txt" );
        verifier.addCliOption( "-pl ?existing-module" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.assertFilePresent( "existing-module/target/touch.txt" ); // existing-module should have been built.
    }

    public void testSelectExistingOptionalProfileByArtifactId() throws VerificationException
    {
        newVerifier( testDir.getAbsolutePath() ).executeGoal( "clean" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setLogFileName( "log-select-existing-artifact-id.txt" );
        verifier.addCliOption( "-pl ?:existing-module" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.assertFilePresent( "existing-module/target/touch.txt" ); // existing-module should have been built.
    }

    public void testSelectNonExistingOptionalProfile() throws VerificationException
    {
        newVerifier( testDir.getAbsolutePath() ).executeGoal( "clean" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setLogFileName( "log-select-non-existing.txt" );
        verifier.addCliOption( "-pl ?non-existing-module" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.assertFilePresent( "existing-module/target/touch.txt" ); // existing-module should have been built.
    }

    public void testDeselectExistingOptionalProfile() throws VerificationException
    {
        newVerifier( testDir.getAbsolutePath() ).executeGoal( "clean" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setLogFileName( "log-deselect-existing.txt" );
        verifier.addCliOption( "-pl !?existing-module" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.assertFileNotPresent( "existing-module/target/touch.txt" ); // existing-module should not have been built.
    }

    public void testDeselectNonExistingOptionalProfile() throws VerificationException
    {
        newVerifier( testDir.getAbsolutePath() ).executeGoal( "clean" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setLogFileName( "log-deselect-non-existing.txt" );
        verifier.addCliOption( "-pl !?non-existing-module" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.assertFilePresent( "existing-module/target/touch.txt" ); // existing-module should have been built.
    }
}
