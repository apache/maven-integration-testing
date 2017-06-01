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
import java.util.List;
import java.util.Map;

import org.apache.maven.it.util.ResourceExtractor;

import static junit.framework.Assert.assertTrue;

/**
 * [MNG-6079] 3.4 regression: cannot override version of a dependencyManagement in a submodule any more
 *
 * @author Christian Schulte
 */
public class MavenITmng6079DependencyManagementImportInterpolationTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng6079DependencyManagementImportInterpolationTest()
    {
        super( "[2.0.9,)" );
    }

    public void testInheritanceProcessing()
        throws Exception
    {
        final File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-6079" );

        final Verifier verifier = newVerifier( testDir.getAbsolutePath(), "remote" );
        verifier.setAutoclean( false );
        verifier.filterFile( "settings-template.xml", "settings.xml", "UTF-8",
                             (Map) verifier.newDefaultFilterProperties() );

        verifier.addCliOption( "-s" );
        verifier.addCliOption( "settings.xml" );
        verifier.executeGoals( Arrays.asList( new String[]
        {
            "clean", "verify"
        } ) );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        final List<String> dependencies0 = verifier.loadLines( "target/compile.txt", "UTF-8" );
        assertTrue( contains( dependencies0, "org.apache.maven.surefire:surefire-api:jar:2.12" ) );
    }

    private static boolean contains( final List<String> lines, final String pattern )
    {
        for ( int i = 0, l0 = lines.size(); i < l0; i++ )
        {
            if ( lines.get( i ).contains( pattern ) )
            {
                return true;
            }
        }

        return false;
    }

}
