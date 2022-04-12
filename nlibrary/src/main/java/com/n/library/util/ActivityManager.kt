package com.n.library.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference

/**
 * Activity任务栈管理
 * 背景：Android未提供获取栈顶Activity和前后台API
 * 支持如下功能:
 * 1.在应用的任意位置 获取我们应用正在显示的Activity;
 * 2.应用进入前台/后台后,我们希望拿到这个回调，进而处理一些定时任务。
 */
class ActivityManager private constructor(){

    //Activity存储。放在弱引用里，防止内存泄漏
    private var activityRefs = ArrayList<WeakReference<Activity>>()
    //应用切换前后台监听
    private val foreBackgroundCallbacks = ArrayList<ForeBackgroundCallback>()
    //统计当前处于前台的Activity数量
    private var activityStartCount = 0
    //标志位，记录当前应用是否进入到前台.默认true，由于应用一打开，必然在前台。
    private var foreBackground = true

    fun init(application: Application){
        application.registerActivityLifecycleCallbacks(InnerActivityLifecycleCallbacks())
    }

    //内部类-Activity生命周期管理
    inner class InnerActivityLifecycleCallbacks :Application.ActivityLifecycleCallbacks{
        override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
            //打开新的Activity，并将其存储起来. 将activity加入到WeakReference中，防止内存泄漏
            activityRefs.add(WeakReference(activity))
        }

        override fun onActivityStarted(activity: Activity) {
            activityStartCount ++
            //activityStartCount>0  说明应用处在可见状态，也就是前台。 可能是后台切前台导致的；也有可能是打开新的Activity，导致activityStartCount>0
            //所以 判断应用是否在后台，如果在后台，即!foreBackground=true
            if (!foreBackground&&activityStartCount>0){//后台->切换到前台
                foreBackground = true
                onForeBackgroundChanged(foreBackground)
            }
        }

        override fun onActivityResumed(activity: Activity) {
        }

        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivityStopped(activity: Activity) {
            activityStartCount --
            if (foreBackground&&activityStartCount<=0){//前台->切换到后台
                foreBackground = false
                onForeBackgroundChanged(foreBackground)
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
            for (activityRef in activityRefs) {
                if (activityRef.get() == activity){
                    activityRefs.remove(activityRef)
                    break
                }
            }
        }
    }

    /**
     * 当前后台发生变化，将结果回调出去
     */
    private fun onForeBackgroundChanged(foreBackground: Boolean) {
        for (foreBackgroundCallback in foreBackgroundCallbacks) {
            foreBackgroundCallback.onChange(foreBackground)
        }
    }

    /**
     * 获取栈顶Activity
     */
    val topActivity:Activity?get() {
        return if (activityRefs.size <= 0) {
            null
        } else {
            activityRefs[activityRefs.size-1].get()
        }
    }

    //注册前后台切换能力
    fun addForeBackgroundCallback(callback:ForeBackgroundCallback){
        foreBackgroundCallbacks.add(callback)
    }

    //移除前后台切换能力
    fun removeForeBackgroundCallback(callback:ForeBackgroundCallback){
        foreBackgroundCallbacks.remove(callback)
    }

    //前后台切换监听接口
    interface ForeBackgroundCallback {
        fun onChange(fore:Boolean)
    }

    //Kotlin单例模式
    companion object{
        //线程安全加载模式
        val instance:ActivityManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
            ActivityManager()
        }
    }
}