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

import org.apache.maven.shared.verifier.Verifier;
import org.apache.maven.shared.verifier.util.ResourceExtractor;
import org.codehaus.plexus.util.Os;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.JRE;

import java.io.File;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-7737">MNG-7737</a>.
 * Simply verifies that various (expected) profiles are properly activated or not.
 *
 */
class MavenITmng7587Jsr330
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng7587Jsr330()
    {
        // affected Maven versions: 3.9.0
        super( "(3.9.2,4.0.0-alpha-1),(4.0.0-alpha-5,)" );
    }

    /**
     * Verify components can be written using JSR330 on JDK 1.8.
     *
     * @throws Exception in case of failure
     */
    @Test
    @EnabledOnJre({JRE.JAVA_8, JRE.JAVA_11, JRE.JAVA_17})
    void testJdk8()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-7587-jsr330");

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.addCliArgument( "clean" );
        verifier.addCliArgument( "verify" );
        verifier.addCliArgument( "-Drunning-java-release-version=1.8" );
        verifier.execute();
        verifier.verifyErrorFreeLog();
    }

    /**
     * Verify components can be written using JSR330 on JDK 11.
     *
     * @throws Exception in case of failure
     */
    @Test
    @EnabledOnJre({JRE.JAVA_11, JRE.JAVA_17})
    void testJdk11()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-7587-jsr330");

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.addCliArgument( "clean" );
        verifier.addCliArgument( "verify" );
        verifier.addCliArgument( "-Drunning-java-release-version=11" );
        verifier.execute();
        verifier.verifyErrorFreeLog();
    }


    /**
     * Verify components can be written using JSR330 on JDK 17.
     *
     * @throws Exception in case of failure
     */
    @Test
    @EnabledOnJre(JRE.JAVA_17)
    void testJdk17()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-7587-jsr330");

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.addCliArgument( "clean" );
        verifier.addCliArgument( "verify" );
        verifier.addCliArgument( "-Drunning-java-release-version=17" );
        verifier.execute();
        verifier.verifyErrorFreeLog();
    }
}
