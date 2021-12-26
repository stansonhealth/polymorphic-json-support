package com.stansonhealth.polymorphic;

import static com.stansonhealth.polymorphic.PolymorphicJsonTypeSupport.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladmihalcea.hibernate.type.util.ObjectMapperSupplier;

public class PolymorphicTypeObjectMapperSupplier implements ObjectMapperSupplier  {
    @Override
    public ObjectMapper get() {
        return buildObjectMapper();
    }
}
