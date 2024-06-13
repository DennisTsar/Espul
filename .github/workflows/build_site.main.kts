#!/usr/bin/env kotlin
@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:2.1.0")
@file:Repository("https://github-workflows-kt-bindings.colman.com.br/binding/")
@file:DependsOn("actions:checkout:v4")
@file:DependsOn("actions:setup-java:v4")
@file:DependsOn("actions:upload-pages-artifact:v3")
@file:DependsOn("actions:deploy-pages:v4")
@file:DependsOn("gradle:actions__setup-gradle:v3")

import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.actions.DeployPages
import io.github.typesafegithub.workflows.actions.actions.SetupJava
import io.github.typesafegithub.workflows.actions.actions.UploadPagesArtifact
import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradle
import io.github.typesafegithub.workflows.domain.Concurrency
import io.github.typesafegithub.workflows.domain.Environment
import io.github.typesafegithub.workflows.domain.Mode
import io.github.typesafegithub.workflows.domain.Permission
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.domain.triggers.WorkflowDispatch
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.ConsistencyCheckJobConfig

workflow(
    name = "Deploy Site to Pages",
    on = listOf(
        WorkflowDispatch(),
        Push(
            branches = listOf("master"),
        )
    ),
    sourceFile = __FILE__,
    targetFileName = "build_and_deploy_site.yml",
    consistencyCheckJobConfig = ConsistencyCheckJobConfig.Disabled,
    concurrency = Concurrency(group = "pages", cancelInProgress = true),
    permissions = mapOf(
        Permission.Contents to Mode.Read,
        Permission.Pages to Mode.Write,
        Permission.IdToken to Mode.Write
    ),
) {
    val exportJob = job(id = "export", runsOn = UbuntuLatest) {
        uses(name = "Checkout", action = Checkout())

        uses(
            name = "Set up Java",
            action = SetupJava(javaVersion = "17", distribution = SetupJava.Distribution.Temurin)
        )

        uses(name = "Setup Gradle", action = ActionsSetupGradle(validateWrappers = true))

        run(
            name = "Build site",
            command = "./gradlew composeApp:jsBrowserDistribution",
        )

        uses(
            name = "Upload artifact",
            action = UploadPagesArtifact(path = "composeApp/build/dist/js/productionExecutable"),
        )
    }
    val deploymentId = "deployment"
    job(
        id = "deploy",
        runsOn = UbuntuLatest,
        needs = listOf(exportJob),
        environment = Environment(name = "github-pages", url = expr { "steps.$deploymentId.outputs.page_url" }),
    ) {
        uses(
            action = DeployPages(),
            _customArguments = mapOf("id" to deploymentId)
        )
    }
}
