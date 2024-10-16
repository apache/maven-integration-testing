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
package org.apache.maven.its.mng8041;

import org.apache.maven.api.Node;
import org.apache.maven.api.PathScope;
import org.apache.maven.api.Project;
import org.apache.maven.api.Session;
import org.apache.maven.api.di.Inject;
import org.apache.maven.api.plugin.annotations.Mojo;

@Mojo(name = "static-tree", defaultPhase = "validate")
public class TestMojo implements org.apache.maven.api.plugin.Mojo {

    @Inject
    Project project;

    @Inject
    Session session;

    @Override
    public void execute() {
        Node node = session.collectDependencies(project, PathScope.MAIN_RUNTIME);
        display(node, "");
    }

    private void display(Node node, String prefix) {
        System.out.println(prefix + node);
        node.getChildren().forEach(c -> display(c, prefix + "  "));
    }
}
