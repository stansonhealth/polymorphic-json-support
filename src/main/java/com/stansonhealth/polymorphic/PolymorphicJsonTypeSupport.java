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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

public class PolymorphicJsonTypeSupport {

    public static ObjectMapper buildObjectMapper() {
        BasicPolymorphicTypeValidator basicPolymorphicTypeValidator = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType(new BasicPolymorphicTypeValidator.TypeMatcher() {
                    @Override
                    public boolean match(MapperConfig<?> config, Class<?> clazz) {
                        return true;
                    }
                })
                .build();
        return new ObjectMapper()
                .activateDefaultTyping(basicPolymorphicTypeValidator, ObjectMapper.DefaultTyping.NON_FINAL)
                .enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)
                .enable(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS);
    }

}
