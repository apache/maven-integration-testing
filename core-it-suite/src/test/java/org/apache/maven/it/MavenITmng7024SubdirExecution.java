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
import org.apache.maven.shared.utils.io.FileUtils;

import java.io.File;

/**
 * This is a test case for <a href="https://issues.apache.org/jira/browse/MNG-7024">MNG-7024</a>.
 */
public class MavenITmng7024SubdirExecution extends AbstractMavenIntegrationTestCase {
    public MavenITmng7024SubdirExecution()
    {
        super( "[4.0.0-alpha-1,)" );
    }

    /**
     * Executing from a subdirectory, however this folder is NOT part of the rootreactor, hence should not try to bind
     */
    public void testShouldIgnoreModuleA() throws Exception
    {
        final File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-7024-subfolderexecution" );

        final Verifier verifier1 = newVerifier( testDir.getAbsolutePath() );

        try
        {
            verifier1.executeGoal( "install" );

            verifier1.deleteDirectory( "target" );
            FileUtils.delete( new File( testDir, "module-a/target/module-a-1.0.jar" )  );
        }
        finally
        {
            verifier1.resetStreams();
        }
        verifier1.assertFilePresent( "module-a/target/classes" );
        verifier1.assertFileNotPresent( "module-a/target/module-a-1.0.jar" );

        final Verifier verifier2 = newVerifier( new File( testDir, "module-b" ).getAbsolutePath() );
        verifier2.setAutoclean( false );
        verifier2.addCliOption( "-fos" );
        verifier2.addCliOption( "WARN" );
        verifier2.executeGoal( "package" );

        verifier2.verifyErrorFreeLog();
        verifier2.resetStreams();
    }

}
