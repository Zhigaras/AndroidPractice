package com.zhigaras.dependencyinjection

import android.app.Application
import com.zhigaras.dependencyinjection.dagger.DaggerComponent
import com.zhigaras.dependencyinjection.dagger.DaggerDaggerComponent
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {
    
    lateinit var daggerComponent: DaggerComponent
    
    override fun onCreate() {
        super.onCreate()
        
        daggerComponent = DaggerDaggerComponent.builder().build()
        
        startKoin {
            modules(module {
                single { FrameFactory() }
                single { WheelsDealer() }
                factory { BicycleFactory(get(), get()) }
            })
        }
    }
}