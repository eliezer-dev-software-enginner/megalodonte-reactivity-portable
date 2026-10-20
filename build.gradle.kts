plugins { `java-library`; `maven-publish` }
group = "megalodonte"
version = "1.0.0-beta"
repositories { mavenLocal(); mavenCentral() }
java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(25)) }
    withSourcesJar()
}
tasks.withType<JavaCompile>().configureEach { options.release.set(17) }
val sourceRoot = file("../megalodonte-reactivity/src/main/java")
val portablePaths = listOf(
    "megalodonte/ComputedState.java", "megalodonte/ListenerManager.java",
    "megalodonte/ForEachState.java", "megalodonte/v2/ListState.java"
)
val portableSources = tasks.register<Sync>("preparePortableSources") {
    doFirst {
        val missing = portablePaths.filterNot { sourceRoot.resolve(it).isFile }
        if (missing.isNotEmpty()) {
            throw GradleException("Missing portable reactivity sources in $sourceRoot: ${missing.joinToString()}")
        }
    }
    from("../megalodonte-reactivity/src/main/java") {
        include(portablePaths)
    }
    into(layout.buildDirectory.dir("generated/sources/portable"))
}
sourceSets.main { java.srcDir(portableSources) }
dependencies { api("megalodonte:megalodonte-core:1.0.0-beta") }
tasks.register<JavaExec>("portableTest") {
    dependsOn(tasks.testClasses)
    classpath = sourceSets.test.get().runtimeClasspath
    mainClass.set("megalodonte.PortableStateTest")
}
tasks.test {
    dependsOn("portableTest")
    failOnNoDiscoveredTests = false
}
tasks.check { dependsOn("portableTest") }
publishing { publications { create<MavenPublication>("mavenJava") { from(components["java"]) } } }
