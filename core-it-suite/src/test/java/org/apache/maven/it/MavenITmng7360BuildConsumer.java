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
 * This test suite tests whether other modules in the same multi module project can be selected when invoking Maven from a submodule.
 * Related JIRA issue: <a href="https://issues.apache.org/jira/browse/MNG-7390">MNG-7390</a>.
 *
 * @author Martin Kanters
 */
public class MavenITmng7360BuildConsumer extends AbstractMavenIntegrationTestCase
{

    private static final String PROJECT_PATH = "/mng-7360-build-consumer";

    public MavenITmng7360BuildConsumer()
    {
        super( "[4.0.0-alpha-1,)" );
    }

    public void testSelectModuleByCoordinate() throws Exception
    {
        final File projectDir = ResourceExtractor.simpleExtractResources( getClass(), PROJECT_PATH );
        final Verifier verifier = newVerifier( projectDir.getAbsolutePath() );
        verifier.executeGoals( Arrays.asList( "clean" ) );
        verifier.verifyErrorFreeLog();
    }

}
