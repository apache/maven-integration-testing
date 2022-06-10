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
package org.apache.maven.it;

import org.apache.maven.it.util.ResourceExtractor;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MavenITmng7244IgnorePomPrefixInExpressions extends AbstractMavenIntegrationTestCase
{
    private static final String PROJECT_PATH = "/mng-7244-ignore-pom-prefix-in-expressions";

    public MavenITmng7244IgnorePomPrefixInExpressions()
    {
        super( "[4.0.0-alpha-1,)" );
    }

    public void testIgnorePomPrefixInExpressions() throws IOException, VerificationException
    {
        final File projectDir = ResourceExtractor.simpleExtractResources( getClass(), PROJECT_PATH );
        final Verifier verifier = newVerifier( projectDir.getAbsolutePath() );

        verifier.executeGoal( "validate" );

        verifyLogDoesNotContainUnexpectedWarning( verifier );
    }

    private void verifyLogDoesNotContainUnexpectedWarning( Verifier verifier ) throws IOException
    {
        List<String> loadedLines = verifier.loadLines( "log.txt", "UTF-8" );
        for ( String line : loadedLines )
        {
            if ( line.startsWith( "[WARNING]" ) && line.contains( "The expression ${pom.version} is deprecated." ) )
            {
                fail( "Log contained unexpected deprecation warning" );
            }
        }
    }
}
