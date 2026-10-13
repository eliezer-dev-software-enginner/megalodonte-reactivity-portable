# Megalodonte Reactivity Portable

This artifact publishes the platform-neutral sources of `megalodonte-reactivity`: `ComputedState`, `ListenerManager`, `ForEachState` and `v2.ListState`. Gradle prepares those sources at build time; the project does not maintain a second implementation.

It depends only on `megalodonte-core` and emits Java 17 bytecode. It excludes the JavaFX `Show` component, examples and the desktop module descriptor. Android supplies its own conditional rendering component.

```kotlin
implementation("megalodonte:megalodonte-reactivity-portable:1.0.0-beta")
```

Do not combine this artifact with desktop `megalodonte-reactivity` on one classpath: both contain classes with the same names. Close `ComputedState` and `ForEachState` when their owning view is disposed so dependency observations are released.

```sh
./gradlew check publishToMavenLocal
```
