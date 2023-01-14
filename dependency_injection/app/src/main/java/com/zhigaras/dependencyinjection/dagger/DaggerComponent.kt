package com.zhigaras.dependencyinjection.dagger

import com.zhigaras.dependencyinjection.MainActivity
import dagger.Component
import javax.inject.Singleton

@Component
@Singleton
interface DaggerComponent {
    
    fun inject(activity: MainActivity)
}