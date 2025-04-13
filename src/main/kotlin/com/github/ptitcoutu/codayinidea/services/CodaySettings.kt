package com.github.ptitcoutu.codayinidea.services

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project

@State(
    name = "CodaySettings",
    storages = [Storage("CodaySettings.xml")]
)
class CodaySettings : PersistentStateComponent<CodaySettings.State> {
    data class State(
        var codayPath: String = "~/Workspace/biznet.io/app/coday",
        var codayUrl: String = "http://localhost:3000"
    )

    private var codayConfigurationState = State()

    override fun getState(): State {
        return codayConfigurationState
    }

    override fun loadState(state: State) {
        codayConfigurationState = state
    }

    fun getCodayPath(): String {
        return codayConfigurationState.codayPath
    }

    fun setCodayPath(path: String) {
        codayConfigurationState.codayPath = path
    }

    fun getCodayUrl(): String {
        return codayConfigurationState.codayUrl
    }

    fun setCodayUrl(url: String) {
        codayConfigurationState.codayUrl = url
    }

    companion object {
        fun getInstance(project: Project): CodaySettings {
            return project.getService(CodaySettings::class.java)
        }
    }
}