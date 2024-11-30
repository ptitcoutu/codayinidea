package com.github.ptitcoutu.codayinidea.services

import com.github.ptitcoutu.codayinidea.CodayInIdeaBundle
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class CodayInIdeaService(project: Project) {
    val codayProcessMediator: CodayProcessMediator
    init {
        thisLogger().info(CodayInIdeaBundle.message("projectService", project.name))
        codayProcessMediator = project.getService(CodayProcessMediator::class.java)
    }

    fun checkIfCodayIsRunning() {
        try {
            if (codayProcessMediator.checkCodayIsRunning()) {
                println("coday is running")
            } else {
                codayProcessMediator.startCodayService()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }




}
