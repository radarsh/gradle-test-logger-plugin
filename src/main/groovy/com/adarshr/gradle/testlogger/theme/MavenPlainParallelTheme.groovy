package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestResultWrapper
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

import static com.adarshr.gradle.testlogger.theme.ThemeType.MAVEN_PLAIN_PARALLEL

@CompileStatic
@InheritConstructors
class MavenPlainParallelTheme extends MavenPlainTheme {

    ThemeType type = MAVEN_PLAIN_PARALLEL

    @Override
    protected String suiteTextInternal(TestDescriptorWrapper descriptor) {
        ''
    }

    @Override
    protected String testTextInternal(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        super.testTextInternal("${descriptor.classDisplayName} ${descriptor.displayName} ${RESULT_TYPE_MAPPING[result.resultType]}", descriptor, result)
    }

    @Override
    protected String suiteStandardStreamTextInternal(String lines) {
        super.standardStreamTextInternal(lines, 2)
    }

    @Override
    protected String testStandardStreamTextInternal(String lines) {
        super.standardStreamTextInternal(lines, 2)
    }
}
