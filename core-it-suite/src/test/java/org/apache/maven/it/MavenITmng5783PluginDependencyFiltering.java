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
import java.util.List;

import org.apache.maven.it.util.ResourceExtractor;

public class MavenITmng5783PluginDependencyFiltering
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng5783PluginDependencyFiltering()
    {
        super( "[3.0,)" );
    }

    public void testSLF4j()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-5783-plugin-dependency-filtering" );
        Verifier verifier = newVerifier( new File( testDir, "plugin" ).getAbsolutePath(), "remote" );
        verifier.executeGoal( "install" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        verifier = newVerifier( new File( testDir, "slf4j" ).getAbsolutePath(), "remote" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        final List<String> dependencies = verifier.loadLines( "target/dependencies.txt", "UTF-8" );
        assertTrue( contains( dependencies, "org.slf4j:slf4j-api:jar:1.7.5" ) );
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
