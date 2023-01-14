package com.zhigaras.dependencyinjection

import android.app.Application
import com.zhigaras.dependencyinjection.dagger.DaggerComponent
import com.zhigaras.dependencyinjection.dagger.DaggerDaggerComponent
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {
    
    val daggerComponent: DaggerComponent by lazy {
        DaggerDaggerComponent.builder().build()
    }
    
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(module {
                single { FrameFactory() }
                single { WheelsDealer() }
                factory<BicycleFactory> { BicycleFactory(get(), get()) }
            })
        }
    }
}