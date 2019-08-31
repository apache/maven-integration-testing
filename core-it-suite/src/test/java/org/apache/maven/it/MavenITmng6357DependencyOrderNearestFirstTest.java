package org.apache.maven.it;

import java.io.File;
import java.util.Properties;

import org.apache.maven.it.util.ResourceExtractor;

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

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-6357">MNG-6357</a>.
 *
 * @author Gabriel Belingueres
 */
public class MavenITmng6357DependencyOrderNearestFirstTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng6357DependencyOrderNearestFirstTest()
    {
        super("(3.6.2,)");
    }

    /**
     * Verify that the ordering of the class path matches the ordering of the dependencies as given in the POM, where
     * artifacts are listed from the nearest to the farest in depth from the root of the dependency tree. Dependencies
     * at the same depth are listed in the order declared in the POM.<br/>
     * <br/>
     * Note: test dependencies used in this test were copied from mng-3813 just to show the new ordering using the same
     * artifacts.
     */
    public void testitMNG6357()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-6357" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.deleteArtifacts( "org.apache.maven.its.mng3813" );
        verifier.filterFile( "settings-template.xml", "settings.xml", "UTF-8", verifier.newDefaultFilterProperties() );
        verifier.addCliOption( "--settings" );
        verifier.addCliOption( "settings.xml" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        Properties pclProps = verifier.loadProperties( "target/pcl.properties" );

        String className = "org.apache.maven.its.mng3813.SomeClass";
        String resName = className.replace( '.', '/' ) + ".class";

        assertEquals( "depA", pclProps.getProperty( className + ".methods" ) );
        assertTrue( pclProps.getProperty( resName ).endsWith( "/dep-a-0.1.jar!/" + resName ) );

        assertEquals( "8", pclProps.getProperty( resName + ".count" ) );

        assertTrue( pclProps.getProperty( resName + ".0" ).endsWith( "/dep-a-0.1.jar!/" + resName ) );
        assertTrue( pclProps.getProperty( resName + ".1" ).endsWith( "/dep-c-0.1.jar!/" + resName ) );
        assertTrue( pclProps.getProperty( resName + ".2" ).endsWith( "/dep-b-0.1.jar!/" + resName ) );
        assertTrue( pclProps.getProperty( resName + ".3" ).endsWith( "/dep-d-0.1.jar!/" + resName ) );
        assertTrue( pclProps.getProperty( resName + ".4" ).endsWith( "/dep-aa-0.1.jar!/" + resName ) );
        assertTrue( pclProps.getProperty( resName + ".5" ).endsWith( "/dep-ac-0.1.jar!/" + resName ) );
        assertTrue( pclProps.getProperty( resName + ".6" ).endsWith( "/dep-ab-0.1.jar!/" + resName ) );
        assertTrue( pclProps.getProperty( resName + ".7" ).endsWith( "/dep-ad-0.1.jar!/" + resName ) );
    }

}