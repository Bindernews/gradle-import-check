package net.bindernews.importcheck

import javassist.ClassPool
import javassist.CtClass
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction
import java.net.URLClassLoader
import com.google.common.reflect.ClassPath as GClassPath

abstract class ImportCheckTask : DefaultTask() {
    /**
     * Set of classpaths to search for applicable classes
     */
    @get:SkipWhenEmpty
    @get:Classpath
    abstract val classPath: ConfigurableFileCollection

    @get:Input
    abstract val checks: SetProperty<ImportCheckSpec>

    @get:Input
    abstract var includeJreClasspath: Boolean

    private val ruleTestCache = hashMapOf<String, RuleTest>()

    /**
     * Create and configure a new import check.
     */
    fun check(a: Action<ImportCheckSpec>): ImportCheckSpec {
        val c = ImportCheckSpec.Impl()
        a.execute(c)
        checks.add(c)
        return c
    }

    @TaskAction
    fun checkRules() {
        // Build the classpath scanner from URLs
        val loaderUrls = classPath.map { it.toURI().toURL() }.toMutableList()
        val parentLoader = if (includeJreClasspath) {
            ClassLoader.getSystemClassLoader()
        } else {
            null
        }
        val loader = URLClassLoader(loaderUrls.toTypedArray(), parentLoader)
        val scanner = GClassPath.from(loader)

        // Build javassist ClassPool from same classpath
        val pool = ClassPool(true)
        classPath.forEach { pool.appendClassPath(it.absolutePath) }

        // For each rule: gather classes to check against, generate test list, and run tests.
        for (rule in checks.get()) {
            // Gather classes to check against
            val ruleClasses = gatherRuleClasses(scanner, rule)
            // Cache the rules so that we don't have to re-generate them
            for (s in rule.rules) {
                ruleTestCache.computeIfAbsent(s) { RuleTest.fromString(s) }
            }
            // Run tests
            try {
                val testList = rule.rules.map { ruleTestCache[it]!! }
                for (clsInfo in ruleClasses) {
                    testClass(pool.get(clsInfo.name), testList)
                }
            } catch (e: RuleResultException) {
                throw GradleException("access check failed:\n${e.message!!}")
            }
        }
    }

    /**
     * Apply the list of tests to the class, reporting warnings,
     * or throwing a [RuleResultException] in case of an error.
     */
    private fun testClass(cls: CtClass, testList: List<RuleTest>) {
        for (i in testList.indices) {
            when (val result = testList[i].test(cls)) {
                RuleResult.ALLOW -> {
                    // On allow, all further tests "pass"
                    return
                }
                RuleResult.DENY -> {
                    throw RuleResultException(cls.name, testList[i].description, result)
                }
                RuleResult.WARN -> {
                    val ex = RuleResultException(cls.name, testList[i].description, result)
                    logger.warn(ex.message!!)
                }
                RuleResult.IGNORE -> {}
            }
        }
    }

    /**
     * Returns all the classes the scanner found that are applicable to the check.
     */
    private fun gatherRuleClasses(scanner: GClassPath, checkSpec: ImportCheckSpec): Set<GClassPath.ClassInfo> {
        return scanner
            .allClasses
            .filter { it.isTopLevel && checkSpec.filter.isIncluded(it.packageName, it.simpleName) }
            .toHashSet()
    }
}
