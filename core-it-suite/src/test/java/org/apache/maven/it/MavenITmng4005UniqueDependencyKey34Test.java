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

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-4005">MNG-4005</a>.
 *
 * @author Benjamin Bentmann
 */
public class MavenITmng4005UniqueDependencyKey34Test
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng4005UniqueDependencyKey34Test()
    {
        super( "[3.4,)" );
    }

    /**
     * Test that duplicate dependencies cause a validation error during building.
     */
    public void testitDependency()
        throws Exception
    {
        test( "dep" );
    }

    /**
     * Test that duplicate managed dependencies cause a validation error during building.
     */
    public void testitManagedDependency()
        throws Exception
    {
        test( "man-dep" );
    }

    /**
     * Test that duplicate dependencies in profiles cause a validation error during building.
     */
    public void testitProfileDependency()
        throws Exception
    {
        test( "profile-dep" );
    }

    /**
     * Test that duplicate managed dependencies in profiles cause a validation error during building.
     */
    public void testitProfileManagedDependency()
        throws Exception
    {
        test( "profile-man-dep" );
    }

    private void test( String project )
        throws Exception
    {
        Verifier verifier = null;

        try
        {
            File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-4005/" + project );

            verifier = newVerifier( testDir.getAbsolutePath() );
            verifier.setAutoclean( false );
            verifier.deleteDirectory( "target" );
            verifier.executeGoal( "validate" );

            fail( "Expected 'VerificationException' not thrown." );
        }
        catch ( final VerificationException e )
        {
            // Expected.
        }
        finally
        {
            if ( verifier != null )
            {
                verifier.resetStreams();
            }
        }
    }

}
