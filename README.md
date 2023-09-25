# Gradle Import Check

[GitHub](https://github.com/bindernews/gradle-import-check)

This plugin allow you to enforce rules about which packages and classes may
access other packages and classes at compile time. This relies on checking
the constant pool of the classes, so it doesn't handle any reflection.

This plugin is intended to help modders who are developing integrations
with other mods and want to ensure that code won't try to load classes
that may not exist at runtime.

The rules (`allow`, `deny`, `warn`) are applied in order. If you want
"parallel" rules, use multiple `check` blocks. 

Methods:
* `allow` - allows access to the given package/class, immediately passing the check
* `deny` - error on access to the given package/class
* `warn` - print a warning on access to the given package/class

## Example

```kotlin
import net.bindernews.importcheck.ImportCheckTask

tasks.register<ImportCheckTask>("importCheck") {
    // Scan classes in the output paths of the compile tasks
    classPath.from(tasks.compileJava.get().destinationDirectory)
    classPath.from(tasks.compileKotlin.get().destinationDirectory)
    check {
        // Scanning com.example and all sub-packages
        filter.packages.add("com.example")
        // Exclude com.example.foo and its sub-packages
        filter.excludedPackages.add("com.example.foo")
        // Exclude this specific class from the check
        filter.excludedClasses.add("com.example.OptionalDepIntegration")
        // Error when accessing the optional dependency
        deny("org.test.optional-foo.*")
    }
    check {
        // Scanning the foo package which references the optional dependency
        filter.packages.add("com.example.foo")
        // Print a warning using DeprecatedClass
        warn("org.test.optional-dep.DeprecatedClass")
    }
}

tasks.check {
    dependsOn("importCheck")
}
```

## Errata
Currently, this plugin only handles top-level classes (no nested or anonymous classes).
I'll fix that once I figure out a good syntax for it.