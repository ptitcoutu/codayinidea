package com.github.ptitcoutu.codayinidea.services

import com.github.ptitcoutu.codayinidea.CodaySettingsState
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader


@Service(Service.Level.PROJECT)
open class CodayProcessMediator(val project: Project,private val settingsState: CodaySettingsState = ApplicationManager.getApplication().getService(CodaySettingsState::class.java)) {

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

    open fun startProcess() {
        val settings = settingsState.state
        if (settings.connectToExistingCoday) {
            println("Connecting to an existing Coday instance...")
            // Implement logic to connect to an existing instance here
            if (checkCodayIsRunning()) {
                println("Coday is running")
            } else {
                println("Coday is not running")
            }

        } else {
            println("Starting a new Coday instance...")
            if(settings.codayPath.isNullOrEmpty()){
                println("No path to Coday executable specified, can't start Coday instance")
                return
            }
            val codayPath = settings.codayPath
            val codayDir = File(codayPath).parentFile.absolutePath
            if (!File(codayPath).exists()) {
                println("Coday executable not found at: $codayPath")
                return
            }

            try {
                val resInit = executeCommand(
                    "bash",
                    "-c",
                    "cd $codayDir; git pull --ff-only; yarn install --frozen-lock-file"
                )
                println("yarn install output: $resInit")
                val resCodayWeb = executeCommand(
                    "bash",
                    "-c",
                    "cd $codayDir; nohup yarn web",
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
    }

    private fun executeCommand(vararg command: String, waitCommandTermination: Boolean = true): String {
        val processBuilder = ProcessBuilder(*command)
        processBuilder.redirectErrorStream(true)
        val process: Process = processBuilder.start()
        return try {
            process.waitFor()
            val exitValue = process.exitValue()
            if (exitValue != 0) {
                println("error : ${exitValue}")
            }
            process.inputStream.bufferedReader().readText()
        } else {
             Thread.sleep(200)
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val errorReader = BufferedReader(InputStreamReader(process.errorStream))
            val output = reader.readText() + "\n" + errorReader.readText()
            output
        }
        }catch (e: IOException){
            return e.toString()
        }
    }
}