import java.net.URI

plugins {
    `java-library`
    `maven-publish`
    groovy
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
    mavenLocal()
}

group = "com.stansonhealth"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

dependencies {
    compileOnly("org.hibernate:hibernate-core:5.6.3.Final")
    api("com.vladmihalcea:hibernate-types-52:2.14.0")

    testImplementation("org.codehaus.groovy:groovy:3.0.7")
    testImplementation("org.spockframework:spock-core:2.0-M4-groovy-3.0")
    testImplementation("junit:junit:4.13.1")
    testImplementation("org.hibernate:hibernate-core:5.6.3.Final")
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

tasks {

    artifacts {
        archives(sourcesJar)
        archives(jar)
    }

}

publishing {
    val stansonMavenRepo: String by System.getProperties()
    val awsUser: String by System.getProperties()
    val mavenPassword: String by System.getProperties()
    repositories {
        maven {
            name = "ecr"
            url = URI(stansonMavenRepo)
            credentials {
                username = awsUser
                password = mavenPassword
            }
        }
    }
    publications {
        register("springProtect", MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar.get())
        }
    }
}


tasks.test {
    useJUnitPlatform()
}
