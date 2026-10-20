package com.bfoxnet.pocketcode.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bfoxnet.pocketcode.activities.main.MainActivity

object FeatureViewModel {
    inline fun <reified T : ViewModel> get(): T? {
        val instance = MainActivity.instance ?: return null
        return ViewModelProvider(instance)[T::class.java]
    }
}
