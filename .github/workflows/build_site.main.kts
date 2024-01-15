#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.9.0")

import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
import io.github.typesafegithub.workflows.actions.actions.SetupJavaV4
import io.github.typesafegithub.workflows.actions.gradle.GradleBuildActionV2
import io.github.typesafegithub.workflows.domain.Concurrency
import io.github.typesafegithub.workflows.domain.Mode
import io.github.typesafegithub.workflows.domain.Permission
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.domain.triggers.WorkflowDispatch
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile

workflow(
    name = "Deploy Site to Pages",
    on = listOf(
        WorkflowDispatch(),
        Push(
            branches = listOf("master"),
        )
    ),
    permissions = mapOf(
        Permission.Contents to Mode.Read,
        Permission.Pages to Mode.Write,
        Permission.IdToken to Mode.Write
    ),
    concurrency = Concurrency(group = "pages", cancelInProgress = true),
    sourceFile = __FILE__.toPath(),
    targetFileName = "build_and_deploy_site.yml"
) {
    val exportJob = job(id = "export", runsOn = UbuntuLatest) {
        uses(name = "Checkout", action = CheckoutV4())

        uses(
            name = "Set up Java",
            action = SetupJavaV4(javaVersion = "17", distribution = SetupJavaV4.Distribution.Temurin)
        )

        uses(name = "Setup Gradle", action = GradleBuildActionV2())

        run(
            name = "Build site",
            command = "./gradlew composeApp:jsBrowserProductionWebpack",
        )

        uses(
            name = "Upload artifact",
            action = UploadPagesArtifactV2(path = "composeApp/build/dist/js/productionExecutable"),
        )
    }
    val deploymentId = "deployment"
    job(
        id = "deploy",
        runsOn = UbuntuLatest,
        needs = listOf(exportJob),
        _customArguments = mapOf(
            "environment" to mapOf(
                "name" to "github-pages",
                "url" to expr { "steps.$deploymentId.outputs.page_url" }
            )
        )
    ) {
        uses(
            action = DeployPagesV2(),
            _customArguments = mapOf("id" to deploymentId)
        )
    }
}.writeToFile(addConsistencyCheck = false)


class UploadPagesArtifactV2(private val path: String) :
    RegularAction<Action.Outputs>("actions", "upload-pages-artifact", "v2") {
    override fun toYamlArguments() = linkedMapOf("path" to path)

    override fun buildOutputObject(stepId: String) = Outputs(stepId)
}

class DeployPagesV2 : RegularAction<Action.Outputs>("actions", "deploy-pages", "v2") {
    override fun toYamlArguments() = linkedMapOf<String, String>()

    override fun buildOutputObject(stepId: String) = Outputs(stepId)
}