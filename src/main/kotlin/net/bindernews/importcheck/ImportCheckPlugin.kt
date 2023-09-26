package net.bindernews.importcheck

import org.gradle.api.Plugin
import org.gradle.api.Project

class ImportCheckPlugin : Plugin<Project> {

//    private var importCheckExt: NamedDomainObjectContainer<ImportCheckSpec>? = null

    override fun apply(project: Project) {
//        importCheckExt = project.container(ImportCheckSpec::class.java) {
//            name -> ImportCheckSpec.Impl(name)
//        }
//        project.extensions.add("importCheck", importCheckExt!!)
//
//        project.tasks.register("allImportChecks", ImportCheckTask::class.java) {}
    }
}

