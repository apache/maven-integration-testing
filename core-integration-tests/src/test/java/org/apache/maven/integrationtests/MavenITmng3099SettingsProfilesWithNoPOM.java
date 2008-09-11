package org.apache.maven.integrationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.DefaultInvocationRequest;
import org.apache.maven.it.IntegrationTestRunner;
import org.apache.maven.it.InvocationRequest;

/**
 * This is a sample integration test. The IT tests typically operate by having a sample project in
 * the /src/test/resources folder along with a junit test like this one. The junit test uses the
 * itr (which uses the invoker) to invoke a new instance of Maven on the project in the
 * resources folder. It then checks the results. This is a non-trivial example that shows two
 * phases. See more information inline in the code.
 * 
 * @author <a href="mailto:brianf@apache.org">Brian Fox</a>
 * 
 */
public class MavenITmng3099SettingsProfilesWithNoPOM
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng3099SettingsProfilesWithNoPOM()
        throws InvalidVersionSpecificationException
    {
        super( "(2.0.8,)" ); // 2.0.9+
    }

    public void testitMNG3099()
        throws Exception
    {
        // The testdir is computed from the location of this
        // file.
        File testDir = extractTestResources( getClass(), "/mng-3099-settingsProfilesWithNoPOM" );

        File plugin = new File( testDir, "plugin" );

        IntegrationTestRunner itr;

        itr = new IntegrationTestRunner( plugin.getAbsolutePath() );

        itr.invoke( "install" );

        /*
         * Reset the streams before executing the itr
         * again.
         */
        itr.resetStreams();

        itr = new IntegrationTestRunner( testDir.getAbsolutePath() );

        /*
         * Use the settings for this test, which contains the profile we're looking for.
         */
        List cliOptions = new ArrayList();
        cliOptions.add( "-s" );
        cliOptions.add( "\"" + new File( testDir, "settings.xml" ).getAbsolutePath() + "\"" );

        InvocationRequest r = new DefaultInvocationRequest()
            .setGoals( "org.apache.maven.its.mng3099:maven-mng3099-plugin:1:profile-props" )
            .setCliOptions( cliOptions )
            .setAutoclean( false );

        itr.invoke( r );

        List lines = itr.loadFile( new File( testDir, "log.txt" ), false );
        boolean found = false;
        for ( Iterator it = lines.iterator(); it.hasNext(); )
        {
            String line = (String) it.next();
            if ( line.indexOf( "local-profile-prop=local-profile-prop-value" ) > -1 )
            {
                found = true;
                break;
            }
        }

        if ( !found )
        {
            fail( "Profile-injected property value: local-profile-prop-value was not found in log output." );
        }
    }
}
