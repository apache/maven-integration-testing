package org.apache.maven.its.extensions;

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

import javax.inject.Named;
import java.util.Iterator;
import javax.inject.Inject;
import org.apache.maven.repository.internal.VersionRangeResultFilter;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.spi.log.Logger;
import org.eclipse.aether.spi.log.LoggerFactory;
import org.eclipse.aether.spi.log.NullLoggerFactory;
import org.eclipse.aether.version.Version;
import org.eclipse.sisu.Nullable;

/**
 * Example implementation for use in ITs.
 * <p>
 * This implementation removes <b>all</b> SNAPSHOT dependencies.
 * <p>
 * Part of the test set <a href="https://issues.apache.org/jira/browse/MNG-6049">MNG-6049</a>
 * and only works with Maven >= 3.4.0.
 */
@Named
public class MavenITVersionRangeResultFilter implements VersionRangeResultFilter
{

    private final Logger logger;

    @Inject
    public MavenITVersionRangeResultFilter( @Nullable LoggerFactory loggerfactory )
    {
        this.logger = ( ( null == loggerfactory ) ? NullLoggerFactory.LOGGER : loggerfactory.getLogger(
                VersionRangeResultFilter.class.getName() ) );
    }

    @Override
    public VersionRangeResult filterVersionRangeResult( VersionRangeResult versionRangeResult )
            throws VersionRangeResolutionException
    {
        if ( !"org.apache.maven.its.mng6049".equals( versionRangeResult.getRequest().getArtifact().getGroupId() ) )
        {
            return versionRangeResult;
        }
        this.logger.debug( "[MAVEN-IT-CORE-MNG-6049] Version range result instance: " + versionRangeResult );
        for ( Iterator<Version> it = versionRangeResult.getVersions().iterator(); it.hasNext(); )
        {
            final Version version = it.next();
            // XXX: better way to identify a SNAPSHOT version
            if ( String.valueOf( version ).endsWith( "SNAPSHOT" ) )
            {
                this.logger.debug( "[MAVEN-IT-CORE-MNG-6049] Remove version: " + version );
                it.remove();
            }
        }
        return versionRangeResult;
    }

}
