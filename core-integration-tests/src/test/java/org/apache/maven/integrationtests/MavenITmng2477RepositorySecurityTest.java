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

package org.apache.maven.integrationtests;

import java.io.File;
import java.io.IOException;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.FileUtils;
import org.apache.maven.it.util.ResourceExtractor;

/**
 * This is a test set for the repository security features.
 */
public class MavenITmng2477RepositorySecurityTest
    extends AbstractMavenIntegrationTestCase
{
    private File mainTestDir;

    public MavenITmng2477RepositorySecurityTest()
        throws InvalidVersionSpecificationException
    {
        super( "(2.0.100,)" ); // only test in 2.1+
    }

    public void setUp()
        throws IOException
    {
        mainTestDir =
            ResourceExtractor.simpleExtractResources( getClass(), "/mng-2477-repository-security" ).getCanonicalFile();
    }

    /* Checksum tests are not yet implemented
    public void testitMNG2477RequiredChecksums()
        throws IOException, VerificationException
    {
        File testDir = new File( mainTestDir, "test-checksum" );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );

        verifier.executeGoal( "compile" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        verifier.assertArtifactPresent( "junit", "junit", "3.8.1", "pom" );
        verifier.assertArtifactPresent( "junit", "junit", "3.8.1", "jar" );

        verifier.assertArtifactPresent( "commons-logging", "commons-logging-api", "1.1", "pom" );
        verifier.assertArtifactPresent( "commons-logging", "commons-logging-api", "1.1", "jar" );
    }

    public void testitMNG2477RequiredChecksumFail()
        throws IOException, VerificationException
    {
        File testDir = new File( mainTestDir, "test-bad-checksum" );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );

        try
        {
            verifier.executeGoal( "compile" );
            fail( "Execution should not succeed with an invalid checksum" );
        }
        catch ( VerificationException e )
        {
            // expected
        }
        finally
        {
            verifier.resetStreams();
        }
    }

    public void testitMNG2477RequiredChecksumPomFail()
        throws IOException, VerificationException
    {
        File testDir = new File( mainTestDir, "test-bad-pom-checksum" );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );

        try
        {
            verifier.executeGoal( "compile" );
            fail( "Execution should not succeed with an invalid checksum" );
        }
        catch ( VerificationException e )
        {
            // expected
        }
        finally
        {
            verifier.resetStreams();
        }
    }*/

    public void testitMNG2477DeploymentPOMIsConvertedToModelv400()
        throws Exception
    {
        File testDir = new File( mainTestDir, "test-deployment" );

        File repository = new File( testDir, "deployment-repository" );
        FileUtils.deleteDirectory( repository );

        File file = new File( repository, "org/apache/maven/its/mng2477/test-deployment/1.0/test-deployment-1.0.pom" );
        assertFalse( file.exists() );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven.its.mng2477", "test-deployment", "1.0", "pom" );

        verifier.executeGoal( "deploy" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        assertTrue( file.exists() );

        // TODO: would be better to read it back as a model
        String contents = FileUtils.fileRead( file );
        assertFalse( contents.indexOf( "signaturePolicy" ) >= 0 );
        // not implemented yet
//        assertTrue( contents.indexOf( "requiredChecksum value=\"99129f16442844f6a4a11ae22fbbee40b14d774f\" pom=\"16d74791c801c89b0071b1680ea0bc85c93417bb\"" ) >= 0 );
    }
}
