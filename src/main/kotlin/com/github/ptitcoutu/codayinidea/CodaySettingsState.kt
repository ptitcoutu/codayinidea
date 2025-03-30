package com.github.ptitcoutu.codayinidea

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "CodaySettingsState",
    storages = [Storage("coday.xml")]
)
class CodaySettingsState : PersistentStateComponent<CodaySettings> {
    var settings = CodaySettings()

    companion object {
        fun getInstance(): CodaySettingsState {
            return ApplicationManager.getApplication().service<CodaySettingsState>()
        }
    }

    override fun getState(): CodaySettings {
        return settings
    }

    override fun loadState(state: CodaySettings) {
        XmlSerializerUtil.copyBean(state, settings)
    }
}