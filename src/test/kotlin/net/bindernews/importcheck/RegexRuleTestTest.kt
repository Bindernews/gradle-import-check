package net.bindernews.importcheck

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class RegexRuleTestTest {

    @Test
    fun testDeny() {
        val rule = RegexRuleTest.fromString("- com.example.foo.*")
        val importSet = setOf(
            "com.example.Name",
            "com.example.foo.Bar",
        )
        assertEquals(RuleResult.DENY, rule.testClasses(importSet))
    }
}