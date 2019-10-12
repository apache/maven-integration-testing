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
import java.net.URI;
import org.apache.maven.it.util.ResourceExtractor;

/**
 * This is a test for <a href="https://issues.apache.org/jira/browse/MNG-6759">MNG-6759</a>.
 */
public class MavenITmng6759TransitiveDependencyRepositoriesTest extends AbstractMavenIntegrationTestCase {

    private final String projectBaseDir = "/mng-6759-transitive-dependency-repositories";

    public MavenITmng6759TransitiveDependencyRepositoriesTest() {
        super( "(,3.6.1],[3.6.3,)" );
    }

    /**
     * Verifies that a project with a dependency graph like A -> B -> C,
     * where C is in a non-Central repository should use B's {@literal <repositories>} to resolve C.
     */
    public void testTransitiveDependenciesAccountForRepositoriesListedByDependencyTrailPredecessor() throws Exception {
        URI customRepoUri = installDependencyCInCustomRepo();
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), projectBaseDir );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );

        verifier.addCliOption( "-Dcustom.repo.uri=" + customRepoUri );
        verifier.executeGoal( "package"  );
        verifier.verifyErrorFreeLog();
    }

    private URI installDependencyCInCustomRepo() throws Exception {
        File dependencyCProjectDir = ResourceExtractor.simpleExtractResources( getClass(), projectBaseDir + "/dependency-in-custom-repo" );
        URI customRepoUri = new File(new File(dependencyCProjectDir, "target" ), "repo" ).toURI();
        Verifier verifier = newVerifier( dependencyCProjectDir.getAbsolutePath() );

        verifier.deleteDirectory( "target" );
        verifier.addCliOption( "-DaltDeploymentRepository=customRepo::default::" + customRepoUri );
        verifier.executeGoal( "deploy" );
        verifier.verifyErrorFreeLog();
        return customRepoUri;
    }

}