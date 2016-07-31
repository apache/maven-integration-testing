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
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-3719">MNG-3719</a>.
 *
 * @author Brett Porter
 * @version $Id$
 */
public class MavenITmng3719PomExecutionOrdering34Test
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng3719PomExecutionOrdering34Test()
    {
        super( "[3.4,)" );
    }

    /**
     * Test that 3 executions are run in the correct order.
     */
    public void testitMNG3719()
        throws Exception
    {
        Verifier verifier = null;

        try
        {
            File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-3719" );

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
