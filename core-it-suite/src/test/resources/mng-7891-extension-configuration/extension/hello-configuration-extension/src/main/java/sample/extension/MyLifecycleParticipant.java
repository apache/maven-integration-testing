package sample.extension;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.execution.MavenSession;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.logging.Logger;

@Named
@Singleton
public class MyLifecycleParticipant extends AbstractMavenLifecycleParticipant {

	private PlexusConfiguration configuration;
	private Logger logger;

	@Inject
	public MyLifecycleParticipant(
			@Named("sample.extension:hello-configuration-extension") PlexusConfiguration configuration, Logger logger) {
		this.configuration = configuration;
		this.logger = logger;
	}

	@Override
	public void afterSessionStart(MavenSession session) throws MavenExecutionException {
		PlexusConfiguration messages = configuration.getChild("messages");
		PlexusConfiguration sessionStart = messages.getChild("sessionStart");
		logger.info(sessionStart.getValue("No session start message configured"));
	}

	@Override
	public void afterProjectsRead(MavenSession session) throws MavenExecutionException {
		PlexusConfiguration messages = configuration.getChild("messages");
		PlexusConfiguration sessionStart = messages.getChild("projectsRead");
		logger.info(sessionStart.getValue("No session projects read message configured"));
	}

}
