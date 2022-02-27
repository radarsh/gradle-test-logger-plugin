# Changelog

## [v3.2.0](https://github.com/radarsh/gradle-test-logger-plugin/tree/v3.2.0) (2022-02-27)

[Full Changelog](https://github.com/radarsh/gradle-test-logger-plugin/compare/v3.1.0...v3.2.0)

**Implemented enhancements:**

- Add filter: hide regular passing tests, show slow passing tests [\#158](https://github.com/radarsh/gradle-test-logger-plugin/issues/158)

**Merged pull requests:**

- Bump byte-buddy from 1.12.6 to 1.12.7 [\#255](https://github.com/radarsh/gradle-test-logger-plugin/pull/255) ([dependabot[bot]](https://github.com/apps/dependabot))
- Bump com.gradle.enterprise from 3.8 to 3.8.1 [\#254](https://github.com/radarsh/gradle-test-logger-plugin/pull/254) ([dependabot[bot]](https://github.com/apps/dependabot))
- Bump com.gradle.plugin-publish from 0.18.0 to 0.19.0 [\#251](https://github.com/radarsh/gradle-test-logger-plugin/pull/251) ([dependabot[bot]](https://github.com/apps/dependabot))
- Bump com.gradle.enterprise from 3.7.2 to 3.8 [\#250](https://github.com/radarsh/gradle-test-logger-plugin/pull/250) ([dependabot[bot]](https://github.com/apps/dependabot))
- Bump byte-buddy from 1.12.2 to 1.12.6 [\#249](https://github.com/radarsh/gradle-test-logger-plugin/pull/249) ([dependabot[bot]](https://github.com/apps/dependabot))
- Bump com.gradle.enterprise from 3.4.1 to 3.7.2 [\#244](https://github.com/radarsh/gradle-test-logger-plugin/pull/244) ([dependabot[bot]](https://github.com/apps/dependabot))
- Bump byte-buddy from 1.11.22 to 1.12.2 [\#243](https://github.com/radarsh/gradle-test-logger-plugin/pull/243) ([dependabot[bot]](https://github.com/apps/dependabot))
- Bump com.gradle.plugin-publish from 0.17.0 to 0.18.0 [\#242](https://github.com/radarsh/gradle-test-logger-plugin/pull/242) ([dependabot[bot]](https://github.com/apps/dependabot))
- Bump com.gradle.plugin-publish from 0.16.0 to 0.17.0 [\#238](https://github.com/radarsh/gradle-test-logger-plugin/pull/238) ([dependabot[bot]](https://github.com/apps/dependabot))
- adds filter for showing only slow tests [\#207](https://github.com/radarsh/gradle-test-logger-plugin/pull/207) ([grimmjo](https://github.com/grimmjo))

## [v3.1.0](https://github.com/radarsh/gradle-test-logger-plugin/tree/v3.1.0) (2021-11-02)

[Full Changelog](https://github.com/radarsh/gradle-test-logger-plugin/compare/v3.0.0...v3.1.0)

**Implemented enhancements:**

- Disable all output in CI environment [\#224](https://github.com/radarsh/gradle-test-logger-plugin/issues/224)
- Indicate nested tests [\#146](https://github.com/radarsh/gradle-test-logger-plugin/issues/146)

**Fixed bugs:**

- NoSuchMethodError: org.gradle.api.provider.Provider.forUseAtConfigurationTime\(\) [\#225](https://github.com/radarsh/gradle-test-logger-plugin/issues/225)
- OutOfMemoryError while collecting test output even when disabled [\#213](https://github.com/radarsh/gradle-test-logger-plugin/issues/213)
- Top level class is not logged [\#204](https://github.com/radarsh/gradle-test-logger-plugin/issues/204)
- Hierarchical tests do not output all levels [\#141](https://github.com/radarsh/gradle-test-logger-plugin/issues/141)

**Closed issues:**

- Upgrade to Gradle 7.2 [\#226](https://github.com/radarsh/gradle-test-logger-plugin/issues/226)
- Add support for @ParameterizedTest with @MethodSource [\#125](https://github.com/radarsh/gradle-test-logger-plugin/issues/125)

**Merged pull requests:**

- Bump byte-buddy from 1.11.20 to 1.11.22 [\#236](https://github.com/radarsh/gradle-test-logger-plugin/pull/236) ([dependabot[bot]](https://github.com/apps/dependabot))
- Add Twitter badge [\#235](https://github.com/radarsh/gradle-test-logger-plugin/pull/235) ([radarsh](https://github.com/radarsh))
- Support nested tests [\#234](https://github.com/radarsh/gradle-test-logger-plugin/pull/234) ([radarsh](https://github.com/radarsh))
- Bump byte-buddy from 1.11.19 to 1.11.20 [\#231](https://github.com/radarsh/gradle-test-logger-plugin/pull/231) ([dependabot[bot]](https://github.com/apps/dependabot))
- Bump byte-buddy from 1.11.18 to 1.11.19 [\#230](https://github.com/radarsh/gradle-test-logger-plugin/pull/230) ([dependabot[bot]](https://github.com/apps/dependabot))
- Bump com.gradle.plugin-publish from 0.14.0 to 0.16.0 [\#229](https://github.com/radarsh/gradle-test-logger-plugin/pull/229) ([dependabot[bot]](https://github.com/apps/dependabot))
- Fix OutOfMemoryError due to output collector [\#228](https://github.com/radarsh/gradle-test-logger-plugin/pull/228) ([radarsh](https://github.com/radarsh))
- Upgrade to Gradle 7.2 [\#227](https://github.com/radarsh/gradle-test-logger-plugin/pull/227) ([radarsh](https://github.com/radarsh))
- Bump jansi from 2.3.2 to 2.3.4 [\#222](https://github.com/radarsh/gradle-test-logger-plugin/pull/222) ([dependabot-preview[bot]](https://github.com/apps/dependabot-preview))
- Upgrade to GitHub-native Dependabot [\#206](https://github.com/radarsh/gradle-test-logger-plugin/pull/206) ([dependabot-preview[bot]](https://github.com/apps/dependabot-preview))
- Bump com.github.kt3k.coveralls from 2.11.0 to 2.12.0 [\#202](https://github.com/radarsh/gradle-test-logger-plugin/pull/202) ([dependabot-preview[bot]](https://github.com/apps/dependabot-preview))
- Bump com.gradle.plugin-publish from 0.13.0 to 0.14.0 [\#201](https://github.com/radarsh/gradle-test-logger-plugin/pull/201) ([dependabot-preview[bot]](https://github.com/apps/dependabot-preview))

## [v3.0.0](https://github.com/radarsh/gradle-test-logger-plugin/tree/v3.0.0) (2021-04-01)

[Full Changelog](https://github.com/radarsh/gradle-test-logger-plugin/compare/v2.1.1...v3.0.0)

**Implemented enhancements:**

- Display start of the test [\#165](https://github.com/radarsh/gradle-test-logger-plugin/issues/165)

**Fixed bugs:**

- System property usage causes Gradle 7 to complain \(when using --configuration-cache\) [\#195](https://github.com/radarsh/gradle-test-logger-plugin/issues/195)
- Stack trace does not show application code [\#170](https://github.com/radarsh/gradle-test-logger-plugin/issues/170)
- Incompatible with Gradle Configuration Cache  [\#163](https://github.com/radarsh/gradle-test-logger-plugin/issues/163)
- evaluationDependsOnChildren\(\): No output at all \(Gradle 6.6 RC2\) [\#156](https://github.com/radarsh/gradle-test-logger-plugin/issues/156)

**Closed issues:**

- Upgrade to Gradle 6.7 [\#172](https://github.com/radarsh/gradle-test-logger-plugin/issues/172)
- Kotlin DSL [\#137](https://github.com/radarsh/gradle-test-logger-plugin/issues/137)
- Document TestLoggerExtension class [\#136](https://github.com/radarsh/gradle-test-logger-plugin/issues/136)

**Merged pull requests:**

- Document Kotlin DSL [\#200](https://github.com/radarsh/gradle-test-logger-plugin/pull/200) ([radarsh](https://github.com/radarsh))
- Document extension properties [\#199](https://github.com/radarsh/gradle-test-logger-plugin/pull/199) ([radarsh](https://github.com/radarsh))
- Bump objenesis from 3.1 to 3.2 [\#197](https://github.com/radarsh/gradle-test-logger-plugin/pull/197) ([dependabot-preview[bot]](https://github.com/apps/dependabot-preview))
- Bump jansi from 1.18 to 2.3.2 [\#196](https://github.com/radarsh/gradle-test-logger-plugin/pull/196) ([dependabot-preview[bot]](https://github.com/apps/dependabot-preview))
- Bump com.github.kt3k.coveralls from 2.9.0 to 2.11.0 [\#194](https://github.com/radarsh/gradle-test-logger-plugin/pull/194) ([dependabot-preview[bot]](https://github.com/apps/dependabot-preview))
- Bump byte-buddy from 1.10.17 to 1.10.22 [\#193](https://github.com/radarsh/gradle-test-logger-plugin/pull/193) ([dependabot-preview[bot]](https://github.com/apps/dependabot-preview))
- Bump com.gradle.plugin-publish from 0.12.0 to 0.13.0 [\#189](https://github.com/radarsh/gradle-test-logger-plugin/pull/189) ([dependabot-preview[bot]](https://github.com/apps/dependabot-preview))
- Fix for NPE when the exception in the TestResult is null. [\#180](https://github.com/radarsh/gradle-test-logger-plugin/pull/180) ([osh-onstructive](https://github.com/osh-onstructive))
- Bump byte-buddy from 1.10.1 to 1.10.17 [\#176](https://github.com/radarsh/gradle-test-logger-plugin/pull/176) ([dependabot-preview[bot]](https://github.com/apps/dependabot-preview))
- Bump commons-io from 2.6 to 2.8.0 [\#174](https://github.com/radarsh/gradle-test-logger-plugin/pull/174) ([dependabot-preview[bot]](https://github.com/apps/dependabot-preview))
- Upgrade to Gradle 6.7 [\#173](https://github.com/radarsh/gradle-test-logger-plugin/pull/173) ([radarsh](https://github.com/radarsh))
- Use lazy configuration [\#171](https://github.com/radarsh/gradle-test-logger-plugin/pull/171) ([radarsh](https://github.com/radarsh))

## [v2.1.1](https://github.com/radarsh/gradle-test-logger-plugin/tree/v2.1.1) (2020-10-14)

[Full Changelog](https://github.com/radarsh/gradle-test-logger-plugin/compare/v2.1.0...v2.1.1)

**Fixed bugs:**

- Cannot override logLevel using command line [\#161](https://github.com/radarsh/gradle-test-logger-plugin/issues/161)
- JetBrains IDEA prints the escape codes instead of switching colors [\#148](https://github.com/radarsh/gradle-test-logger-plugin/issues/148)

**Closed issues:**

- Upgrade to Gradle 6.6 [\#153](https://github.com/radarsh/gradle-test-logger-plugin/issues/153)
- Incorrect coverage reported by JaCoCo and Coveralls [\#152](https://github.com/radarsh/gradle-test-logger-plugin/issues/152)

**Merged pull requests:**

- Fix issue with overriding logLevel [\#169](https://github.com/radarsh/gradle-test-logger-plugin/pull/169) ([radarsh](https://github.com/radarsh))
- Upgrade to Gradle 6.6.1 [\#167](https://github.com/radarsh/gradle-test-logger-plugin/pull/167) ([radarsh](https://github.com/radarsh))
- Upgrade plugin publish plugin [\#155](https://github.com/radarsh/gradle-test-logger-plugin/pull/155) ([radarsh](https://github.com/radarsh))

## [v2.1.0](https://github.com/radarsh/gradle-test-logger-plugin/tree/v2.1.0) (2020-06-30)

[Full Changelog](https://github.com/radarsh/gradle-test-logger-plugin/compare/v2.0.0...v2.1.0)

**Implemented enhancements:**

- Auto Switch Theme based on --console value and --parallel [\#149](https://github.com/radarsh/gradle-test-logger-plugin/issues/149)

**Fixed bugs:**

- Theme is defaulted to plain even when maxForks is greater than 1 [\#139](https://github.com/radarsh/gradle-test-logger-plugin/issues/139)
- Plugin version 1.7.1 not working with Gradle 4.8.1  [\#138](https://github.com/radarsh/gradle-test-logger-plugin/issues/138)

**Closed issues:**

- Upgrade to Gradle 6.4.1 [\#150](https://github.com/radarsh/gradle-test-logger-plugin/issues/150)
- Upgrade to Gradle 6.2 [\#143](https://github.com/radarsh/gradle-test-logger-plugin/issues/143)
- Add support for kotlin-dsl [\#127](https://github.com/radarsh/gradle-test-logger-plugin/issues/127)

**Merged pull requests:**

- Upgrade to Gradle 6.4.1 [\#151](https://github.com/radarsh/gradle-test-logger-plugin/pull/151) ([radarsh](https://github.com/radarsh))
- Upgrade to Gradle 6.2 [\#144](https://github.com/radarsh/gradle-test-logger-plugin/pull/144) ([radarsh](https://github.com/radarsh))
- Seamlessly switch to parallel theme [\#142](https://github.com/radarsh/gradle-test-logger-plugin/pull/142) ([radarsh](https://github.com/radarsh))
- Add missing inputs to "functionalTest" task [\#140](https://github.com/radarsh/gradle-test-logger-plugin/pull/140) ([theosotr](https://github.com/theosotr))
- Configuration option for changing the log level [\#124](https://github.com/radarsh/gradle-test-logger-plugin/pull/124) ([rabidaudio](https://github.com/rabidaudio))

## [v2.0.0](https://github.com/radarsh/gradle-test-logger-plugin/tree/v2.0.0) (2019-10-10)

[Full Changelog](https://github.com/radarsh/gradle-test-logger-plugin/compare/v1.7.1...v2.0.0)

**Closed issues:**

- Upgrade versions of everything [\#132](https://github.com/radarsh/gradle-test-logger-plugin/issues/132)
- Upgrade to Gradle 5.6.2 [\#129](https://github.com/radarsh/gradle-test-logger-plugin/issues/129)
- Upgrade to Gradle 5.2 [\#102](https://github.com/radarsh/gradle-test-logger-plugin/issues/102)

**Merged pull requests:**

- Update issue templates [\#135](https://github.com/radarsh/gradle-test-logger-plugin/pull/135) ([radarsh](https://github.com/radarsh))
- Switch to modern Gradle dependency configurations [\#134](https://github.com/radarsh/gradle-test-logger-plugin/pull/134) ([radarsh](https://github.com/radarsh))
- Upgrade all dependency versions [\#133](https://github.com/radarsh/gradle-test-logger-plugin/pull/133) ([radarsh](https://github.com/radarsh))
- Remove references to Gradle 2.1 [\#131](https://github.com/radarsh/gradle-test-logger-plugin/pull/131) ([radarsh](https://github.com/radarsh))
- Upgrade to Gradle 5.6.2 [\#130](https://github.com/radarsh/gradle-test-logger-plugin/pull/130) ([radarsh](https://github.com/radarsh))
- Upgrade to Gradle 5.2 [\#107](https://github.com/radarsh/gradle-test-logger-plugin/pull/107) ([radarsh](https://github.com/radarsh))

## [v1.7.1](https://github.com/radarsh/gradle-test-logger-plugin/tree/v1.7.1) (2019-10-07)

[Full Changelog](https://github.com/radarsh/gradle-test-logger-plugin/compare/v1.7.0...v1.7.1)

**Closed issues:**

- Add example image of summary to readme [\#126](https://github.com/radarsh/gradle-test-logger-plugin/issues/126)
- Output containing testlogger tags can interfere with the produced logs [\#123](https://github.com/radarsh/gradle-test-logger-plugin/issues/123)
- Clarify documentation about new stack trace options [\#120](https://github.com/radarsh/gradle-test-logger-plugin/issues/120)
- Filtering standard stream doesn't seem to be working properly [\#114](https://github.com/radarsh/gradle-test-logger-plugin/issues/114)
- A couple usability suggestions [\#88](https://github.com/radarsh/gradle-test-logger-plugin/issues/88)

**Merged pull requests:**

- Ignore tag like expressions in standard streams [\#128](https://github.com/radarsh/gradle-test-logger-plugin/pull/128) ([radarsh](https://github.com/radarsh))
- Fix typo in README [\#122](https://github.com/radarsh/gradle-test-logger-plugin/pull/122) ([radarsh](https://github.com/radarsh))
- Clarify documentation [\#121](https://github.com/radarsh/gradle-test-logger-plugin/pull/121) ([radarsh](https://github.com/radarsh))

## [v1.7.0](https://github.com/radarsh/gradle-test-logger-plugin/tree/v1.7.0) (2019-05-29)

[Full Changelog](https://github.com/radarsh/gradle-test-logger-plugin/compare/v1.6.0...v1.7.0)

**Closed issues:**

- Not enough details about exception thrown by the test. [\#118](https://github.com/radarsh/gradle-test-logger-plugin/issues/118)
- NoSuchMethodError: org.fusesource.jansi.Ansi.fgGreen\(\)Lorg/fusesource/jansi/Ans [\#116](https://github.com/radarsh/gradle-test-logger-plugin/issues/116)
- Mocha on dark terminals [\#115](https://github.com/radarsh/gradle-test-logger-plugin/issues/115)
- Add support for @Nested and @DisplayName [\#106](https://github.com/radarsh/gradle-test-logger-plugin/issues/106)
- Get rid of logs original test logs [\#105](https://github.com/radarsh/gradle-test-logger-plugin/issues/105)
- Prints corrupted json [\#104](https://github.com/radarsh/gradle-test-logger-plugin/issues/104)
- Bad encoding on standard and mocha themes when printing to Eclipse's console [\#92](https://github.com/radarsh/gradle-test-logger-plugin/issues/92)
- Testsuite fatal error output not shown [\#89](https://github.com/radarsh/gradle-test-logger-plugin/issues/89)

**Merged pull requests:**

- Show full stacktraces [\#119](https://github.com/radarsh/gradle-test-logger-plugin/pull/119) ([radarsh](https://github.com/radarsh))
- Improve Windows build stability [\#117](https://github.com/radarsh/gradle-test-logger-plugin/pull/117) ([radarsh](https://github.com/radarsh))
- Show output from before System.exit\(5\) was called [\#113](https://github.com/radarsh/gradle-test-logger-plugin/pull/113) ([radarsh](https://github.com/radarsh))
- Configure build-scan plugin [\#112](https://github.com/radarsh/gradle-test-logger-plugin/pull/112) ([aalmiray](https://github.com/aalmiray))
- Use classDisplayName for friendlier suite names [\#109](https://github.com/radarsh/gradle-test-logger-plugin/pull/109) ([radarsh](https://github.com/radarsh))
- Fix double escaping of brackets [\#108](https://github.com/radarsh/gradle-test-logger-plugin/pull/108) ([radarsh](https://github.com/radarsh))

## [v1.6.0](https://github.com/radarsh/gradle-test-logger-plugin/tree/v1.6.0) (2018-11-20)

[Full Changelog](https://github.com/radarsh/gradle-test-logger-plugin/compare/v1.5.0...v1.6.0)

**Implemented enhancements:**

- Allow testlogger to be configured independently for each task [\#90](https://github.com/radarsh/gradle-test-logger-plugin/issues/90)

**Closed issues:**

- Test time reporting under the threshold [\#101](https://github.com/radarsh/gradle-test-logger-plugin/issues/101)
- Doesn't render tickmarks for mocha theme on Windows [\#99](https://github.com/radarsh/gradle-test-logger-plugin/issues/99)
- Add CompileStatic to more classes [\#97](https://github.com/radarsh/gradle-test-logger-plugin/issues/97)
- Print list of tests before execution [\#91](https://github.com/radarsh/gradle-test-logger-plugin/issues/91)
- JUnit5 Gradle Plugin interferes with this one [\#87](https://github.com/radarsh/gradle-test-logger-plugin/issues/87)
- Use of testLogger.showStandardStreams can lead to confusion [\#86](https://github.com/radarsh/gradle-test-logger-plugin/issues/86)

**Merged pull requests:**

- Add @CompileStatic to all classes [\#98](https://github.com/radarsh/gradle-test-logger-plugin/pull/98) ([radarsh](https://github.com/radarsh))
- Highlight lowercase extension name in documentation [\#96](https://github.com/radarsh/gradle-test-logger-plugin/pull/96) ([radarsh](https://github.com/radarsh))
- Clarify documentation about junit-platform Gradle plugin [\#95](https://github.com/radarsh/gradle-test-logger-plugin/pull/95) ([radarsh](https://github.com/radarsh))
- React to testLogging.showStandardStreams [\#94](https://github.com/radarsh/gradle-test-logger-plugin/pull/94) ([radarsh](https://github.com/radarsh))
- Configure testlogger for each task [\#93](https://github.com/radarsh/gradle-test-logger-plugin/pull/93) ([radarsh](https://github.com/radarsh))

## [v1.5.0](https://github.com/radarsh/gradle-test-logger-plugin/tree/v1.5.0) (2018-09-08)

[Full Changelog](https://github.com/radarsh/gradle-test-logger-plugin/compare/v1.4.0...v1.5.0)

**Implemented enhancements:**

- Feature: suppress logging output for successful tests [\#61](https://github.com/radarsh/gradle-test-logger-plugin/issues/61)
- Consider adding option to show failed only tests in output. [\#38](https://github.com/radarsh/gradle-test-logger-plugin/issues/38)

**Closed issues:**

- Upgrade to Gradle 4.10 [\#83](https://github.com/radarsh/gradle-test-logger-plugin/issues/83)
- Encapsulate TestResult and TestDescriptor along with all their associated methods [\#78](https://github.com/radarsh/gradle-test-logger-plugin/issues/78)
- Refresh screenshots [\#76](https://github.com/radarsh/gradle-test-logger-plugin/issues/76)
- Fix Gradle deprecation warnings [\#75](https://github.com/radarsh/gradle-test-logger-plugin/issues/75)
- Add TestNG based tests [\#74](https://github.com/radarsh/gradle-test-logger-plugin/issues/74)

**Merged pull requests:**

- Refresh screenshots [\#85](https://github.com/radarsh/gradle-test-logger-plugin/pull/85) ([radarsh](https://github.com/radarsh))
- Upgrade to Gradle 4.10 [\#84](https://github.com/radarsh/gradle-test-logger-plugin/pull/84) ([radarsh](https://github.com/radarsh))
- Add TestNG based tests [\#82](https://github.com/radarsh/gradle-test-logger-plugin/pull/82) ([radarsh](https://github.com/radarsh))
- Filter standard streams [\#81](https://github.com/radarsh/gradle-test-logger-plugin/pull/81) ([radarsh](https://github.com/radarsh))
- Resolve Gradle deprecation warnings [\#80](https://github.com/radarsh/gradle-test-logger-plugin/pull/80) ([radarsh](https://github.com/radarsh))
- Encapsulate core classes [\#79](https://github.com/radarsh/gradle-test-logger-plugin/pull/79) ([radarsh](https://github.com/radarsh))
- Filter test results by type [\#77](https://github.com/radarsh/gradle-test-logger-plugin/pull/77) ([radarsh](https://github.com/radarsh))

## [v1.4.0](https://github.com/radarsh/gradle-test-logger-plugin/tree/v1.4.0) (2018-07-31)

[Full Changelog](https://github.com/radarsh/gradle-test-logger-plugin/compare/v1.3.1...v1.4.0)

**Implemented enhancements:**

- Add support for parallel test execution [\#8](https://github.com/radarsh/gradle-test-logger-plugin/issues/8)

**Closed issues:**

- Introduce a high level abstraction for collecting standard stream output [\#72](https://github.com/radarsh/gradle-test-logger-plugin/issues/72)
- Fix CircleCI build instability [\#68](https://github.com/radarsh/gradle-test-logger-plugin/issues/68)
- Upgrade to Gradle 4.9 [\#67](https://github.com/radarsh/gradle-test-logger-plugin/issues/67)

**Merged pull requests:**

- Refactor standard stream collectors into OutputCollector abstraction [\#73](https://github.com/radarsh/gradle-test-logger-plugin/pull/73) ([radarsh](https://github.com/radarsh))
- Add test for ThemeType [\#71](https://github.com/radarsh/gradle-test-logger-plugin/pull/71) ([radarsh](https://github.com/radarsh))
- Upgrade to Gradle 4.9 [\#70](https://github.com/radarsh/gradle-test-logger-plugin/pull/70) ([radarsh](https://github.com/radarsh))
- Limit max memory available to Gradle for CircleCI builds [\#69](https://github.com/radarsh/gradle-test-logger-plugin/pull/69) ([radarsh](https://github.com/radarsh))
- Add support for parallel test execution [\#66](https://github.com/radarsh/gradle-test-logger-plugin/pull/66) ([radarsh](https://github.com/radarsh))

## [v1.3.1](https://github.com/radarsh/gradle-test-logger-plugin/tree/v1.3.1) (2018-06-25)

[Full Changelog](https://github.com/radarsh/gradle-test-logger-plugin/compare/v1.3.0...v1.3.1)

**Closed issues:**

- Bright yellow is not readable if console has bright background [\#59](https://github.com/radarsh/gradle-test-logger-plugin/issues/59)

**Merged pull requests:**

- Turn off bright yellow in standard theme [\#65](https://github.com/radarsh/gradle-test-logger-plugin/pull/65) ([radarsh](https://github.com/radarsh))

## [v1.3.0](https://github.com/radarsh/gradle-test-logger-plugin/tree/v1.3.0) (2018-06-11)

[Full Changelog](https://github.com/radarsh/gradle-test-logger-plugin/compare/v1.2.0...v1.3.0)

**Implemented enhancements:**

- Document system property overrides [\#63](https://github.com/radarsh/gradle-test-logger-plugin/issues/63)
- Allow overriding configuration by using system properties [\#40](https://github.com/radarsh/gradle-test-logger-plugin/issues/40)

**Fixed bugs:**

- Develop branch: No such property: displayName for class: org.gradle.api.internal.tasks.testing.DecoratingTestDescriptor [\#60](https://github.com/radarsh/gradle-test-logger-plugin/issues/60)

**Closed issues:**

- Upgrade to Gradle 4.7 [\#57](https://github.com/radarsh/gradle-test-logger-plugin/issues/57)

**Merged pull requests:**

- Document system property overrides [\#64](https://github.com/radarsh/gradle-test-logger-plugin/pull/64) ([radarsh](https://github.com/radarsh))
- Fix Gradle backward compatibility issue [\#62](https://github.com/radarsh/gradle-test-logger-plugin/pull/62) ([radarsh](https://github.com/radarsh))
- Upgrade to Gradle 4.7 [\#58](https://github.com/radarsh/gradle-test-logger-plugin/pull/58) ([radarsh](https://github.com/radarsh))
- Allow overriding of config using system properties [\#56](https://github.com/radarsh/gradle-test-logger-plugin/pull/56) ([radarsh](https://github.com/radarsh))
- Added support for nested JUnit5 tests. [\#55](https://github.com/radarsh/gradle-test-logger-plugin/pull/55) ([mithomas](https://github.com/mithomas))

## [v1.2.0](https://github.com/radarsh/gradle-test-logger-plugin/tree/v1.2.0) (2018-03-31)

[Full Changelog](https://github.com/radarsh/gradle-test-logger-plugin/compare/v1.1.2...v1.2.0)

**Implemented enhancements:**

- Slow test that failed doesn't show the execution time [\#51](https://github.com/radarsh/gradle-test-logger-plugin/issues/51)
- Add option to display STDOUT [\#50](https://github.com/radarsh/gradle-test-logger-plugin/issues/50)
- Are you planning to support jUnit 5? [\#47](https://github.com/radarsh/gradle-test-logger-plugin/issues/47)

**Closed issues:**

- Upgrade to Gradle 4.5.1 [\#48](https://github.com/radarsh/gradle-test-logger-plugin/issues/48)
- Upgrade to Gradle 4.4 [\#45](https://github.com/radarsh/gradle-test-logger-plugin/issues/45)
- Could not create an instance of type com.adarshr.gradle.testlogger.TestLoggerExtension\_Decorated. [\#44](https://github.com/radarsh/gradle-test-logger-plugin/issues/44)

**Merged pull requests:**

- Display standard streams [\#54](https://github.com/radarsh/gradle-test-logger-plugin/pull/54) ([radarsh](https://github.com/radarsh))
- JUnit5 support [\#53](https://github.com/radarsh/gradle-test-logger-plugin/pull/53) ([radarsh](https://github.com/radarsh))
- Show duration for failed tests [\#52](https://github.com/radarsh/gradle-test-logger-plugin/pull/52) ([radarsh](https://github.com/radarsh))
- Upgrade to Gradle 4.5.1 [\#49](https://github.com/radarsh/gradle-test-logger-plugin/pull/49) ([radarsh](https://github.com/radarsh))
- Upgrade to Gradle 4.4 [\#46](https://github.com/radarsh/gradle-test-logger-plugin/pull/46) ([radarsh](https://github.com/radarsh))

## [v1.1.2](https://github.com/radarsh/gradle-test-logger-plugin/tree/v1.1.2) (2017-11-07)

[Full Changelog](https://github.com/radarsh/gradle-test-logger-plugin/compare/v1.1.1...v1.1.2)

**Fixed bugs:**

- Erase ahead should be added to suite text [\#42](https://github.com/radarsh/gradle-test-logger-plugin/issues/42)

**Merged pull requests:**

- Erase ahead before writing every line [\#43](https://github.com/radarsh/gradle-test-logger-plugin/pull/43) ([radarsh](https://github.com/radarsh))

## [v1.1.1](https://github.com/radarsh/gradle-test-logger-plugin/tree/v1.1.1) (2017-11-06)

[Full Changelog](https://github.com/radarsh/gradle-test-logger-plugin/compare/v1.1.0...v1.1.1)

**Fixed bugs:**

- Summary doesn't fully overwrite existing lines [\#39](https://github.com/radarsh/gradle-test-logger-plugin/issues/39)

**Merged pull requests:**

- Erase ahead before writing summary text [\#41](https://github.com/radarsh/gradle-test-logger-plugin/pull/41) ([radarsh](https://github.com/radarsh))

## [v1.1.0](https://github.com/radarsh/gradle-test-logger-plugin/tree/v1.1.0) (2017-11-06)

[Full Changelog](https://github.com/radarsh/gradle-test-logger-plugin/compare/v1.0.1...v1.1.0)

**Implemented enhancements:**

- Mocha theme should more accurately represent Mocha's spec reporter [\#34](https://github.com/radarsh/gradle-test-logger-plugin/issues/34)
- Upgrade to Gradle 4.3 [\#30](https://github.com/radarsh/gradle-test-logger-plugin/issues/30)
- Use consistent status badges [\#26](https://github.com/radarsh/gradle-test-logger-plugin/issues/26)
- Perform code coverage analysis [\#25](https://github.com/radarsh/gradle-test-logger-plugin/issues/25)
- Print test duration for slow tests [\#18](https://github.com/radarsh/gradle-test-logger-plugin/issues/18)
- Post suite summaries [\#11](https://github.com/radarsh/gradle-test-logger-plugin/issues/11)

**Fixed bugs:**

- Specifying --console=plain doesn't have any effect if a theme is configured using the DSL [\#35](https://github.com/radarsh/gradle-test-logger-plugin/issues/35)
- Fix coverage badge [\#32](https://github.com/radarsh/gradle-test-logger-plugin/issues/32)
- Fix flakey tests caused by non-deterministic test execution order [\#23](https://github.com/radarsh/gradle-test-logger-plugin/issues/23)

**Merged pull requests:**

- Make mocha theme closer to mocha-spec reporter [\#37](https://github.com/radarsh/gradle-test-logger-plugin/pull/37) ([radarsh](https://github.com/radarsh))
- Show summary [\#36](https://github.com/radarsh/gradle-test-logger-plugin/pull/36) ([radarsh](https://github.com/radarsh))
- Fix coveralls badge [\#33](https://github.com/radarsh/gradle-test-logger-plugin/pull/33) ([radarsh](https://github.com/radarsh))
- Upgrade to Gradle 4.3 [\#31](https://github.com/radarsh/gradle-test-logger-plugin/pull/31) ([radarsh](https://github.com/radarsh))
- Perform code coverage analysis [\#29](https://github.com/radarsh/gradle-test-logger-plugin/pull/29) ([radarsh](https://github.com/radarsh))
- Update status badges [\#28](https://github.com/radarsh/gradle-test-logger-plugin/pull/28) ([radarsh](https://github.com/radarsh))
- Print test duration for slow tests [\#27](https://github.com/radarsh/gradle-test-logger-plugin/pull/27) ([radarsh](https://github.com/radarsh))
- Fix test instability [\#24](https://github.com/radarsh/gradle-test-logger-plugin/pull/24) ([radarsh](https://github.com/radarsh))

## [v1.0.1](https://github.com/radarsh/gradle-test-logger-plugin/tree/v1.0.1) (2017-10-15)

[Full Changelog](https://github.com/radarsh/gradle-test-logger-plugin/compare/v1.0.0...v1.0.1)

**Implemented enhancements:**

- Print failure reason and stacktrace [\#13](https://github.com/radarsh/gradle-test-logger-plugin/issues/13)
- Test logger should use test logger [\#12](https://github.com/radarsh/gradle-test-logger-plugin/issues/12)
- Add ability to hook it into additional Test type tasks [\#10](https://github.com/radarsh/gradle-test-logger-plugin/issues/10)

**Fixed bugs:**

- Do not print empty suites with no tests [\#19](https://github.com/radarsh/gradle-test-logger-plugin/issues/19)
- Error running with Gradle 4.1 \(and possibly Android's gradle plugin 3.0.0-beta7\) [\#14](https://github.com/radarsh/gradle-test-logger-plugin/issues/14)

**Closed issues:**

- Add AppVeyor build badge [\#15](https://github.com/radarsh/gradle-test-logger-plugin/issues/15)

**Merged pull requests:**

- Display failure reason [\#22](https://github.com/radarsh/gradle-test-logger-plugin/pull/22) ([radarsh](https://github.com/radarsh))
- Do not log empty suites [\#21](https://github.com/radarsh/gradle-test-logger-plugin/pull/21) ([radarsh](https://github.com/radarsh))
- Use plugin inside the plugin [\#20](https://github.com/radarsh/gradle-test-logger-plugin/pull/20) ([radarsh](https://github.com/radarsh))
- Add AppVeyor badge [\#17](https://github.com/radarsh/gradle-test-logger-plugin/pull/17) ([radarsh](https://github.com/radarsh))
- Hook into any task of type Test [\#16](https://github.com/radarsh/gradle-test-logger-plugin/pull/16) ([radarsh](https://github.com/radarsh))
- Add Configuration for AppVeyor [\#9](https://github.com/radarsh/gradle-test-logger-plugin/pull/9) ([baynezy](https://github.com/baynezy))

## [v1.0.0](https://github.com/radarsh/gradle-test-logger-plugin/tree/v1.0.0) (2017-09-29)

[Full Changelog](https://github.com/radarsh/gradle-test-logger-plugin/compare/577a89fd0a8f1621802527317ee0bcfbd0f3f49e...v1.0.0)

**Implemented enhancements:**

- Publish plugin [\#5](https://github.com/radarsh/gradle-test-logger-plugin/issues/5)
- Update documentation [\#4](https://github.com/radarsh/gradle-test-logger-plugin/issues/4)

**Fixed bugs:**

- Unicode characters don't display correctly on Windows [\#2](https://github.com/radarsh/gradle-test-logger-plugin/issues/2)

**Merged pull requests:**

- Publishing [\#7](https://github.com/radarsh/gradle-test-logger-plugin/pull/7) ([radarsh](https://github.com/radarsh))
- Update documentation [\#6](https://github.com/radarsh/gradle-test-logger-plugin/pull/6) ([radarsh](https://github.com/radarsh))
- Add support for special symbols on Windows [\#3](https://github.com/radarsh/gradle-test-logger-plugin/pull/3) ([radarsh](https://github.com/radarsh))
- Add theming support [\#1](https://github.com/radarsh/gradle-test-logger-plugin/pull/1) ([radarsh](https://github.com/radarsh))



\* *This Changelog was automatically generated by [github_changelog_generator](https://github.com/github-changelog-generator/github-changelog-generator)*
