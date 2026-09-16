import org.gradle.api.publish.PublishingExtension

/**
 * Dual-publish repos matching computer.kolektiv.publishing
 * (gradle-conventions#1). Inline until the plugin is published.
 */
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
                val yuriUser = providers.environmentVariable("YURI_CAPITAL_REPO_USERNAME")
                val yuriPass = providers.environmentVariable("YURI_CAPITAL_REPO_PASSWORD")
                if (yuriUser.isPresent && yuriPass.isPresent) {
                    val snapshot = version.toString().endsWith("-SNAPSHOT", ignoreCase = true)
                    maven {
                        name = if (snapshot) "yuriSnapshots" else "yuriReleases"
                        url = uri(
                            if (snapshot) "https://repo.yuri.capital/repository/maven-snapshots/"
                            else "https://repo.yuri.capital/repository/maven-releases/"
                        )
                        credentials {
                            username = yuriUser.get()
                            password = yuriPass.get()
                        }
                    }
                }
            }
        }
    }
}
