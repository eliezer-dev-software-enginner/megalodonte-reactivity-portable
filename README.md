# Megalodonte Reactivity Portable

This Java library packages the platform-neutral state utilities from `megalodonte-reactivity`: `ComputedState`, `ListenerManager`, `ForEachState` and `v2.ListState`. It supports derived values, observable lists and list-to-component reconciliation without the desktop rendering code. Gradle copies the four source files at build time; this repository does not maintain a second implementation.

It depends only on `megalodonte-core` and emits Java 17 bytecode. It excludes the JavaFX `Show` component, examples and the desktop module descriptor. Android supplies its own conditional rendering component.

## Build prerequisites

The included Gradle wrapper uses Gradle 9.2.0. The build requires a JDK 25 toolchain and produces Java 17 bytecode. Check out `megalodonte-reactivity` beside this repository: the `preparePortableSources` task reads its Java files from `../megalodonte-reactivity/src/main/java`. A standalone checkout without those files cannot build the intended artifact.

The build resolves `megalodonte:megalodonte-core:1.0.0-beta` from Maven Local or Maven Central. If it is unavailable in your configured repositories, check out `megalodonte-core` and publish it to Maven Local first:

```sh
cd ../megalodonte-core
./gradlew check publishToMavenLocal
cd ../megalodonte-reactivity-portable
./gradlew check publishToMavenLocal
```

Run these commands from the root of this repository. `check` runs the portable state checks; `publishToMavenLocal` publishes the library for local consumers.

## Use

After publishing the library locally, add `mavenLocal()` to the consuming Gradle build's repositories and declare:

```kotlin
implementation("megalodonte:megalodonte-reactivity-portable:1.0.0-beta")
```

For example, a `ComputedState` follows its declared dependencies:

```java
import megalodonte.ComputedState;
import megalodonte.base.state.State;

State<String> name = State.of("Ada");
ComputedState<Integer> length = ComputedState.of(() -> name.get().length(), name);
name.set("Grace");
System.out.println(length.get()); // 5
length.close();
```

Do not combine this artifact with desktop `megalodonte-reactivity` on one classpath: both contain classes with the same names. Close `ComputedState` and `ForEachState` when their owning view is disposed so dependency observations are released.

## License

No license file is present in this repository. Licensing terms require confirmation from the project maintainer.
