package net.bindernews.importcheck

import com.google.common.annotations.VisibleForTesting
import javassist.CtClass

class RegexRuleTest(
    override val description: String, private val regex: Regex, private val result: RuleResult
) : RuleTest {
    override fun test(cls: CtClass): RuleResult {
        return testClasses(cls.classFile2.constPool.classNames)
    }

    @VisibleForTesting
    internal fun testClasses(classNames: Set<String>): RuleResult {
        return if (classNames.any { s: Any? -> regex.matches(s as String) })
            result
        else
            RuleResult.IGNORE
    }

    companion object {
        @JvmStatic
        fun fromString(rule: String): RegexRuleTest {
            val s = rule.trim()
            val result = RuleResult.fromPrefix(s)
                .orElseThrow { RuleResult.invalidPrefixException(s[0]) }
            val trimmedRule = s.substring(1).trimStart()
            var s2 = trimmedRule.replace('.', '/')
            s2 = if (s2.endsWith("*")) {
                Regex.escape(s2.substring(0, s2.length - 1)) + ".+"
            } else {
                Regex.escape(s2)
            }
            return RegexRuleTest(trimmedRule, Regex(s2), result)
        }
    }
}