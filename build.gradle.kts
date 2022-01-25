import java.net.URI

plugins {
    `java-library`
    `maven-publish`
    signing
    groovy
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
    mavenLocal()
}

group = "com.stansonhealth"
version = "1.1"
java.sourceCompatibility = JavaVersion.VERSION_1_8

val publicationName = "polymorphicJsonSupport"
val isReleaseVersion: Boolean = !(version as String).endsWith("SNAPSHOT")
val ossrhUsername: String = findProperty("sonatypeUsername") as String
val ossrhPassword: String = findProperty("sonatypePassword") as String
val signingKey: String = findProperty("signingKey") as String
val signingPassword: String = findProperty("signingPassword") as String

dependencies {
    compileOnly("org.hibernate:hibernate-core:5.6.3.Final")
    api("com.vladmihalcea:hibernate-types-52:2.14.0")

    testImplementation("org.codehaus.groovy:groovy:3.0.8")
    testImplementation("org.spockframework:spock-core:2.0-groovy-3.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.hibernate:hibernate-core:5.6.3.Final")
}

tasks {

    val sourcesJar by registering(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }

    val javadocJar by registering(Jar::class) {
        dependsOn.add(javadoc)
        archiveClassifier.set("javadoc")
        from(javadoc)
    }

    jar {
        dependsOn.addAll(listOf(sourcesJar, javadocJar))
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    javadoc {
        setDestinationDir(File("${rootDir}/docs"))
    }

    test {
        useJUnitPlatform()
    }

    artifacts {
        archives(sourcesJar)
        archives(javadocJar)
        archives(jar)
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    repositories {
        maven {
            val releaseRepo = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotRepo = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            name = "OSSRH"
            url = URI(if (isReleaseVersion) releaseRepo else snapshotRepo)
            credentials {
                username = ossrhUsername
                password = ossrhPassword
            }
        }
    }
    publications {
        register(publicationName, MavenPublication::class) {
            from(components["java"])
            pom {
                groupId = project.group as String?
                name.set(project.name)
                description.set("Add jackson based polymorphic support to JSON Hibernate Type")
                url.set("https://github.com/stansonhealth/polymorphic-json-support")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("andrewhall")
                        name.set("Andrew Hall")
                        email.set("andrew_hall@premierinc.com")
                    }
                }
                scm {
                    connection.set("scm:git:git@github.com:stansonhealth/polymorphic-json-support.git")
                    developerConnection.set("scm:git:git@github.com:stansonhealth/polymorphic-json-supportgit")
                    url.set("https://github.com/stansonhealth/polymorphic-json-support")
                }
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications.getByName(publicationName))
}
