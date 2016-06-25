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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-6049">MNG-6049</a>.
 *
 * <pre>
 *   <dependencies>
 *     <dependency>
 *       <groupId>org.apache.maven.its.mng6049</groupId>
 *       <artifactId>a</artifactId>
 *       <version>[1.0,2.0)</version>
 *     </dependency>
 *   </dependencies>
 * </pre>
 */
public class MavenITmng6049VersionRangeResultFilterExtensionTest
        extends AbstractMavenIntegrationTestCase
{

    public MavenITmng6049VersionRangeResultFilterExtensionTest()
    {
        // TODO Make inclusive as soon as Maven 3.4.0 is released
        super( "(3.4.0,)" );
    }

    /**
     * Verify that the Maven default behavior will be used without a VersionRangeResultFilter extension.
     */
    public void testDefault()
            throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-6049" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.deleteArtifacts( "org.apache.maven.its.mng6049" );
        verifier.filterFile( "settings-template.xml", "settings.xml", "UTF-8", verifier.newDefaultFilterProperties() );
        verifier.filterFile( "pom-mng-6049.xml", "pom.xml", "UTF-8", verifier.newDefaultFilterProperties() );
        verifier.addCliOption( "--settings" );
        verifier.addCliOption( "settings.xml" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        List<String> classpath = verifier.loadLines( "target/classpath.txt", "UTF-8" );
        assertTrue( classpath.toString(), classpath.contains( "a-2.0-SNAPSHOT.jar" ) );
    }

    /**
     * Verify that the Maven VersionRangeResultFilter extension behavior is active and checks that non-snapshot
     * version will be used.
     */
    public void testVersionRangeResultFilterExtensionSystemProperties()
            throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-6049" );
        File extensionDir = new File( testDir, "filter-extension" );

        final Map<String, String> filterProperties = new HashMap<>();
        filterProperties.put( "@baseurl@", "file://" + testDir.getAbsolutePath() );

        Verifier verifier;
        verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.filterFile( "settings-template.xml", "settings.xml", "UTF-8", filterProperties );
        verifier.filterFile( "extension.xml", ".mvn/extension.xml", "UTF-8", filterProperties );

        // install the test extension
        verifier = newVerifier( extensionDir.getAbsolutePath(), "remote" );
        verifier.filterFile( "pom.xml", "pom.xml", "UTF-8", filterProperties );
        verifier.addCliOption( "-f" );
        verifier.addCliOption( extensionDir.getAbsolutePath() + "/pom.xml" );
        verifier.addCliOption( "-Drat.skip=true" );
        verifier.setLogFileName( "install-extension.log" );

        verifier.executeGoal( "install" );
        verifier.resetStreams();
        verifier.verifyErrorFreeLog();

        // validate the test project
        verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( true );
        verifier.setDebug( true );
        verifier.setMavenDebug( true );
        verifier.filterFile( "pom-mng-6049.xml", "pom.xml", "UTF-8", filterProperties );

        verifier.addCliOption( "--settings" );
        verifier.addCliOption( testDir.getAbsolutePath() + "/settings.xml" );

        verifier.addCliOption( "-Dmaven.ext.class.path="
                + verifier.getArtifactPath( "org.apache.maven.its.extensions", "versionrange-resultfilter-extension",
                        "1.0-SNAPSHOT", "jar" ) );

        verifier.setLogFileName( "validate-extension.log" );
        verifier.executeGoal( "validate" );

        verifier.displayStreamBuffers();
        verifier.verifyErrorFreeLog();

        List<String> classpath = verifier.loadLines( "target/classpath.txt", "UTF-8" );
        assertTrue( classpath.toString(), classpath.contains( "a-1.2.jar" ) );
    }

}
