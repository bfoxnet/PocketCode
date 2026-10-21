package com.bfoxnet.pocketcode.application

import com.bfoxnet.pocketcode.App
import com.bfoxnet.pocketcode.TerminalFeature
import com.bfoxnet.pocketcode.ai.AiFeature
import com.bfoxnet.pocketcode.app.AppFlavour
import com.bfoxnet.pocketcode.feature.FeatureRegistry
import com.bfoxnet.pocketcode.git.GitFeature
import com.bfoxnet.pocketcode.runner.RunnerFeature

/**
 * Application entry point for the `playstore` distribution.
 *
 * The Play Store build deliberately does not bundle the `:features:extensions` module, because Play policy forbids
 * loading executable code at runtime, so its feature is not registered here.
 */
class XedApplication : App() {
    override fun onCreate() {
        // Publish the compile-time flavour (see app/build.gradle.kts) before any shared code runs.
        AppFlavour.init(BuildConfig.FLAVOUR)

        super.onCreate()

        // Register pluggable features
        FeatureRegistry.register(TerminalFeature())
        FeatureRegistry.register(RunnerFeature())
        FeatureRegistry.register(GitFeature())
        FeatureRegistry.register(AiFeature())

        // Initialize core features
        FeatureRegistry.initFeatures(this)
    }
}
