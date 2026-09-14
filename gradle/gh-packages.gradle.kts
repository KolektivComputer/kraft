import org.gradle.api.publish.PublishingExtension

subprojects {
    pluginManager.withPlugin("maven-publish") {
        extensions.configure<PublishingExtension>("publishing") {
            repositories {
                val ghActor = providers.environmentVariable("GITHUB_ACTOR")
                val ghToken = providers.environmentVariable("GITHUB_TOKEN")
                if (ghActor.isPresent && ghToken.isPresent) {
                    maven {
                        name = "GitHubPackages"
                        url = uri("https://maven.pkg.github.com/KolektivComputer/kraft")
                        credentials {
                            username = ghActor.get()
                            password = ghToken.get()
                        }
                    }
                }
            }
        }
    }
}
