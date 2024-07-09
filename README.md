# Blade

A batteries-included and modular Minestom Network framework.

[!CAUTION]
This is extremely experimental and will probably not receive much future support. Use at your own risk.

---

### Adding as a dependency

```kotlin
repositories {
    maven("https://mvn.bladehunt.net/releases")
}

dependencies {
    // kotlinx.coroutines is required
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC")
    implementation("net.bladehunt:blade:0.1.0-alpha.1")
}
```

---

### Example

Visit [the example folder](/example) to view an example.

- Clone the repository: `git clone https://github.com/bladehuntmc/Blade`
- Run the example in IntelliJ
- Join on two Minecraft instances
- Enter the command `/join` on both. (Note: The instance takes time to load, so if entered too quickly, the game will
  start before fully loaded)