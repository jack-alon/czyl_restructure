package com.bjcr.component_base

import android.app.Application
import android.content.Context

/**
 * android app启动调用的方法流程：
 * 1.Application 中的attachBaseContext 此时applicationContext还为null
 * 2.contentprovider的attached方法
 * 3.application 的onCreate以及contentprovider的call方法
 *
 */
open class BaseApplication: Application() {

    companion object{
        private lateinit var application: BaseApplication//全局application 实例
        fun getApplication() = application//外面获取application实例
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        application = this
    }

    override fun onCreate() {
        super.onCreate()

    }

}