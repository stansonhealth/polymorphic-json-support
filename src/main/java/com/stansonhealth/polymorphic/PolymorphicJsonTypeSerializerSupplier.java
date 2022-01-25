/*
 * Copyright (c) 2022
 * Premier, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stansonhealth.polymorphic;

import static com.stansonhealth.polymorphic.PolymorphicJsonTypeSupport.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladmihalcea.hibernate.type.util.JsonSerializer;
import com.vladmihalcea.hibernate.type.util.JsonSerializerSupplier;
import com.vladmihalcea.hibernate.type.util.ObjectMapperWrapper;

public class PolymorphicJsonTypeSerializerSupplier implements JsonSerializerSupplier {

    static class PolymorphicSerializer implements JsonSerializer {
        private final ObjectMapperWrapper objectMapperWrapper;

        public PolymorphicSerializer(ObjectMapperWrapper objectMapperWrapper) {
            this.objectMapperWrapper = objectMapperWrapper;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T clone(T jsonObject) {
            return (T) this.objectMapperWrapper.fromBytes(
                    this.objectMapperWrapper.toBytes(jsonObject),
                    Object.class
            );
        }
    }
    private final PolymorphicSerializer polymorphicSerializer;

    public PolymorphicJsonTypeSerializerSupplier() {
        this.polymorphicSerializer = buildPolymorphicSerializer();
    }

    @Override
    public JsonSerializer get() {
        return this.polymorphicSerializer;
    }

    protected PolymorphicSerializer buildPolymorphicSerializer() {
        return new PolymorphicSerializer(
                new ObjectMapperWrapper(getObjectMapper())
        );
    }

    protected ObjectMapper getObjectMapper() {
        return buildObjectMapper();
    }
}

