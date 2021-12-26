import java.io.ByteArrayOutputStream
/*
 * This file was generated by the Gradle 'init' task.
 *
 * The settings file is used to specify which projects to include in your build.
 *
 * Detailed information about configuring a multi-project build in Gradle can be found
 * in the user manual at https://docs.gradle.org/7.0.2/userguide/multi_project_builds.html
 */

rootProject.name = "polymorphic-json-support"

System.setProperty("awsUser", startParameter.projectProperties["awsUser"] ?: "AWS")
System.setProperty("stansonMavenRepo", startParameter.projectProperties["stansonMavenRepo"] ?: "https://stansonhealth-862916455355.d.codeartifact.us-east-1.amazonaws.com/maven/stansonhealth/")
System.setProperty("awsAccountId", startParameter.projectProperties["awsAccountId"] ?: "862916455355")
System.setProperty("awsRegion", startParameter.projectProperties["awsRegion"] ?: "us-east-1")
System.setProperty("mavenPassword", startParameter.projectProperties["mavenPassword"] ?: getCodeArtifactPassword(System.getProperty("awsAccountId") as String))

fun getAuthorizationToken(env : Map<String, String>, domainOwner: String) : String {
    val ecrStdOut = ByteArrayOutputStream()
    val ecrStdErr = ByteArrayOutputStream()
    try {
        exec {
            environment(env)
            val args = mutableListOf(
                "aws",
                "codeartifact",
                "get-authorization-token",
                "--domain",
                "stansonhealth",
                "--domain-owner",
                domainOwner,
                "--query",
                "authorizationToken",
                "--output",
                "text"
            )
            if (startParameter.projectProperties["no-verify-ssl"] != null) {
                args.add("--no-verify-ssl")
            }
            commandLine = args
            standardOutput = ecrStdOut
            if (startParameter.projectProperties["show-aws-error"] == null) {
                errorOutput = ecrStdErr
            }
        }
    } catch (e : Exception) {
        // ignore errors as we"ll defer details to error output which can be
        // turned on via -Pshow-aws-error"
    }
    return ecrStdOut.toString()
}

fun getCodeArtifactPassword(awsAccountId : String) : String {
    return (System.getenv()["CODEARTIFACT_AUTH_TOKEN"]) ?:
    getAuthorizationToken(System.getenv(), awsAccountId)
}

