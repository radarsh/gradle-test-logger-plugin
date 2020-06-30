# Contributing

Thanks for considering to contribute to gradle-test-logger-plugin. Here are the house rules:

## Branching model

The project uses Git flow. It's best to install the [Git flow extensions](https://github.com/nvie/gitflow) before you get started.

## Commit messages

Try to come up with short but meaningful commit messages in an imperative tone. [How to write a Git commit message](https://chris.beams.io/posts/git-commit/) is a great starting point.

## Testing

The project uses Groovy based [Spock Framework](http://spockframework.org/spock/docs/1.3/all_in_one.html) for both unit and functional tests. Spock is an extremely expressive tool for writing tests for code written in any JVM language.

## Formatting

The project uses [`.editorconfig`](https://editorconfig.org/) for setting some basic rules. The only additional thing we ask you to do is to make sure that statements after each label in Spock tests are indented using 4 spaces as follows:

```groovy
def "suite text"() {
    given:
        testDescriptorMock.classDisplayName >> 'ClassName'
    when:
        def actual = theme.suiteText(testDescriptorMock, testResultMock)
    then:
        actual == "ClassName${lineSeparator()}"
}
```

## Merging

Make sure that your rebase against develop first. Merging develop into a feature branch is discouraged. If you have several commits in your branch, squash them all into one commit as you rebase. 

If you have totally separate units of work going into the same branch, then it's OK to have these as separate commits. For example, if you have two commits "Implement feature x" and "Upgrade jansi version" on the same feature branch, it's OK to not squash them.

## Releasing

1. `git flow release start <new version>`
2. Edit gradle.properties to have the new version
3. Edit README.md to have the new version
4. Generate a new changelog. See [Generating a changelog](#generating-a-changelog) for more info.
5. Commit all files using `git commit -am 'Release version <new version>`
6. `git flow release finish <new version>`
7. Tag the new release `v<new version>`
8. Push to develop, master and tags using `git push origin develop && git push origin master && git push origin --tags`

### Generating a changelog

The project uses [GitHub changelog generator](https://github.com/github-changelog-generator/github-changelog-generator) to make the process of generating changelogs simpler. After installing it, run

```sh
github_changelog_generator --token <token> --user radarsh --project gradle-test-logger-plugin --enhancement-labels feature --unreleased-label <new version> --future-release v<new version>
```
