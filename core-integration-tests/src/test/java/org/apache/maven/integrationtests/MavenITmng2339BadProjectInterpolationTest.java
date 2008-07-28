package org.apache.maven.integrationtests;

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
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenITmng2339BadProjectInterpolationTest
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng2339BadProjectInterpolationTest()
        throws org.apache.maven.artifact.versioning.InvalidVersionSpecificationException
    {
        super( "(2.0.8,)" ); // 2.0.9+
    }

    public void testitMNG2339a()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/mng-2339-badProjectInterpolation/a" );
        IntegrationTestRunner itr;
        itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        List cliOptions = new ArrayList();
        cliOptions.add( "-Dversion=foo" );
        itr.executeGoal( "validate", cliOptions );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }

    // test that -Dversion=1.0 is still available for interpolation.
    public void testitMNG2339b()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/mng-2339-badProjectInterpolation/b" );

        IntegrationTestRunner itr;

        itr = new IntegrationTestRunner( testDir.getAbsolutePath() );

        itr.executeGoal( "initialize" );

        assertTrue( "Touchfile using ${project.version} for ${version} does not exist.",
                    new File( testDir, "target/touch-1.txt" ).exists() );

        itr.verifyErrorFreeLog();
        itr.resetStreams();

        File logFile = new File( testDir, "log.txt" );
        logFile.renameTo( new File( testDir, "log-pom-specified.txt" ) );

        itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        List cliOptions = new ArrayList();
        cliOptions.add( "-Dversion=2" );
        itr.executeGoal( "initialize", cliOptions );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

        logFile.renameTo( new File( testDir, "log-cli-specified.txt" ) );

        assertTrue( "Touchfile using CLI-specified ${version} does not exist.",
                    new File( testDir, "target/touch-2.txt" ).exists() );
    }

}
