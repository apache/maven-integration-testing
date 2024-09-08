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
package org.apache.maven.its.mng8254.extension;

import java.util.List;

import org.apache.maven.api.di.Inject;
import org.apache.maven.api.di.Named;
import org.apache.maven.api.di.Priority;
import org.apache.maven.api.di.Singleton;
import org.apache.maven.api.model.Model;
import org.apache.maven.api.services.ModelBuilder;
import org.apache.maven.api.services.ModelBuilderException;
import org.apache.maven.api.services.ModelBuilderRequest;
import org.apache.maven.api.services.ModelBuilderResult;
import org.apache.maven.api.services.ModelTransformerContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Named
@Priority(42)
final class DumbModelBuilder implements ModelBuilder {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ModelBuilder delegate;

    @Inject
    public DumbModelBuilder(List<ModelBuilder> delegate) {
        this.delegate = delegate.stream()
                .filter(modelBuilder -> !(modelBuilder instanceof DumbModelBuilder))
                .findFirst()
                .get();
    }

    @Override
    public ModelBuilderResult build(ModelBuilderRequest request) throws ModelBuilderException {
        logger.warn("[MNG-8254] DumbModelBuilder Called from extension");
        return delegate.build(request);
    }

    @Override
    public ModelTransformerContextBuilder newTransformerContextBuilder() {
        return delegate.newTransformerContextBuilder();
    }

    @Override
    public Model buildRawModel(ModelBuilderRequest request) {
        return delegate.buildRawModel(request);
    }
}
