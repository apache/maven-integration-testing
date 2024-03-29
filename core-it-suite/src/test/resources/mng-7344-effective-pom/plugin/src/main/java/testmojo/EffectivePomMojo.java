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
package testmojo;

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

import java.lang.reflect.Field;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "effective-pom", aggregator = true)
public class EffectivePomMojo extends AbstractMojo {

    @Parameter(defaultValue = "${reactorProjects}", required = true, readonly = true)
    private List<MavenProject> projects;

    public void execute() throws MojoExecutionException, MojoFailureException {
        projects.stream()
                .map(MavenProject::getDependencyManagement)
                .map(depMgt -> depMgt != null ? depMgt.getDependencies() : null)
                .filter(dependencies -> dependencies != null)
                .flatMap(List::stream)
                .forEach(dependency -> {
                    getLog().info("" + dependency);
                    try {
                        getLog().info(getImportedFrom(dependency));
                    } catch (Exception ex) {
                        getLog().error("Failed to get importedFrom");
                    }
                });
    }

    private String getImportedFrom(Dependency dependency) throws Exception {
        Field delegateField = Class.forName("org.apache.maven.model.BaseObject").getDeclaredField("delegate");
        delegateField.setAccessible(true);
        Object delegate = delegateField.get(dependency);
        delegateField.setAccessible(false);
        Object importedFrom = delegate.getClass().getMethod("getImportedFrom").invoke(delegate);

        return ("" + importedFrom).replaceAll("^.*[\\\\/]", "");
    }
}
