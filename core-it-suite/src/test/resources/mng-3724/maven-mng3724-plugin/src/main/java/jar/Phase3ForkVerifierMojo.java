package jar;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
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

import java.util.Iterator;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

/**
 * @goal phase3
 * @execute phase="validate"
 */
public class Phase3ForkVerifierMojo
    extends AbstractMng2734Mojo
{
    /**
     * @parameter expression="${executedProject}"
     * @required
     */
    private MavenProject executedProject;

    public void execute()
        throws MojoExecutionException
    {
        List sourceRoots = executedProject.getCompileSourceRoots();

        String generatedDir = getGeneratorTargetDir().getAbsolutePath();

        boolean foundGeneratedDir = false;
        for ( Iterator it = sourceRoots.iterator(); it.hasNext(); )
        {
            String dir = (String) it.next();
            if ( generatedDir.equals( dir ) )
            {
                foundGeneratedDir = true;
                break;
            }
        }

        if ( !foundGeneratedDir )
        {
            throw new MojoExecutionException( "Generated-source target directory not found in compile source roots of the execution project!" );
        }
    }
}
