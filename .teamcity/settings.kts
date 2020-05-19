import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.failureConditions.BuildFailureOnMetric
import jetbrains.buildServer.configs.kotlin.v2019_2.failureConditions.failOnMetricChange
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.VcsTrigger
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2019.2"

project {

    vcsRoot(HttpsGithubComMiteshDevopsMochaExamplesGitRefsHeadsMaster1)

    buildType(BuildTest2)

    subProject(KtsTesting)
}

object BuildTest2 : BuildType({
    name = "Build test 2"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        step {
            name = "install node"
            type = "jonnyzzz.nvm"
            param("version", "13.6.0")
        }
        script {
            name = "Run Test"
            workingDir = "packages/express-rest-api"
            scriptContent = """
                npm install
                npm install mocha-teamcity-reporter
                npm install nyc
                ./node_modules/.bin/nyc --reporter teamcity ./node_modules/.bin/mocha --reporter mocha-teamcity-reporter
            """.trimIndent()
        }
    }

    triggers {
        vcs {
            quietPeriodMode = VcsTrigger.QuietPeriodMode.USE_DEFAULT
            branchFilter = ""
        }
    }

    failureConditions {
        failOnMetricChange {
            metric = BuildFailureOnMetric.MetricType.COVERAGE_LINE_PERCENTAGE
            units = BuildFailureOnMetric.MetricUnit.PERCENTS
            comparison = BuildFailureOnMetric.MetricComparison.MORE
            compareTo = build {
                buildRule = lastFinished()
            }
        }
        failOnMetricChange {
            enabled = false
            metric = BuildFailureOnMetric.MetricType.COVERAGE_LINE_PERCENTAGE
            units = BuildFailureOnMetric.MetricUnit.DEFAULT_UNIT
            comparison = BuildFailureOnMetric.MetricComparison.DIFF
            compareTo = value()
        }
    }
})

object HttpsGithubComMiteshDevopsMochaExamplesGitRefsHeadsMaster1 : GitVcsRoot({
    name = "https://github.com/mitesh-devops/mocha-examples.git#refs/heads/master (1)"
    url = "https://github.com/mitesh-devops/mocha-examples.git"
    authMethod = password {
        userName = "mitesh-devops"
        password = "credentialsJSON:e7cd95f6-6f34-4e08-a818-0612697751f6"
    }
})


object KtsTesting : Project({
    name = "KTS Testing"
})
