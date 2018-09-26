package com.mercandalli.android.browser.screenshot

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class DemoModeRule : TestRule {

    private val helper = DemoModeEnabler()

    override fun apply(base: Statement, description: Description): Statement {
        return DemoModeStatement(base)
    }

    private inner class DemoModeStatement(private val base: Statement) : Statement() {
        override fun evaluate() {
            helper.enable()
            base.evaluate()
            helper.disable()
        }
    }
}