package com.n.library.restful

import java.io.IOException

interface NCall<T> {
    //同步请求
    @Throws(IOException::class)
    fun execute():NResponse<T>

    //异步请求
    fun enqueue(callback:NCallback<T>)

    //通过工厂模式创建 NCall
    interface Factory{
        fun newCall(request:NRequest):NCall<*>
    }
}