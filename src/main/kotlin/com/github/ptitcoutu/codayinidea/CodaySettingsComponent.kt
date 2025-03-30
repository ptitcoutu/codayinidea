package com.github.ptitcoutu.codayinidea

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.options.ConfigurableUi
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.fields.ExtendableTextField
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent


class CodaySettingsComponent : ConfigurableUi<CodaySettings> {
    private val settingsState: CodaySettingsState =
        ApplicationManager.getApplication().getService(CodaySettingsState::class.java)

    private val codayPathField = ExtendableTextField()
    private val connectToExistingCodayCheckbox = JBCheckBox("Connect to existing Coday instance")
    private lateinit var mainPanel: DialogPanel

    init {

        codayPathField.emptyText.text = "Path to coday executable"
        mainPanel = panel {
            row("Coday Path:") {
                cell(codayPathField).resizableColumn().align(com.intellij.ui.dsl.builder.Align.FILL)
                browserButton("Select Coday Executable") {
                    val descriptor = FileChooserDescriptor(true, false, false, false, false, false)
                    val selectedFile = com.intellij.openapi.fileChooser.FileChooser.chooseFile(descriptor, null, null)
                    if (selectedFile != null) {
                        codayPathField.text = selectedFile.path
                    }
                }
            }
            row {
                cell(connectToExistingCodayCheckbox)
            }
        }
    }

    override fun isModified(): Boolean {
        val currentSettings = getSettings()
        return codayPathField.text != currentSettings.codayPath || connectToExistingCodayCheckbox.isSelected != currentSettings.connectToExistingCoday
    }

    override fun apply() {
        val currentSettings = getSettings()
        settingsState.loadState(currentSettings)
    }

    override fun reset() {
        val settings = settingsState.state
        codayPathField.text = settings.codayPath
        connectToExistingCodayCheckbox.isSelected = settings.connectToExistingCoday
    }

    override fun getComponent(): JComponent {
        return mainPanel
    }

    private fun getSettings(): CodaySettings {
        return CodaySettings(
            codayPath = codayPathField.text,
            connectToExistingCoday = connectToExistingCodayCheckbox.isSelected
        )
    }


}