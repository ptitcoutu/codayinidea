package com.github.ptitcoutu.codayinidea

import com.github.ptitcoutu.codayinidea.services.CodayInIdeaService
import com.github.ptitcoutu.codayinidea.services.CodayProcessMediator
import com.intellij.openapi.components.service
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import junit.framework.TestCase

class CodayInIdeaTest : BasePlatformTestCase() {

    fun testProjectService() {
        val projectService = project.service<CodayInIdeaService>()
        TestCase.assertNotNull(projectService.codayProcessMediator)
    }

    fun `test when project service check coday is running`() {
        // Given Coday is Running
        var startCodayServiceHasBeenCalled = false
        val mockedCodayProcessMediator = object : CodayProcessMediator(project) {
            override fun checkCodayIsRunning(): Boolean = true

            override fun startCodayService() {
                startCodayServiceHasBeenCalled = true
            }
        }


        // When checkIfCodayIsRunning is called
        val projectService = CodayInIdeaService(project, mockedCodayProcessMediator)
        projectService.checkIfCodayIsRunning()

        // Then startCodayService is called
        assertFalse(startCodayServiceHasBeenCalled)

    }

}