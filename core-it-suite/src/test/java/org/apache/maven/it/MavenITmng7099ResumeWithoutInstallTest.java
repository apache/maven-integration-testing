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
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.it.util.ResourceExtractor;

/**
 * This is a collection of test cases for <a href="https://issues.apache.org/jira/browse/MNG-5760">MNG-5760</a>,
 * <code>--resume</code> / <code>-r</code> in case of build failures.
 *
 * The test uses a multi-module project with three modules:
 * <ul>
 *     <li>module-a</li>
 *     <li>module-b</li>
 *     <li>module-c</li> (depends on module-b)
 * </ul>
 *
 * @author Maarten Mulders
 * @author Martin Kanters
 */
public class MavenITmng7099ResumeWithoutInstallTest extends AbstractMavenIntegrationTestCase {
    private final File projectTestDir;

    public MavenITmng7099ResumeWithoutInstallTest() throws IOException {
        super( "[3.6.0,)" );
        this.projectTestDir = ResourceExtractor.simpleExtractResources( getClass(),
                "/mng-7099-resume-without-install" );
    }

    public void testResumeWithoutInstall() throws Exception
    {
        // First pass: try to verify
        Verifier verifier = newVerifier( projectTestDir.getAbsolutePath() );
        verifier.addCliOption( "-Dmodule-b.fail=true" );
        try
        {
            verifier.executeGoal( "verify" );
            fail( "Expected this invocation to fail" );
        }
        catch ( final VerificationException ve )
        {
            verifier.verifyTextInLog( "Building module-a 1.0" );
            verifier.verifyTextInLog( "Building module-b 1.0" );
            verifier.verifyTextInLog( "mvn <args> -rf :module-b" );
        }
        finally
        {
            verifier.resetStreams();
        }

        verifier = newVerifier( projectTestDir.getAbsolutePath() );
        verifier.addCliOption( "-rf :module-b" );
        try
        {
            verifier.executeGoal( "verify" );
        }
        finally
        {
            verifier.resetStreams();
        }
    }

}
