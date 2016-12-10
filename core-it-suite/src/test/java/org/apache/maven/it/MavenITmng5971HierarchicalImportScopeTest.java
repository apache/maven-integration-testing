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
 * [MNG-5971] Imported dependencies should be available to inheritance processing.
 *
 * @author Christian Schulte
 */
public class MavenITmng5971HierarchicalImportScopeTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng5971HierarchicalImportScopeTest()
    {
        super( "[3.4,)" );
    }

    public void testInheritanceProcessing()
        throws Exception
    {
        final File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-5971/inheritance" );

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

        final List<String> dependencies0 = verifier.loadLines( "target/compile.txt", "UTF-8" );
        assertTrue( contains( dependencies0, "org.apache.maven.its.mng5971:dependency:jar:0" ) );

        final List<String> dependencies1 = verifier.loadLines( "1/target/compile.txt", "UTF-8" );
        assertTrue( contains( dependencies1, "org.apache.maven.its.mng5971:dependency:jar:1" ) );

        final List<String> dependencies2 = verifier.loadLines( "1/2/target/compile.txt", "UTF-8" );
        assertTrue( contains( dependencies2, "org.apache.maven.its.mng5971:dependency:jar:2" ) );

        final List<String> dependencies3 = verifier.loadLines( "1/2/3/target/compile.txt", "UTF-8" );
        assertTrue( contains( dependencies3, "org.apache.maven.its.mng5971:dependency:jar:3" ) );
    }

    public void testOverrideProcessing()
        throws Exception
    {
        final File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-5971/override" );

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

        final List<String> dependencies = verifier.loadLines( "target/compile.txt", "UTF-8" );
        assertTrue( contains( dependencies, "org.apache.maven.its.mng5971:dependency:jar:3" ) );
    }

    public void testInheritanceProcessingWithProjectBasedProperties()
        throws Exception
    {
        final File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-5971/properties" );

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

        final List<String> dependencies0 = verifier.loadLines( "target/compile.txt", "UTF-8" );
        assertTrue( contains( dependencies0, "org.apache.maven.its.mng5971:dependency:jar:0" ) );

        final List<String> dependencies1 = verifier.loadLines( "1/target/compile.txt", "UTF-8" );
        assertTrue( contains( dependencies1, "org.apache.maven.its.mng5971:dependency:jar:1" ) );

        final List<String> dependencies2 = verifier.loadLines( "1/2/target/compile.txt", "UTF-8" );
        assertTrue( contains( dependencies2, "org.apache.maven.its.mng5971:dependency:jar:2" ) );

        final List<String> dependencies3 = verifier.loadLines( "1/2/3/target/compile.txt", "UTF-8" );
        assertTrue( contains( dependencies3, "org.apache.maven.its.mng5971:dependency:jar:3" ) );

        final List<String> dependencies4 = verifier.loadLines( "1/2/3/4/target/compile.txt", "UTF-8" );
        assertTrue( contains( dependencies4, "org.apache.maven.its.mng5971:dependency:jar:3" ) );
    }

    public void testIncludeInheritanceProcessingWithProjectBasedProperties()
        throws Exception
    {
        final File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-5971/include-properties" );

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

        final List<String> dependencies0 = verifier.loadLines( "target/compile.txt", "UTF-8" );
        assertTrue( contains( dependencies0, "org.apache.maven.its.mng5971:dependency:jar:0" ) );

        final List<String> dependencies1 = verifier.loadLines( "1/target/compile.txt", "UTF-8" );
        assertTrue( contains( dependencies1, "org.apache.maven.its.mng5971:dependency:jar:1" ) );

        final List<String> dependencies2 = verifier.loadLines( "1/2/target/compile.txt", "UTF-8" );
        assertTrue( contains( dependencies2, "org.apache.maven.its.mng5971:dependency:jar:2" ) );

        final List<String> dependencies3 = verifier.loadLines( "1/2/3/target/compile.txt", "UTF-8" );
        assertTrue( contains( dependencies3, "org.apache.maven.its.mng5971:dependency:jar:3" ) );

        final List<String> dependencies4 = verifier.loadLines( "1/2/3/4/target/compile.txt", "UTF-8" );
        assertTrue( contains( dependencies4, "org.apache.maven.its.mng5971:dependency:jar:3" ) );
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
