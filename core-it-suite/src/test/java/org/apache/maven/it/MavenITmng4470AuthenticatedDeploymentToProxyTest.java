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

import org.apache.maven.it.util.ResourceExtractor;
import org.apache.maven.it.utils.DeployedResource;
import org.codehaus.plexus.util.StringUtils;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NetworkConnector;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.B64Code;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Password;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import static org.eclipse.jetty.servlet.ServletContextHandler.SECURITY;
import static org.eclipse.jetty.servlet.ServletContextHandler.SESSIONS;
import static org.eclipse.jetty.util.security.Constraint.__BASIC_AUTH;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-4470">MNG-4470</a>.
 *
 * @author Benjamin Bentmann
 *
 */
public class MavenITmng4470AuthenticatedDeploymentToProxyTest
    extends AbstractMavenIntegrationTestCase
{
    private Server server;

    private int port;

    private volatile boolean deployed;

    private final Deque<DeployedResource> deployedResources = new ConcurrentLinkedDeque<>();

    public MavenITmng4470AuthenticatedDeploymentToProxyTest()
    {
        super( "[2.0.3,3.0-alpha-1),[3.0-alpha-6,)" );
    }

    @Override
    protected void setUp()
        throws Exception
    {
        Handler proxyHandler = new AbstractHandler()
        {
            @Override
            public void handle( String target, Request baseRequest, HttpServletRequest request,
                                HttpServletResponse response )
            {
                System.out.println( "Handling " + request.getMethod() + " " + request.getRequestURL() );

                String auth = request.getHeader( "Proxy-Authorization" );
                if ( auth != null )
                {
                    auth = auth.substring( auth.indexOf( ' ' ) + 1 ).trim();
                    auth = B64Code.decode( auth, StandardCharsets.US_ASCII.name() );
                }
                System.out.println( "Proxy-Authorization: " + auth );

                if ( !"proxyuser:proxypass".equals( auth ) )
                {
                    response.setStatus( HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED );
                    response.addHeader( "Proxy-Authenticate", "Basic realm=\"Squid proxy-caching web server\"" );
                    ( (Request) request ).setHandled( true );
                }

                DeployedResource deployedResource = new DeployedResource();

                deployedResource.httpMethod = request.getMethod();
                deployedResource.requestUri = request.getRequestURI();
                deployedResource.transferEncoding = request.getHeader( "Transfer-Encoding" );
                deployedResource.contentLength = request.getHeader( "Content-Length" );

                deployedResources.add( deployedResource );
            }
        };

        Handler repoHandler = new AbstractHandler()
        {
            @Override
            public void handle( String target, Request baseRequest, HttpServletRequest request,
                                HttpServletResponse response )
            {
                System.out.println( "Handling " + request.getMethod() + " " + request.getRequestURL() );

                if ( "PUT".equalsIgnoreCase( request.getMethod() ) )
                {
                    response.setStatus( HttpServletResponse.SC_OK );
                    deployed = true;
                }
                else
                {
                    response.setStatus( HttpServletResponse.SC_NOT_FOUND );
                }

                ( (Request) request ).setHandled( true );

                DeployedResource deployedResource = new DeployedResource();

                deployedResource.httpMethod = request.getMethod();
                deployedResource.requestUri = request.getRequestURI();
                deployedResource.transferEncoding = request.getHeader( "Transfer-Encoding" );
                deployedResource.contentLength = request.getHeader( "Content-Length" );

                deployedResources.add( deployedResource );
            }
        };

        Constraint constraint = new Constraint();
        constraint.setName( Constraint.__BASIC_AUTH );
        constraint.setRoles( new String[]{ "deployer" } );
        constraint.setAuthenticate( true );

        ConstraintMapping constraintMapping = new ConstraintMapping();
        constraintMapping.setConstraint( constraint );
        constraintMapping.setPathSpec( "/*" );

        HashLoginService userRealm = new HashLoginService( "TestRealm" );
        userRealm.putUser( "testuser", new Password( "testtest" ), new String[] { "deployer" } );

        server = new Server( 0 );
        ServletContextHandler ctx = new ServletContextHandler( server, "/", SESSIONS | SECURITY );
        ConstraintSecurityHandler securityHandler = (ConstraintSecurityHandler) ctx.getSecurityHandler();
        securityHandler.setLoginService( userRealm );
        securityHandler.setAuthMethod( __BASIC_AUTH );
        securityHandler.setConstraintMappings( new ConstraintMapping[] { constraintMapping } );

        HandlerList handlerList = new HandlerList();
        handlerList.addHandler( proxyHandler );
        handlerList.addHandler( securityHandler );
        handlerList.addHandler( repoHandler );

        server.setHandler( handlerList );
        server.start();
        if ( server.isFailed() )
        {
            fail( "Couldn't bind the server socket to a free port!" );
        }
        port = ( (NetworkConnector) server.getConnectors()[0] ).getLocalPort();
        System.out.println( "Bound server socket to the port " + port );
        deployed = false;
    }

    @Override
    protected void tearDown()
        throws Exception
    {
        if ( server != null )
        {
            server.stop();
            server.join();
        }
    }

    /**
     * Test that deployment (of a release) to a proxy that requires authentication works.
     *
     * @throws Exception in case of failure
     */
    public void testitRelease()
        throws Exception
    {
        testit( "release" );
    }

    /**
     * Test that deployment (of a snapshot) to a proxy that requires authentication works.
     *
     * @throws Exception in case of failure
     */
    public void testitSnapshot()
        throws Exception
    {
        testit( "snapshot" );
    }

    private void testit( String project )
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-4470/" + project );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.filterFile( "settings-template.xml", "settings.xml", "UTF-8",
                             Collections.singletonMap( "@port@", Integer.toString( port ) ) );
        verifier.addCliOption( "--settings" );
        verifier.addCliOption( "settings.xml" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        for ( DeployedResource deployedResource : deployedResources )
        {
            if ( StringUtils.equalsIgnoreCase( "chunked", deployedResource.transferEncoding ) )
            {
                fail( "deployedResource " + deployedResource
                          + " use chuncked transfert encoding some http server doesn't support that" );
            }
        }

        assertTrue( deployed );
    }
}
