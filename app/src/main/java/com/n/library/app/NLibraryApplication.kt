package com.n.library.app

import android.app.Application
import com.n.library.util.ActivityManager

/**
 * n-library demo
 */
class NLibraryApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        init()
    }

    private fun init() {
        //初始化Activity任务栈
        ActivityManager.instance.init(this)
    }

}