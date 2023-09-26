package net.bindernews.importcheck

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File


class BuildTest {
    private var testProjectDir: File = File("./test-project")

    @Test
    fun testAllowGood() {
        assertEquals(TaskOutcome.SUCCESS, gradleRunTask("testAllowGood"))
    }

    @Test
    fun testAllowFail() {
        assertEquals(TaskOutcome.FAILED, gradleRunTask("testAllowFail"))
    }

    @Test
    fun testDenyGood() {
        assertEquals(TaskOutcome.FAILED, gradleRunTask("testDenyGood"))
    }

    @Suppress("UnstableApiUsage")
    private fun gradleRunTask(taskName: String): TaskOutcome {
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments(taskName)
            .run()
        return result.task(":$taskName")!!.outcome
    }
}