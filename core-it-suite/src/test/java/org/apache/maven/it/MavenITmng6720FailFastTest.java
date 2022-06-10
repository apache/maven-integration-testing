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
import java.util.Arrays;
import java.util.List;

/**
 * An integration test to check that concurrently running projects are finished
 * in --fail-fast mode, while downstream projects are skipped.
 *
 * <a href="https://issues.apache.org/jira/browse/MNG-6720">MNG-6720</a>.
 *
 */
public class MavenITmng6720FailFastTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng6720FailFastTest()
    {
        super( "[3.6.2,)" );
    }

    public void testItShouldWaitForConcurrentModulesToFinish()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-6720-fail-fast" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath(), false );
        verifier.setMavenDebug( false );
        verifier.setAutoclean( false );
        verifier.addCliOption( "-T" );
        verifier.addCliOption( "2" );
        verifier.addCliOption( "-Dmaven.test.redirectTestOutputToFile=true" );

        try
        {
            verifier.executeGoals( Arrays.asList( "clean", "test" ) );
        } catch (VerificationException e)
        {
            //expected
        }
        verifier.resetStreams();

        List<String> module1Lines = verifier.loadFile(
            new File( testDir, "module-1/target/surefire-reports/Module1Test-output.txt" ), false );
        assertTrue( module1Lines.contains( "Module1" ) );
        List<String> module2Lines = verifier.loadFile(
            new File( testDir, "module-2/target/surefire-reports/Module2Test-output.txt" ), false );
        assertTrue(module2Lines.contains( "Module2" ) );
        List<String> module3Lines = verifier.loadFile(
            new File( testDir, "module-3/target/surefire-reports/Module3Test-output.txt" ), false );
        assertTrue( module3Lines.isEmpty() );


    }
}
