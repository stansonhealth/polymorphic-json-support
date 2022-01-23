package com.stansonhealth.polymorphic

import groovy.transform.EqualsAndHashCode
import spock.lang.Specification

class PolymorphicJsonSpec extends Specification {

    @EqualsAndHashCode
    static class CloneableClass {

        String foo

        CloneableClass() {}

        CloneableClass(String foo) {
            this.foo = foo
        }
    }

    def "should return a clone when calling PolymorphicSerializer.clone()"() {
        given:
        def original = new CloneableClass("foobar")
        def supplier = new PolymorphicJsonTypeSerializerSupplier()
        def objectMapper = PolymorphicJsonTypeSupport.buildObjectMapper()

        when:
        def cloneJson = supplier.get().clone(objectMapper.writeValueAsString(original))
        def cloned = objectMapper.readValue(cloneJson, CloneableClass)

        then:
        cloned == original
    }

    def "should return an ObjectMapper from the PolymorphicTypeObjectMapperSupplier"() {
        when:
        def objectMapper = new PolymorphicTypeObjectMapperSupplier().get()

        then:
        objectMapper
    }

}
