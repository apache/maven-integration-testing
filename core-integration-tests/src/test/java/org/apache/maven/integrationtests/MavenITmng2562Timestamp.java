package org.apache.maven.integrationtests;

import java.io.File;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.IntegrationTestRunner;

public class MavenITmng2562Timestamp extends AbstractMavenIntegrationTestCase {
	
	public MavenITmng2562Timestamp()
		throws InvalidVersionSpecificationException
	{
		super( "(2.0.9,)");
	}

    public void testitMNG2562() throws Exception {
        File testDir = extractTestResources(getClass(),
                "/mng-2562-timestamp");
        IntegrationTestRunner verifier = new IntegrationTestRunner(testDir.getAbsolutePath());
        verifier.executeGoal("verify");

        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}
