package net.bindernews.importcheck

import org.gradle.api.GradleException
import java.io.Serializable

interface ImportCheckSpec : Serializable {

    /**
     * Class filter specification.
     */
    val filter: ClassFilterSpec

    /**
     * Set of rules which each class will be checked against, in order.
     *
     * Rules start with a `+` to allow access, `-` to error on access, or `!` to warn on access.
     * The remainder of the rule is a class name or package ending in a `.*`. The first matching
     * rule will trigger a result, so different checks should be in different [ImportCheckSpec]s.
     *
     * This example allow access to anything in `com.example.util`, explicitly allows access
     * to `com.example.test.TestUtil`, disallows access to the package `com.example.test`, and
     * provides a warning when accessing the `com.example.inner` package.
     * ```
     * + com.example.util.*
     * + com.example.test.TestUtil
     * - com.example.test.*
     * ! com.example.inner.*
     * ```
     */
    var rules: ArrayList<String>

    /**
     * Add a rule, ensuring it has a proper prefix.
     */
    fun addRule(rule: String): ImportCheckSpec {
        // Check proper prefix
        RuleResult.fromPrefix(rule).orElseThrow {
            GradleException(RuleResult.invalidPrefixException(rule[0]).message!!)
        }
        rules.add(rule)
        return this
    }

    /**
     * Add a rule that allows access to the given package/class.
     */
    fun allow(rule: String): ImportCheckSpec {
        return addRule("+ $rule")
    }

    /**
     * Add a rule that denies access to the given package/class.
     */
    fun deny(rule: String): ImportCheckSpec {
        return addRule("- $rule")
    }

    /**
     * Add a rule that prints a warning but doesn't error, when detecting access to the given package/class.
     */
    fun warn(rule: String): ImportCheckSpec {
        return addRule("! $rule")
    }

    class Impl() : ImportCheckSpec {
        override var filter: ClassFilterSpec = ClassFilterSpec.Impl()
        override var rules: ArrayList<String> = arrayListOf()
    }
}