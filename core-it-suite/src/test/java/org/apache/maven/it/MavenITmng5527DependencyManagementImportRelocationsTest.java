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
import java.util.Map;
import java.util.Properties;

import org.apache.maven.it.util.ResourceExtractor;

import static junit.framework.Assert.assertEquals;

/**
 * [MNG-5527] Dependency management import should support relocations.
 *
 * @author Christian Schulte
 */
public class MavenITmng5527DependencyManagementImportRelocationsTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng5527DependencyManagementImportRelocationsTest()
    {
        super( "[3.4.0,)" );
    }

    public void testCanRelocateDependencyManagementImport()
        throws Exception
    {
        final File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-5527/relocations" );

        final Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.filterFile( "../settings-template.xml", "settings.xml", "UTF-8",
                             (Map) verifier.newDefaultFilterProperties() );

        verifier.addCliOption( "-s" );
        verifier.addCliOption( "settings.xml" );
        verifier.executeGoals( Arrays.asList( new String[]
        {
            "clean", "verify"
        } ) );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        verifier.verifyTextInLog( "LEVEL_1" );
        verifier.verifyTextInLog( "LEVEL_2" );

        final Properties properties = verifier.loadProperties( "target/project.properties" );
        assertEquals( "1", properties.getProperty( "project.dependencyManagement.dependencies" ) );
        assertEquals( "group-id-to-import:artifact-id-to-import:jar",
                      properties.getProperty( "project.dependencyManagement.dependencies.0.managementKey" ) );

    }

}
