/*
 * Copyright 2016 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.maven.it;

import java.io.File;
import java.util.List;
import org.apache.maven.it.util.ResourceExtractor;
import static junit.framework.Assert.assertTrue;

/**
 * Integration tests for <a href="https://issues.apache.org/jira/browse/MNG-4463">MNG-4463</a>.
 *
 * @author Christian Schulte
 */
public class MavenITmng4463DependencyManagementImportVersionRanges
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng4463DependencyManagementImportVersionRanges()
    {
        super( "[3.4,)" );
    }

    public void testCanImportDependencyManagementUsingVersionRanges()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-4463" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath(), "remote" );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        List<String> artifacts = verifier.loadLines( "target/compile.txt", "UTF-8" );
        assertTrue( artifacts.toString(), artifacts.contains( "org.apache.maven:maven-plugin-api:jar:3.0" ) );
    }

}
