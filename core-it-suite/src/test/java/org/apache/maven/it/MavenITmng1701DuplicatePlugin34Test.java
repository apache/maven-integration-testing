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
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-1701">MNG-1701</a>.
 *
 * @author Benjamin Bentmann
 */
public class MavenITmng1701DuplicatePlugin34Test
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng1701DuplicatePlugin34Test()
    {
        super( "[3.4,)" );
    }

    /**
     * Verify that duplicate plugin declarations cause an error.
     */
    public void testit()
        throws Exception
    {
        Verifier verifier = null;

        try
        {
            File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-1701" );

            verifier = newVerifier( testDir.getAbsolutePath() );
            verifier.setAutoclean( false );
            verifier.executeGoal( "validate" );
            fail( "Expected 'VerificationException' due to duplicate plugin declarations not thrown." );
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
