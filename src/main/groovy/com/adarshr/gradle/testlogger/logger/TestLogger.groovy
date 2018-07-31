package com.adarshr.gradle.testlogger.logger

import org.gradle.api.tasks.testing.TestListener
import org.gradle.api.tasks.testing.TestOutputListener


interface TestLogger extends TestListener, TestOutputListener {
}
