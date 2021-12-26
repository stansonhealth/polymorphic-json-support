# Polymorphic Json Support
Adds Polymporphic Json Support to the [Hibernate Types](https://github.com/vladmihalcea/hibernate-types).
This allows a Class and it's descendants to be serialized into a Json Column.

## Usage
Just include the library into project.  Then for an Entity, define the Json type - there is
no special configuration required - the Json field will automatically be read from/written to
the column with the Class information - enabling polymorphism.

The key to "hooking" in this feature is the setup of the [hibernate.properties](src/main/resources/hibernate.properties).
