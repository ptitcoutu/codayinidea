package com.github.ptitcoutu.codayinidea.toolWindow

import com.github.ptitcoutu.codayinidea.services.CodayInIdeaService
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.jcef.JBCefBrowser


class CodayInIdeaWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val codayInIdeaWindow = CodayInIdeaWindow(toolWindow, project)
        val content = ContentFactory.getInstance().createContent(codayInIdeaWindow.content, null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class CodayInIdeaWindow(toolWindow: ToolWindow, project: Project) {

        private val service = toolWindow.project.service<CodayInIdeaService>()

        private val webView: JBCefBrowser by lazy {
            service.checkIfCodayIsRunning()
            JBCefBrowser(service.codayProcessMediator.codayUrl).also { browser ->
                Disposer.register(project, browser)
            }
        }

        val content = webView.component
    }
}
