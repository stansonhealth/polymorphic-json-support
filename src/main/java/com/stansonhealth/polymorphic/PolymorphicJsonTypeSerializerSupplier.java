package com.stansonhealth.polymorphic;

import static com.stansonhealth.polymorphic.PolymorphicJsonTypeSupport.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladmihalcea.hibernate.type.util.JsonSerializer;
import com.vladmihalcea.hibernate.type.util.JsonSerializerSupplier;
import com.vladmihalcea.hibernate.type.util.ObjectMapperWrapper;

public class PolymorphicJsonTypeSerializerSupplier implements JsonSerializerSupplier {

    private final PolymorphicSerializer polymorphicSerializer;

    public PolymorphicJsonTypeSerializerSupplier() {
        this.polymorphicSerializer = buildPolymorphicSerializer();
    }

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

