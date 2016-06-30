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

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

import java.io.File;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-4963">MNG-4963</a>.
 * 
 * @author Benjamin Bentmann
 */
public class MavenITmng4963ParentResolutionFromMirrorTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng4963ParentResolutionFromMirrorTest()
    {
        // This test got disabled for Maven >= 3.4 because the test is testing built-in repository behaviour. As of
        // Maven 3.4 built-in repositories have been moved to the default settings and thus this test is obsolete as of
        // Maven 3.4 because there is no way to test built-in repositories without declaring them in the settings or the
        // pom.
        super( "[2.0.5,3.0-alpha-1),[3.0.3,3.4)" );
    }

    /**
     * Verify that a released parent POM can be resolved when the settings define only a snapshot repository
     * which is subject to mirroring. Technically, this means to properly aggregate the built-in central repo
     * definition with the declared snapshot repo when both are mirrored.
     */
    public void testit()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-4963" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.deleteArtifacts( "org.apache.maven.its.mng4963" );
        verifier.addCliOption( "-s" );
        verifier.addCliOption( "settings.xml" );
        verifier.filterFile( "settings-template.xml", "settings.xml", "UTF-8", verifier.newDefaultFilterProperties() );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }

}
