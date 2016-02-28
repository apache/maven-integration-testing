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
import java.util.Arrays;
import org.apache.maven.it.util.ResourceExtractor;

/**
 * [MNG-5799] Incorrect execution order of plugins in the same phase
 *
 * @author Christian Schulte
 */
public class MavenITmng5799ExecutionOrderTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng5799ExecutionOrderTest()
    {
        super( "[3.4,)" );
    }

    public void testExecutionOrder()
        throws Exception
    {
        final File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-5799" );

        final Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.executeGoals( Arrays.asList( new String[]
        {
            "clean", "test"
        } ) );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }

}
