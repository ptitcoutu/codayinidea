package com.github.ptitcoutu.codayinidea.config

import com.github.ptitcoutu.codayinidea.services.CodaySettings
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.TextFieldWithHistory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent

class CodaySettingsConfigurable(private val project: Project) : Configurable {
    private var codayPathField: TextFieldWithBrowseButton = TextFieldWithBrowseButton()
    private var codayUrlField: TextFieldWithHistory = TextFieldWithHistory()
    private val settings: CodaySettings get() = CodaySettings.getInstance(project)

    init {
        codayPathField.addBrowseFolderListener(
            "Select Coday Directory",
            "Choose the directory where Coday is installed",
            project,
            FileChooserDescriptor(false, true, false, false, false, false)
        )
    }

    override fun createComponent(): JComponent {
        val panel = JBPanel<JBPanel<*>>()
        return FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Coday Path:"), codayPathField)
            .addLabeledComponent(JBLabel("Coday Url:"), codayUrlField)
            .addComponentFillVertically(panel, 0)
            .panel
    }

    override fun isModified(): Boolean {
        return codayPathField.text != settings.getCodayPath() || codayUrlField.text != settings.getCodayUrl()
    }

    override fun apply() {
        settings.setCodayPath(codayPathField.text)
        settings.setCodayUrl(codayUrlField.text)
    }

    override fun reset() {
        codayPathField.text = settings.getCodayPath()
        codayUrlField.text = settings.getCodayUrl()
    }

    override fun getDisplayName(): String = "Coday Settings"
}