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
import static org.apache.maven.it.ItUtils.verifyTextNotInLog;

import java.io.File;

public class MavenITmng7568SettingsProfileDeactivationTest
        extends AbstractMavenIntegrationTestCase
{
    private static final String PROJECT_PATH = "/mng-7568-inactive-profile-deactivation";

    public MavenITmng7568SettingsProfileDeactivationTest()
    {
        super( "[3.8.7,4)" );
    }

    /**
     * This test verifies that deactivating a profile defined in settings and in project
     * does not cause unnecessary warning.
     *
     * @throws Exception in case of failure
     */
    public void testDeactivatingProfileExistingInSettingsAndInProject() throws Exception
    {
        final File projectDir = ResourceExtractor.simpleExtractResources( getClass(), PROJECT_PATH );
        final Verifier verifier = newVerifier( projectDir.getAbsolutePath() );

        verifier.addCliOption( "-s" );
        verifier.addCliOption( "settings.xml" );
        verifier.addCliOption( "-P" );
        verifier.addCliOption( "-k-profile" );
        verifier.setLogFileName( "test-mng7568.txt" );

        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();

        verifyTextNotInLog( verifier, "[WARNING] The requested profile \"k-profile\" could not be activated because it does not exist." );
    }
}
