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

    interface Fixture {}

    @EqualsAndHashCode
    static class TestFixtureA implements Fixture {}

    @EqualsAndHashCode
    static class TestFixtureB implements Fixture {}

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

    def "should clone a List of Interfaces"() {
        given:
        List<Fixture> fixtures = [
                new TestFixtureA(),
                new TestFixtureB()
        ]
        def supplier = new PolymorphicJsonTypeSerializerSupplier()
        def jsonSerializer = supplier.get()

        when:
        def result = jsonSerializer.clone(fixtures)

        then:
        result == fixtures
    }

}
