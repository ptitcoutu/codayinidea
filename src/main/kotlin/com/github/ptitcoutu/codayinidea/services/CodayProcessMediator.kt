package com.github.ptitcoutu.codayinidea.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import java.io.File

@Service(Service.Level.PROJECT)
open class CodayProcessMediator(val project: Project) {
    val codayUrl =  CodaySettings.getInstance(project).getCodayUrl()
    open fun checkCodayIsRunning(): Boolean {

        val process = Runtime.getRuntime().exec(arrayOf("curl", codayUrl))
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
            val codayPath = CodaySettings.getInstance(project).getCodayPath()
            val expandedPath = codayPath.replace("~", System.getProperty("user.home"))
            
            val resInit = executeCommand(
                "bash",
                "-c",
                "cd $expandedPath; git pull --ff-only; yarn install --frozen-lock-file"
            )
            println("yarn install output: $resInit")
            val resCodayWeb = executeCommand(
                "bash",
                "-c",
                "cd $expandedPath; nohup yarn web",
                waitCommandTermination = false
            )
            println("coday web output: $resCodayWeb")
            var numberOfChecks = 0
            do {
                Thread.sleep(100)
                numberOfChecks++
            } while (!checkCodayIsRunning() && numberOfChecks < 50)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun executeCommand(vararg command: String, waitCommandTermination: Boolean = true): String {
        val processBuilder = ProcessBuilder(*command)
        processBuilder.redirectErrorStream(true)
        val process = processBuilder.start()
        return if (waitCommandTermination) {
            process.waitFor()
            val exitValue = process.exitValue()
            if (exitValue != 0) {
                println("error : ${exitValue}")
            }
            process.inputStream.bufferedReader().readText()
        } else {
            Thread.sleep(200)
            val codayPath = CodaySettings.getInstance(project).getCodayPath()
            val expandedPath = codayPath.replace("~", System.getProperty("user.home"))
            File("$expandedPath/nohup.out").readText()
        }
    }
}