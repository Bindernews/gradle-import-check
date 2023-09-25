package net.bindernews.importcheck

import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import java.util.Optional

enum class RuleResult {
    ALLOW,
    DENY,
    WARN,
    IGNORE;

    companion object {
        /**
         * Returns the [RuleResult] matching the first character of the string,
         * or throws an exception if the prefix is invalid.
         */
        @JvmStatic
        fun fromPrefix(s: String): Optional<RuleResult> {
            return Optional.ofNullable(when (s[0]) {
                '+' -> ALLOW
                '-' -> DENY
                '!' -> WARN
                else -> null
            })
        }

        /**
         * Returns an exception appropriate for when [fromPrefix] returns [Optional.EMPTY].
         */
        fun invalidPrefixException(prefix: Char): RuntimeException {
            return IllegalArgumentException("Invalid rule prefix '$prefix'")
        }
    }
}