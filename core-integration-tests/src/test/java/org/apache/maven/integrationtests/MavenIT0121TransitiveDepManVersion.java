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
import java.io.IOException;

import org.apache.maven.it.IntegrationTestException;
import org.apache.maven.it.IntegrationTestRunner;

public class MavenIT0121TransitiveDepManVersion
    extends AbstractMavenIntegrationTestCase
{
    public void testit0121()
        throws Exception
    {
        File testDirBase = extractTestResources( getClass(), "/it0121-transitiveDepManVersion" );

        compileDDep( testDirBase, "D1", "1.0" );
        compileDDep( testDirBase, "D2", "2.0" );

        File testProjectDir = new File( testDirBase, "test-project" );
        
        IntegrationTestRunner itr = new IntegrationTestRunner( testProjectDir.getAbsolutePath() );
        itr.deleteArtifact( "org.apache.maven.its.it0121", "A", "1.0", "pom" );
        itr.deleteArtifact( "org.apache.maven.its.it0121", "A", "1.0", "jar" );
        itr.deleteArtifact( "org.apache.maven.its.it0121", "B", "1.0", "pom" );
        itr.deleteArtifact( "org.apache.maven.its.it0121", "B", "1.0", "jar" );
        itr.deleteArtifact( "org.apache.maven.its.it0121", "C", "1.0", "pom" );
        itr.deleteArtifact( "org.apache.maven.its.it0121", "D", "1.0", "jar" );
        itr.invoke( "install" );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }

    private void compileDDep( File testDirBase, String projectDDepDir, String version )
        throws IntegrationTestException, IOException
    {
        File testOtherDepDir = new File( testDirBase, "test-other-deps/" + projectDDepDir );
        IntegrationTestRunner itrOtherDep = new IntegrationTestRunner( testOtherDepDir.getAbsolutePath() );
        itrOtherDep.deleteArtifact( "org.apache.maven.its.it0121", "D", version, "jar" );
        itrOtherDep.deleteArtifact( "org.apache.maven.its.it0121", "D", version, "pom" );
        itrOtherDep.invoke( "install" );
        itrOtherDep.verifyErrorFreeLog();
        itrOtherDep.resetStreams();
    }
}
