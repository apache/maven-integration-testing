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

import org.apache.maven.it.util.ResourceExtractor;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-6772">MNG-6772</a>:
 *
 * The pom.xml project POM imports dependencyManagement from bom-a POM, which also imports dependencyManagement from another bom-b POM.
 *
 * Both BOM POMs can only be found in the repository defined in the project POM, that overrides central repository.
 *
 * The test checks that central repository override from project pom.xml remains used to get bom-b POM.
 *
 */
public class MavenITmng6772NestedImportScopeRepositoryOverride
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng6772NestedImportScopeRepositoryOverride()
    {
        super( "[4.0.0-alpha-1,)" );
    }

    // This will test the behavior using ProjectModelResolver
    public void testitInProject()
        throws Exception
    {
        final File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-6772-override-in-project" );

        final Verifier verifier = newVerifier( testDir.getAbsolutePath(), null );
        overrideSettings( testDir, verifier );
        verifier.deleteArtifacts( "org.apache.maven.its.mng6772" );

        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }

    // central must not be defined in any settings.xml or super POM will never be in play.
    private void overrideSettings( final File testDir, final Verifier verifier )
    {
        final File settingsFile = new File( testDir, "settings-override.xml" );
        final String path = settingsFile.getAbsolutePath();

        verifier.getCliOptions().add( "--global-settings" );
        if ( path.indexOf( ' ' ) < 0 )
        {
            verifier.getCliOptions().add( path );
        }
        else
        {
            verifier.getCliOptions().add( '"' + path + '"' );
        }

        verifier.getCliOptions().add( "--settings" );
        if ( path.indexOf( ' ' ) < 0 )
        {
            verifier.getCliOptions().add( path );
        }
        else
        {
            verifier.getCliOptions().add( '"' + path + '"' );
        }
    }

}
