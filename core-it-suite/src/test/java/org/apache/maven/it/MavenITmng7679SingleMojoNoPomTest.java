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

import org.apache.maven.shared.verifier.Verifier;
import org.apache.maven.shared.verifier.util.ResourceExtractor;
import org.junit.jupiter.api.Test;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-7679">MNG-7679</a>.
 * Executing single mojo without POM should not NPE.
 *
 */
class MavenITmng7679SingleMojoNoPomTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng7679SingleMojoNoPomTest()
    {
        super( "(,3.9.0),(3.9.0,)" );
    }

    /**
     * Verify that maven invocation works.
     *
     * @throws Exception in case of failure
     */
    @Test
    void testBuildSubtree()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-7679" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.addCliArgument( "install:install-file" );
        verifier.addCliArgument( "-Dfile=mng-7679.txt" );
        verifier.addCliArgument( "-DgroupId=org.apache.maven.it.mng7679" );
        verifier.addCliArgument( "-DartifactId=artifact" );
        verifier.addCliArgument( "-Dversion=1.0.0" );
        verifier.addCliArgument( "-Dpackaging=jar" );
        verifier.execute();
        verifier.verifyErrorFreeLog();
    }

}
