package net.bindernews.importcheck

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.reflect.HasPublicType
import org.gradle.api.reflect.TypeOf
import org.gradle.api.tasks.TaskProvider

class ImportCheckPlugin : Plugin<Project> {
    override fun apply(target: Project) {}
}

