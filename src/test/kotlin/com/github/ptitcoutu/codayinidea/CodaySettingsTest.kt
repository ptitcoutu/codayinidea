package com.github.ptitcoutu.codayinidea

import com.intellij.openapi.application.ApplicationManager
import com.intellij.testFramework.LightPlatformTestCase
import org.junit.Assert.assertEquals
import org.junit.Test

class CodaySettingsTest : LightPlatformTestCase() {

    @Test
    fun testCodaySettingsPersistence() {
        val application = ApplicationManager.getApplication()
        val settingsState = application.getService(CodaySettingsState::class.java)

        // Set new settings
        val newCodayPath = "/path/to/new/coday"
        val newConnectToExisting = true
        val newSettings = CodaySettings(newCodayPath, newConnectToExisting)
        settingsState.loadState(newSettings)

        // Retrieve the settings
        val loadedSettings = settingsState.state

        // Assert that the settings have been saved correctly
        assertEquals(newCodayPath, loadedSettings.codayPath)
        assertEquals(newConnectToExisting, loadedSettings.connectToExistingCoday)
    }
}