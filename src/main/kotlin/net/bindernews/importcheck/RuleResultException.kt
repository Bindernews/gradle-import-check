package net.bindernews.importcheck

import java.lang.RuntimeException

data class RuleResultException(
    val cls: String,
    val rule: String,
    val result: RuleResult
) : RuntimeException("class $cls failed rule $result '${rule}'")