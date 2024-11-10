package com.github.ptitcoutu.codayinidea.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
open class CodayProcessMediator(val project: Project) {
    open fun checkCodayIsRunning(): Boolean {
        val process = Runtime.getRuntime().exec(arrayOf("curl", "http://localhost:3000"))
        val exitCode = process.waitFor()

        return if (exitCode == 0) {
            val res = process.inputStream.bufferedReader().readText()
            res.contains("Coday")
        } else {
            val error = process.errorStream.bufferedReader().readText()
            println("Error executing curl: $error")
            false
        }
    }

    open fun startCodayService() {
        try {
            val process = Runtime.getRuntime().exec(
                arrayOf(
                    "bash",
                    "-c",
                    "cd ~/Workspace/biznet.io/app/coday; git pull --ff-only; yarn install; yarn web &"
                )
            )
            process.waitFor()
            var numberOfChecks = 0
            do {
                Thread.sleep(100)
                numberOfChecks++
            } while (!checkCodayIsRunning() && numberOfChecks < 50)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}