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

import java.io.File;
import java.util.Properties;

import org.apache.maven.shared.utils.StringUtils;
import org.apache.maven.shared.verifier.Verifier;
import org.apache.maven.shared.verifier.util.ResourceExtractor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This is a test set for <a href="https://github.com/apache/maven/issues/10937">gh-10937</a>.
 */
class MavenITgh10937QuotedPipesInMavenOptsTest extends AbstractMavenIntegrationTestCase {

    MavenITgh10937QuotedPipesInMavenOptsTest() {
        super("[3.9.12,)");
    }

    /**
     *  Verify the dependency management of the consumer POM is computed correctly
     */
    @Test
    void testIt() throws Exception {
        File basedir = ResourceExtractor.simpleExtractResources(getClass(), "/gh-10937-pipes-maven-opts");

        Verifier verifier = newVerifier(basedir.getAbsolutePath());
        verifier.setEnvironmentVariable("MAVEN_OPTS", "-Dprop.maven-opts=\"foo|bar\"");
        verifier.addCliArguments("validate");
        verifier.execute();
        verifier.verifyErrorFreeLog();

        Properties props = verifier.loadProperties("target/pom.properties");
        // Strip quotes because the bash startup script in 3.9.x preserves them while cmd does not
        // In this test we only care that the pipe does not cause issues and is retained,
        // not what happens to the surrounding quotes.
        assertEquals("foo|bar", StringUtils.strip(props.getProperty("project.properties.pom.prop.jvm-opts"), "\""));
        assertEquals("foo|bar", StringUtils.strip(props.getProperty("project.properties.pom.prop.maven-opts"), "\""));
    }
}
