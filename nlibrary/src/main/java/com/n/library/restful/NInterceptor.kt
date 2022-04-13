package com.n.library.restful

import android.security.KeyChain

/**
 * 拦截器
 */
interface NInterceptor {
    fun intercept(chain: Chain):Boolean

    /**
     * Chain 对象会在我们派发拦截器的时候 创建
     */
    interface Chain {
        val isRequestPeriod:Boolean get() = false //是否在请求阶段

        fun request():NRequest

        /**
         * 这个response对象 在网络发起之前，是为空的
         * 在网络发起请求之前和网络发起之后都会派发一遍，所以在网络发起之前response()返回值是空的
         */
        fun response():NResponse<*>?
    }
}