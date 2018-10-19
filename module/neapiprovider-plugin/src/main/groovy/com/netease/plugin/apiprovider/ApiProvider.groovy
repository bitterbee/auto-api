package com.netease.plugin.apiprovider

import com.android.build.gradle.AppPlugin
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

class ApiProvider implements Plugin<Project> {

    Project mProject
    Config mConfig

    @Override
    void apply(Project project) {

        mProject = project

        //判断是library还是application
        def hasAppPlugin = project.plugins.withType(AppPlugin)
        def variants = hasAppPlugin ? project.android.applicationVariants : project.android.libraryVariants

        //set config
        project.extensions.create('ApiProviderConfig', Config)
        mConfig = project.ApiProviderConfig

        def taskNames = project.gradle.startParameter.taskNames
        def isContainAssembleTask = false
        for (int index = 0; index < taskNames.size(); ++index) {
            def taskName = taskNames[index]

            println "---- ApiProvider Plugin taskname ${taskName} ----"

            if (taskName.contains("assemble") || taskName.contains("JavaWithJavac")) {
                isContainAssembleTask = true
                break
            }
        }

        //export build clean
//        if (!isContainAssembleTask) {
//            return
//        }

        project.afterEvaluate {
            variants.all { variant ->

                for (def task : project.tasks) {
                    println "---- ApiProvider Plugin taskname ${task.toString()} ----"
                }

                def compileJavaTask = project.tasks.getByName("compile${variant.name.capitalize()}JavaWithJavac")
                def apiTaskName = "ApiProvider${variant.name.capitalize()}"

                println "---- ApiProvider Plugin compileJavaTask ${compileJavaTask.toString()} ----"
                println "---- ApiProvider Plugin apiTask ${apiTaskName.toString()} ----"

                compileJavaTask.doLast {
                    println '---- ApiProvider Plugin Start ----'

                    def fromPath = "${project.projectDir}/build/generated/source/apt/debug/com/netease/libs/apiprovider/"
                    def toPath = "${project.rootProject.projectDir}/${mConfig.apiProjectPath}/src/api/java/com/netease/libs/apiprovider/"

                    def fromDir = new File("${fromPath}")
                    def toDir = new File("${toPath}")

                    if (!fromDir.exists()) {
                        println "---- ApiProvider Plugin Error ${fromPath} folder not exist"
                        return
                    }
                    if (!toDir.exists()) {
                        if (!toDir.mkdirs()) {
                            println "---- ApiProvider Plugin Error ${toPath} folder create error"
                            return
                        }
                    }

                    boolean success = false
                    try {
                        success = FileUtil.directoryCopy(fromPath, toPath)
                    } catch (IOException e) {
                        throw GradleException("---- ApiProvider Plugin Error ${e.toString()} ----")
                    }
                    if (!success) {
                        throw GradleException("---- ApiProvider Plugin Failed ----")
                    }

                    println '---- ApiProvider Plugin End ----'
                }

                //inject task
//                def apiTask = project.tasks.getByName(apiTaskName)
//                apiTask.dependsOn compileJavaTask.taskDependencies.getDependencies(compileJavaTask)
//                compileJavaTask.dependsOn apiTask
            }
        }
    }
}