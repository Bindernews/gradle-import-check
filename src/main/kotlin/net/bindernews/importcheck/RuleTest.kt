package net.bindernews.importcheck

import javassist.CtClass

/**
 * The "runnable" form of a rule, checks if a given class has any imports that match the rule.
 */
interface RuleTest {
    /**
     * Check the class against this rule.
     */
    fun test(cls: CtClass): RuleResult

    /**
     * Description of this rule, for use in error messages.
     */
    val description: String

    companion object {
        @JvmStatic
        fun fromString(rule: String): RuleTest = RegexRuleTest.fromString(rule)
    }
}