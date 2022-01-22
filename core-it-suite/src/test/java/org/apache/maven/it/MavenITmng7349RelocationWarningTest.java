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
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.util.ResourceExtractor;

public class MavenITmng7349RelocationWarningTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng7349RelocationWarningTest()
    {
        super( "[3.8.5,)" );
    }

    public void testit()
            throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(),
                    "/mng-7349-relocation-warning" );
        File artifactsDir = new File( testDir, "artifacts" );
        File projectDir = new File( testDir, "project" );

        Verifier verifier;

        verifier = newVerifier( artifactsDir.getAbsolutePath() );
        verifier.executeGoal( "install" );
        verifier.resetStreams();
        verifier.verifyErrorFreeLog();

        verifier = newVerifier( projectDir.getAbsolutePath() );
        verifier.executeGoal( "verify" );
        verifier.resetStreams();
        verifier.verifyErrorFreeLog();
        List<String> lines = verifier.loadLines( verifier.getLogFileName(), "UTF-8" );
        List<String> relocated = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append(line);
            if (line.contains("has been relocated")) {
                relocated.add(line);
            }
        }
        if (relocated.size() != 2 ) {
            throw new VerificationException("Expected 2 relocations, but found multiple.\nBuild output:\n" + sb);
        }
        if (!sb.toString().contains("The artifact org.apache.maven.its.mng7349:old-dep:jar:1.0.0-SNAPSHOT has been relocated to org.apache.maven.its.mng7349:new-dep:jar:1.0.0-SNAPSHOT: Test relocation reason for old-dep")
                || !sb.toString().contains("The artifact org.apache.maven.its.mng7349:old-plugin:jar:1.0.0-SNAPSHOT has been relocated to org.apache.maven.its.mng7349:new-plugin:jar:1.0.0-SNAPSHOT: Test relocation reason for old-plugin")) {
            throw new VerificationException("Expected the relocation messages to be logged.\nBuild output:\n" + sb);
        }
    }
}
