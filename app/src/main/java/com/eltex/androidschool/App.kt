package com.eltex.androidschool

import android.app.Application
import com.eltex.androidschool.di.DependencyContainer
import com.eltex.androidschool.di.DependencyContainerImpl
import com.eltex.androidschool.di.DependencyContainerProvider

class App : Application(), DependencyContainerProvider {
    private val container: DependencyContainerImpl by lazy {
        DependencyContainerImpl()
    }

    override fun getContainer(): DependencyContainer = container
}
