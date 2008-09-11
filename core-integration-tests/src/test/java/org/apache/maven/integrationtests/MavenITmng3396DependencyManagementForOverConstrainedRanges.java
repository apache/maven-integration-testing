package org.apache.maven.integrationtests;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.IntegrationTestRunner;

public class MavenITmng3396DependencyManagementForOverConstrainedRanges
    extends AbstractMavenIntegrationTestCase
{
    private static final String GROUP_ID = "org.apache.maven.its.mng3396";

    public MavenITmng3396DependencyManagementForOverConstrainedRanges()
    throws InvalidVersionSpecificationException
{
    super( "(2.0.8,)" ); // 2.0.9+
}
    
    public void testitMNG3396()
        throws Exception
    {
        String baseDir = "/mng-3396-dependencyManagementForOverConstrainedRanges";
        File testDir = extractTestResources( getClass(), baseDir + "/dependencies" );

        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( GROUP_ID, "A", "1.0", "pom" );
        itr.deleteArtifact( GROUP_ID, "A", "1.0", "jar" );
        itr.deleteArtifact( GROUP_ID, "B", "1.0", "pom" );
        itr.deleteArtifact( GROUP_ID, "B", "1.0", "jar" );
        itr.invoke( "install" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

        testDir = extractTestResources( getClass(), baseDir + "/plugin" );

        itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( GROUP_ID, "A", "1.0", "pom" );
        itr.deleteArtifact( GROUP_ID, "A", "1.0", "jar" );
        itr.deleteArtifact( GROUP_ID, "A", "3.0", "pom" );
        itr.deleteArtifact( GROUP_ID, "A", "3.0", "jar" );
        itr.deleteArtifact( GROUP_ID, "plugin", "1.0", "pom" );
        itr.deleteArtifact( GROUP_ID, "plugin", "1.0", "jar" );
        itr.invoke( "install" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();

        testDir = extractTestResources( getClass(), baseDir + "/pluginuser" );

        itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        itr.deleteArtifact( GROUP_ID, "pluginuser", "1.0", "pom" );
        itr.deleteArtifact( GROUP_ID, "pluginuser", "1.0", "jar" );
        itr.invoke( "install" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }
}
