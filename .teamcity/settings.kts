import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.pullRequests
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.dockerCommand
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.projectFeatures.dockerRegistry
import jetbrains.buildServer.configs.kotlin.v2019_2.projectFeatures.gitlabConnection
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

    vcsRoot(Angularproject)
    vcsRoot(HttpsGithubComMiteshDevopsMochaExamplesGitRefsHeadsMaster1)
    vcsRoot(HttpsGitlabComMiteshITadcornerNodeUnitTestsGitRefsHeadsDevelop)
    vcsRoot(Develop)
    vcsRoot(HttpsGitlabComMiteshITadcornerTestingProjectGitRefsHeadsMaster)
    vcsRoot(HttpsGithubComMiteshDevopsExpressAppTestingDemoGitRefsHeadsMaster)
    vcsRoot(HttpsGithubComMiteshDevopsNodeUnitTestsGitRefsHeadsDevelop)
    vcsRoot(NewSshTest)
    vcsRoot(Cucumber)
    vcsRoot(HttpsGithubComMiteshDevopsIstanbulCodeCoverageExampleGitRefsHeadsMaster)

    buildType(Kuberetes)
    buildType(Kotlin)
    buildType(BuildTest)
    buildType(ExpressApp)
    buildType(CodeCoverageExample)

    features {
        gitlabConnection {
            id = "PROJECT_EXT_10"
            displayName = "GitLab.com"
            applicationId = "13326d3122a23dcac11653dce17f0da2f316bb1f356cea4f6177468f2533a261"
            clientSecret = "credentialsJSON:9e739afd-3358-4a42-95d4-29c444bffaec"
        }
        dockerRegistry {
            id = "PROJECT_EXT_9"
            name = "Mitesh Docker Credentials"
            url = "https://docker.io"
            userName = "miteshgangaramani"
            password = "credentialsJSON:46d285f9-990a-4a68-881c-4a87080b23aa"
        }
    }
}

object BuildTest : BuildType({
    name = "Build Test"

    params {
        param("IMAGE_TAG", "dev-%build.counter%")
        param("CLUSTER_NAME", "cluster-1")
        param("ENV", "dev")
        param("GCLOUD_KEY", """
            {
              "type": "service_account",
              "project_id": "sample-project-266811",
              "private_key_id": "fc11593a8fd7cd9e675385aed7d410409d9620ff",
              "private_key": "-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC8p7EmRcsZP4/M\nZJpeaTQlhdbmFWd+5WywL2RepGdQgHITNdoRM9jsaod5/rd7qN/Tl60RJiVuDLqw\nb8VG00aPwhquXcAU6LkwJGWqZS8aeo9Q7OLJMiz7VU5uSEWT08tJvWJe4iZQxYME\nRxOsZnHCii308U86vt1ndjsq+4UI00jfAyemiafOSMztP90Q252qq01rxjLbUHBd\nSts0hWxEhFnNUQj0cCIBOWld4Hm3PY7zSVf52jONs13Pe+YONUeelyNyCTj4H5sK\n3io9H/EG9WjEH9Uucrdr3z0/0MrfkJAvsPKoEu/1S7P6vaiSqOXQzFn2EIYH7DyZ\niZzmyjBZAgMBAAECggEAEXgy2JEydThUU9BGFjOGQ2pBI+W8A31xwt/kmrn9IjB+\nv8YnFxw2vG/B81tRuNcCwQiE23dtlGMD36Y9/ai/Wj0WdOqUCKujSHC6ZFzBz6El\nR9Bj0M8/aJe171LsdHuykbXCl/g3pZegogMF8Js88g+TIWE+HbnCbVv+rySC5XXo\nMNcAvD/fntRQzGDi2N6bpevJSMEcysRe/xbJ+KSSkDlMpwDPsCc7vk6azqQM6ehk\n5huN+VaViT0+UzFnUloEi0/2IW+amwS7tEjskU8trC2vENAFthVEdnGsFWCBUrqA\ndgdTH6aq6RdRcZGPdfHvc38S3OxujSk3n5w8nWdG4QKBgQD7nLyGElN3q1nJNS+n\nhwTbcbfHMu5kiettjfMAuBedVeUg3XgtfUGKZO/e1q3I6+NOl39DOQJjCx2MsQbT\nW7NnbZG5jef5PvFj3nff7H/voglIF6e6t+n6xoYUuG1vVSh64yPm9Owfi+rHnhMf\n1qKfD3LvEd4cLOaGldlDhra3CQKBgQC/8eXhbF1si4JAAFYuhnShBeFt9wUxduDM\nHvZzG7CxG0dezgSCSqESUe9elXZEFva0+WjIJHIoctwycEXTvQykZKykKUIpI7tZ\n/AU7kpQsfZLzzSWfmYP6kpeT8lRidZtm2UB8swtfCym+/PEFbZl9cDK1sW1rIK/w\nuEKsbacy0QKBgQCJN+BEiegFQmiT57KWfXbzql/cffUu1VyUga59vOf8ASUVOFLb\nFA1TSVrbv15FTgfIC6z23zdjBmPWH5EmYUNE66dmd3KEJoSEv4XGk/btnAPN0kDB\n/pzXC7+Bu1bLObj607b3tKi1R5wBwz6QxYAomMdIhAkKep451a0IO4aX6QKBgHu7\nrQvm9kaNnDYzwVqWlEJk7xAbJ/7d+kW7Q/WqCPnycsybk+6Fu1V6+gqH4Mg94WWC\nTdiPHgQTHv1owKqg+LBUsbHVYu/wN8rnGlxvvhL8FCoTucgrhxeVPh9CHMGnvLjN\nHbQFGzR2lr+mgJyWLVbeZ+IWcUmYtbJApcJPoXOxAoGAEwg++PEiOaUl23Li1Zme\nkH2Vnh6mDI8zIvqidNCjE1bGZjOh+xH8momdRiTnnoI/sJzpfM27KJ9G9LTCmY1M\nRYQKFGHzFb89u+KuIh5lBTH1UvY7AE5vt18fTzV49KDzgj6rTTdbmRZzYC6Pbw76\ncP62OrH4DKLZDTJnN06/9M4=\n-----END PRIVATE KEY-----\n",
              "client_email": "clustertestaccount@sample-project-266811.iam.gserviceaccount.com",
              "client_id": "116569351754267852501",
              "auth_uri": "https://accounts.google.com/o/oauth2/auth",
              "token_uri": "https://oauth2.googleapis.com/token",
              "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
              "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/clustertestaccount%40sample-project-266811.iam.gserviceaccount.com"
            }
        """.trimIndent())
        param("PROJECT_ID", "sample-project-266811")
    }

    vcs {
        root(HttpsGitlabComMiteshITadcornerNodeUnitTestsGitRefsHeadsDevelop)
    }

    steps {
        script {
            name = "random test"
            enabled = false
            scriptContent = "service docker status"
        }
        step {
            name = "install dependency"
            type = "jonnyzzz.nvm"
            enabled = false
            param("version", "13.6.0")
        }
        step {
            name = "Install modules and run test"
            type = "jonnyzzz.npm"
            enabled = false
            param("npm_commands", "install")
        }
        dockerCommand {
            name = "Docker Build image"
            enabled = false
            executionMode = BuildStep.ExecutionMode.RUN_ON_FAILURE
            commandType = build {
                source = file {
                    path = "Dockerfile"
                }
                namesAndTags = "gcr.io/%PROJECT_ID%/node-app:dev-%build.counter%"
                commandArgs = "--pull"
            }
            param("dockerImage.platform", "linux")
        }
        script {
            name = "service account authentication"
            enabled = false
            scriptContent = """
                echo '%GCLOUD_KEY%' > ./new-file.json
                gcloud auth activate-service-account --key-file=./new-file.json --project=%PROJECT_ID%
                gcloud container clusters get-credentials %CLUSTER_NAME% --zone us-central1-c --project %PROJECT_ID%
                kubectl config current-context
                kubectl config set-context --current --namespace=mitesh
                gcloud auth configure-docker
            """.trimIndent()
        }
        dockerCommand {
            name = "Docker Push"
            enabled = false
            commandType = push {
                namesAndTags = "gcr.io/%PROJECT_ID%/node-app:dev-%build.counter%"
                removeImageAfterPush = false
            }
        }
        script {
            name = "deploy image to kubernetes"
            enabled = false
            scriptContent = "IMAGETAG='%IMAGE_TAG%' node deploy-apps.js"
        }
    }

    triggers {
        vcs {
            enabled = false
            branchFilter = ""
        }
    }

    features {
        pullRequests {
            enabled = false
            vcsRootExtId = "MiteshTest_HttpsGitlabComMiteshITadcornerNodeUnitTestsGitRefsHeadsMaster"
            provider = gitlab {
                authType = token {
                    token = "credentialsJSON:95e1a01e-c69f-4d30-8fc2-2e6ee1778eb1"
                }
                filterTargetBranch = "[+|-:]*"
            }
        }
        commitStatusPublisher {
            enabled = false
            vcsRootExtId = "${HttpsGitlabComMiteshITadcornerNodeUnitTestsGitRefsHeadsDevelop.id}"
            publisher = gitlab {
                gitlabApiUrl = "https://gitlab.com/api/v4"
                accessToken = "credentialsJSON:95e1a01e-c69f-4d30-8fc2-2e6ee1778eb1"
            }
        }
    }
})

object CodeCoverageExample : BuildType({
    name = "Code Coverage Example"

    enablePersonalBuilds = false
    artifactRules = """
        coverage/lcov-report/** => coverage.zip
        spec/unit/** => spec.zip
    """.trimIndent()
    maxRunningBuilds = 1

    vcs {
        root(NewSshTest)

        showDependenciesChanges = true
    }

    steps {
        step {
            name = "Npm test"
            type = "jonnyzzz.npm"
            param("npm_commands", "install")
        }
        step {
            name = "install dependency"
            type = "jonnyzzz.nvm"
            enabled = false
            param("version", "13.6.0")
        }
        step {
            name = "Run Test"
            type = "jonnyzzz.npm"
            param("npm_commands", """
                install
                install express
                run test
                run test-cover
                npm install nyc
                ./node_modules/.bin/nyc --reporter teamcity
            """.trimIndent())
        }
    }

    triggers {
        vcs {
        }
    }
})

object ExpressApp : BuildType({
    name = "Express App"

    artifactRules = "coverage/** => coverage.zip"

    params {
        param("build", "%teamcity.build.branch%-%build.counter%")
        param("teamcity.vcsTrigger.runBuildInNewEmptyBranch", "false")
    }

    vcs {
        root(Cucumber)
    }

    steps {
        step {
            name = "Run Test"
            type = "jonnyzzz.npm"
            param("npm_commands", """
                install
                test
            """.trimIndent())
        }
        script {
            name = "testing"
            scriptContent = """
                npm install cucumber-teamcity-formatter --save-dev
                ./node_modules/.bin/cucumber-js --format node_modules/cucumber-teamcity-formatter
            """.trimIndent()
        }
    }

    triggers {
        vcs {
            perCheckinTriggering = true
            enableQueueOptimization = false
        }
    }

    features {
        pullRequests {
            enabled = false
            vcsRootExtId = "${HttpsGitlabComMiteshITadcornerTestingProjectGitRefsHeadsMaster.id}"
            provider = gitlab {
                authType = token {
                    token = "credentialsJSON:c4ec1430-4f58-4ecc-b522-bc428f12ca5a"
                }
            }
        }
        commitStatusPublisher {
            vcsRootExtId = "${HttpsGitlabComMiteshITadcornerTestingProjectGitRefsHeadsMaster.id}"
            publisher = gitlab {
                gitlabApiUrl = "https://gitlab.com/api/v4"
                accessToken = "credentialsJSON:c4ec1430-4f58-4ecc-b522-bc428f12ca5a"
            }
        }
    }
})

object Kotlin : BuildType({
    name = "kotlin"

    vcs {
        root(DslContext.settingsRoot)
    }

    triggers {
        vcs {
        }
    }
})

object Kuberetes : BuildType({
    name = "Kuberetes"

    steps {
        script {
            name = "kubectl command"
            workingDir = "/root"
            scriptContent = """
                gcloud components list
                kubectl cluster-info
                kubectl apply -f https://k8s.io/examples/controllers/nginx-deployment.yaml
            """.trimIndent()
        }
    }
})

object Angularproject : GitVcsRoot({
    name = "angularproject"
    url = "https://github.com/mitesh-devops/angulartest.git"
    authMethod = password {
        userName = "mitesh.gangaramani@rapidops.com"
        password = "credentialsJSON:e7cd95f6-6f34-4e08-a818-0612697751f6"
    }
})

object Cucumber : GitVcsRoot({
    name = "cucumber"
    url = "https://github.com/mitesh-devops/cucumber-teamcity-formatter.git"
    branchSpec = "+:refs/heads/*"
})

object Develop : GitVcsRoot({
    name = "Develop"
    url = "https://gitlab.com/Mitesh_ITadcorner/node-unit-tests.git"
    branch = "refs/heads/develop"
    authMethod = password {
        userName = "Mitesh_ITadcorner"
        password = "credentialsJSON:1f51d04b-2d22-407e-bce6-d398a683ae91"
    }
})

object HttpsGithubComMiteshDevopsExpressAppTestingDemoGitRefsHeadsMaster : GitVcsRoot({
    name = "https://github.com/mitesh-devops/express-app-testing-demo.git#refs/heads/master"
    url = "https://github.com/mitesh-devops/express-app-testing-demo.git"
    authMethod = password {
        userName = "mitesh-devops"
        password = "credentialsJSON:e7cd95f6-6f34-4e08-a818-0612697751f6"
    }
})

object HttpsGithubComMiteshDevopsIstanbulCodeCoverageExampleGitRefsHeadsMaster : GitVcsRoot({
    name = "https://github.com/mitesh-devops/istanbul-code-coverage-example.git#refs/heads/master"
    url = "https://github.com/mitesh-devops/istanbul-code-coverage-example.git"
    authMethod = password {
        userName = "mitesh-devops"
        password = "credentialsJSON:e7cd95f6-6f34-4e08-a818-0612697751f6"
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

object HttpsGithubComMiteshDevopsNodeUnitTestsGitRefsHeadsDevelop : GitVcsRoot({
    name = "https://github.com/mitesh-devops/node-unit-tests.git#refs/heads/develop"
    url = "https://github.com/mitesh-devops/node-unit-tests.git"
    branch = "refs/heads/develop"
    authMethod = password {
        userName = "mitesh-devops"
        password = "credentialsJSON:e7cd95f6-6f34-4e08-a818-0612697751f6"
    }
})

object HttpsGitlabComMiteshITadcornerNodeUnitTestsGitRefsHeadsDevelop : GitVcsRoot({
    name = "https://gitlab.com/Mitesh_ITadcorner/node-unit-tests.git#refs/heads/develop"
    url = "https://gitlab.com/Mitesh_ITadcorner/node-unit-tests.git"
    branch = "refs/heads/develop"
    authMethod = password {
        userName = "Mitesh_ITadcorner"
        password = "credentialsJSON:46d285f9-990a-4a68-881c-4a87080b23aa"
    }
})

object HttpsGitlabComMiteshITadcornerTestingProjectGitRefsHeadsMaster : GitVcsRoot({
    name = "https://gitlab.com/Mitesh_ITadcorner/testing-project.git#refs/heads/master"
    url = "https://gitlab.com/Mitesh_ITadcorner/testing-project.git"
    authMethod = password {
        userName = "Mitesh_ITadcorner"
        password = "credentialsJSON:46d285f9-990a-4a68-881c-4a87080b23aa"
    }
})

object NewSshTest : GitVcsRoot({
    name = "New ssh Test"
    url = "https://gitlab.com/Mitesh_ITadcorner/node-unit-tests.git"
    authMethod = password {
        userName = "Mitesh_ITadcorner"
        password = "credentialsJSON:46d285f9-990a-4a68-881c-4a87080b23aa"
    }
})
