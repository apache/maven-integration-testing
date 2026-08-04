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
package org.apache.maven.its.mng7160;

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.execution.MavenSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Named
public class Extension extends AbstractMavenLifecycleParticipant {

    private final Logger logger = LoggerFactory.getLogger(Extension.class);

    @Override
    public void afterProjectsRead(MavenSession session) throws MavenExecutionException {
        logger.debug("extension afterProjectsRead called");
        logger.info("extension afterProjectsRead called");
    }

    @Override
    public void afterSessionStart(MavenSession session) throws MavenExecutionException {
        logger.debug("extension afterSessionStart called");
        logger.info("extension afterSessionStart called");
    }

    @Override
    public void afterSessionEnd(MavenSession session) throws MavenExecutionException {
        logger.debug("extension afterSessionEnd called");
        logger.info("extension afterSessionEnd called");
    }
}
