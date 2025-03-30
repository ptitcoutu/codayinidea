package com.github.ptitcoutu.codayinidea.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "com.github.ptitcoutu.codayinidea.settings.CodaySettings",
    storages = [Storage("CodaySettings.xml")]
)
class CodaySettings : PersistentStateComponent<CodaySettings> {

    var codayPath: String = ""
    var connectToExistingCoday: Boolean = false

    override fun getState(): CodaySettings {
        return this
    }

    override fun loadState(state: CodaySettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(): CodaySettings {
            return service()
        }
    }
}