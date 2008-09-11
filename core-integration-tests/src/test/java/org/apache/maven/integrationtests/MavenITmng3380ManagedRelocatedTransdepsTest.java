package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.IntegrationTestRunner;

/**
 * expected project.getArtifacts() results:
 * 
 * direct-dependency-groupId:direct-dependency-artifactId:jar:1:compile
 * transitive-dependency-new-groupId:transitive-dependency-artifactId:jar:2:compile
 * other-groupId:other-artifactId-a:jar:1:compile other-groupId:other-artifactId-b:jar:1:compile
 * 
 * org.apache.maven.project.MavenProject#.getArtifacts() is called with goal:
 * org.apache.maven.its:mng3380.plugin:mng-3380-test
 * 
 */
public class MavenITmng3380ManagedRelocatedTransdepsTest
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng3380ManagedRelocatedTransdepsTest()
        throws InvalidVersionSpecificationException
    {
        super( "(2.0.9,)" );
    }

    public void testitMNG3380()
        throws Exception
    {

        // compute test directory
        File testDir = extractTestResources( getClass(), "/mng-3380-managedRelocatedTransdeps" );

        IntegrationTestRunner itr = new IntegrationTestRunner( testDir.getAbsolutePath() );

        deleteArtifacts( itr );

        installDependencies( testDir );

        String path = testDir.getAbsolutePath() //
            + "/consumer";

        itr = new IntegrationTestRunner( path );
        itr.invoke( "package" );

        // verify no errors so far
        itr.verifyErrorFreeLog();
        itr.resetStreams();
    }

    private void installDependencies( File testDir )
        throws Exception
    {
        // install projects
        String path = testDir.getAbsolutePath() //
            + "/other-c";
        IntegrationTestRunner itr = new IntegrationTestRunner( path );
        itr.invoke( "install" );

        path = testDir.getAbsolutePath() //
            + "/other-b";
        itr = new IntegrationTestRunner( path );
        itr.invoke( "install" );

        path = testDir.getAbsolutePath() //
            + "/other-a";
        itr = new IntegrationTestRunner( path );
        itr.invoke( "install" );

        path = testDir.getAbsolutePath() //
            + "/transdep-old";
        itr = new IntegrationTestRunner( path );
        itr.invoke( "install" );

        path = testDir.getAbsolutePath() //
            + "/transdep-new-1";
        itr = new IntegrationTestRunner( path );
        itr.invoke( "install" );

        path = testDir.getAbsolutePath() //
            + "/transdep-new-2";
        itr = new IntegrationTestRunner( path );
        itr.invoke( "install" );

        path = testDir.getAbsolutePath() //
            + "/direct-dep";
        itr = new IntegrationTestRunner( path );
        itr.invoke( "install" );
    }

    private void deleteArtifacts( IntegrationTestRunner itr )
        throws Exception
    {
        // delete projects
        itr.deleteArtifact( //
                                 "other-groupId", //
                                 "other-artifactId-c", //
                                 "1", //
                                 "jar" );
        itr.deleteArtifact( //
                                 "other-groupId", //
                                 "other-artifactId-c", //
                                 "1", //
                                 "pom" );
        itr.assertArtifactNotPresent( //
                                           "other-groupId", //
                                           "other-artifactId-c", //
                                           "1", //
                                           "jar" );
        itr.assertArtifactNotPresent( //
                                           "other-groupId", //
                                           "other-artifactId-c", //
                                           "1", //
                                           "pom" );

        itr.deleteArtifact( //
                                 "other-groupId", //
                                 "other-artifactId-b", //
                                 "1", //
                                 "jar" );
        itr.deleteArtifact( //
                                 "other-groupId", //
                                 "other-artifactId-b", //
                                 "1", //
                                 "pom" );
        itr.assertArtifactNotPresent( //
                                           "other-groupId", //
                                           "other-artifactId-b", //
                                           "1", //
                                           "jar" );
        itr.assertArtifactNotPresent( //
                                           "other-groupId", //
                                           "other-artifactId-b", //
                                           "1", //
                                           "pom" );

        itr.deleteArtifact( //
                                 "other-groupId", //
                                 "other-artifactId-a", //
                                 "1", //
                                 "jar" );
        itr.deleteArtifact( //
                                 "other-groupId", //
                                 "other-artifactId-a", //
                                 "1", //
                                 "pom" );
        itr.assertArtifactNotPresent( //
                                           "other-groupId", //
                                           "other-artifactId-a", //
                                           "1", //
                                           "jar" );
        itr.assertArtifactNotPresent( //
                                           "other-groupId", //
                                           "other-artifactId-a", //
                                           "1", //
                                           "pom" );

        itr.deleteArtifact( //
                                 "transitive-dependency-old-groupId", //
                                 "transitive-dependency-artifactId", //
                                 "1", //
                                 "jar" );
        itr.deleteArtifact( //
                                 "transitive-dependency-old-groupId", //
                                 "transitive-dependency-artifactId", //
                                 "1", //
                                 "pom" );
        itr.assertArtifactNotPresent( //
                                           "transitive-dependency-old-groupId", //
                                           "transitive-dependency-artifactId", //
                                           "1", //
                                           "jar" );
        itr.assertArtifactNotPresent( //
                                           "transitive-dependency-old-groupId", //
                                           "transitive-dependency-artifactId", //
                                           "1", //
                                           "pom" );

        itr.deleteArtifact( //
                                 "transitive-dependency-new-groupId", //
                                 "transitive-dependency-artifactId", //
                                 "1", //
                                 "jar" );
        itr.deleteArtifact( //
                                 "transitive-dependency-new-groupId", //
                                 "transitive-dependency-artifactId", //
                                 "1", //
                                 "pom" );
        itr.assertArtifactNotPresent( //
                                           "transitive-dependency-new-groupId", //
                                           "transitive-dependency-artifactId", //
                                           "1", //
                                           "jar" );
        itr.assertArtifactNotPresent( //
                                           "transitive-dependency-new-groupId", //
                                           "transitive-dependency-artifactId", //
                                           "1", //
                                           "pom" );

        itr.deleteArtifact( //
                                 "transitive-dependency-new-groupId", //
                                 "transitive-dependency-artifactId", //
                                 "2", //
                                 "jar" );
        itr.deleteArtifact( //
                                 "transitive-dependency-new-groupId", //
                                 "transitive-dependency-artifactId", //
                                 "2", //
                                 "pom" );
        itr.assertArtifactNotPresent( //
                                           "transitive-dependency-new-groupId", //
                                           "transitive-dependency-artifactId", //
                                           "2", //
                                           "jar" );
        itr.assertArtifactNotPresent( //
                                           "transitive-dependency-new-groupId", //
                                           "transitive-dependency-artifactId", //
                                           "2", //
                                           "pom" );

        itr.deleteArtifact( //
                                 "direct-dependency-groupId", //
                                 "direct-dependency-artifactId", //
                                 "1", //
                                 "jar" );
        itr.deleteArtifact( //
                                 "direct-dependency-groupId", //
                                 "direct-dependency-artifactId", //
                                 "1", //
                                 "pom" );
        itr.assertArtifactNotPresent( //
                                           "direct-dependency-groupId", //
                                           "direct-dependency-artifactId", //
                                           "1", //
                                           "jar" );
        itr.assertArtifactNotPresent( //
                                           "direct-dependency-groupId", //
                                           "direct-dependency-artifactId", //
                                           "1", //
                                           "pom" );

        itr.deleteArtifact( //
                                 "root-groupId", //
                                 "root-artifactId", //
                                 "1", //
                                 "jar" );
        itr.deleteArtifact( //
                                 "root-groupId", //
                                 "root-artifactId", //
                                 "1", //
                                 "pom" );
        itr.assertArtifactNotPresent( //
                                           "root-groupId", //
                                           "root-artifactId", //
                                           "1", //
                                           "jar" );
        itr.assertArtifactNotPresent( //
                                           "root-groupId", //
                                           "root-artifactId", //
                                           "1", //
                                           "pom" );
    }
}
