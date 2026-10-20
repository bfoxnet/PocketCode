package com.bfoxnet.pocketcode.application

import com.bfoxnet.pocketcode.App
import com.rk.ExtensionFeature
import com.bfoxnet.pocketcode.TerminalFeature
import com.bfoxnet.pocketcode.ai.AiFeature
import com.bfoxnet.pocketcode.app.AppFlavour
import com.bfoxnet.pocketcode.feature.FeatureRegistry
import com.bfoxnet.pocketcode.git.GitFeature
import com.bfoxnet.pocketcode.runner.RunnerFeature

/**
 * Application entry point for the `community` distribution.
 *
 * Registers every feature, including `ExtensionFeature` provided by the bundled `:features:extensions` module.
 */
class XedApplication : App() {
    override fun onCreate() {
        // Publish the compile-time flavour (see app/build.gradle.kts) before any shared code runs.
        AppFlavour.init(BuildConfig.FLAVOUR)

        super.onCreate()

        // Register pluggable features
        FeatureRegistry.register(TerminalFeature())
        FeatureRegistry.register(ExtensionFeature())
        FeatureRegistry.register(RunnerFeature())
        FeatureRegistry.register(GitFeature())
        FeatureRegistry.register(AiFeature())

        // Initialize core features
        FeatureRegistry.initFeatures(this)
    }
}
