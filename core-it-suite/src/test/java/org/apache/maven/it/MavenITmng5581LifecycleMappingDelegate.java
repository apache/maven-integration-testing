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
import java.util.List;

import org.apache.maven.it.util.ResourceExtractor;

public class MavenITmng5581LifecycleMappingDelegate
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng5581LifecycleMappingDelegate()
    {
        super( "[3.2.1,)" );
    }

    public void testCustomLifecycle()
        throws Exception
    {
        /*
         * This test comes in two parts, a build extension project that defines custom lifecycle with corresponding
         * lifecycle mapping delegate, and a test project used to validate the custom lifecycle. The custom lifecycle id
         * is "test-only", it has single build phase "test-only" and lifecycle mapping delegate that picks default
         * surefire-plugin execution out of all mojos configured in project pom.xml. The test asserts it is possible to
         * run "test-only" build phase and that it does not run maven-compiler-plugin.
         */

        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-5581-lifecycle-mapping-delegate" );
        File extensionDir = new File( testDir, "extension" );
        File projectDir = new File( testDir, "basic" );

        Verifier verifier;

        // install the test extension
        verifier = newVerifier( extensionDir.getAbsolutePath(), "remote" );
        verifier.executeGoal( "install" );
        verifier.resetStreams();
        verifier.verifyErrorFreeLog();

        // compile the test project
        verifier = newVerifier( projectDir.getAbsolutePath(), "remote" );
        verifier.executeGoal( "compile" );
        verifier.resetStreams();
        verifier.verifyErrorFreeLog();

        // run custom "test-only" build phase
        verifier.setForkJvm( true );
        verifier.setMavenDebug( true );
        verifier.executeGoal( "test-only" );
        verifier.resetStreams();
        verifier.verifyErrorFreeLog();
        verifier.verifyTextInLog( "maven-surefire-plugin" );
        verifyTextNotInLog( verifier, "maven-compiler-plugin" );
    }

    private void verifyTextNotInLog( Verifier verifier, String text )
        throws VerificationException
    {
        List<String> lines = verifier.loadFile( verifier.getBasedir(), verifier.getLogFileName(), false );

        boolean textFound = false;
        for ( String line : lines )
        {
            if ( line.contains( text ) )
            {
                textFound = true;
                break;
            }
        }
        if ( textFound )
        {
            throw new VerificationException( "Text found in log: " + text );
        }
    }

}
