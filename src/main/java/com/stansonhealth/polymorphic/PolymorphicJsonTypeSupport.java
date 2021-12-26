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
