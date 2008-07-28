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
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.IntegrationTestRunner;

public class MavenITmng3355TranslatedPathInterpolationTest
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng3355TranslatedPathInterpolationTest()
        throws org.apache.maven.artifact.versioning.InvalidVersionSpecificationException
    {
        super( "(2.0.8,)" ); // 2.0.9+
    }

    public void testitMNG3355()
        throws Exception
    {
        File testDir = extractTestResources( getClass(), "/mng-3355" );
        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );
        List cliOptions = new ArrayList();
        cliOptions.add( "-Dversion=foo" );
        itr.executeGoal( "validate", cliOptions );
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }
}
