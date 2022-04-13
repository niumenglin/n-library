package com.n.library.restful

import java.lang.reflect.Proxy


open class NRestful constructor(val baseUrl: String, callFactory: NCall.Factory) {
    private var interceptors: MutableList<NInterceptor> = mutableListOf()

    fun addInterceptor(interceptor: NInterceptor) {
        interceptors.add(interceptor)
    }

    fun <T> create(service: Class<T>): T {
        return Proxy.newProxyInstance(
            service.classLoader,
            arrayOf<Class<*>>(service)
        ) { proxy, method, args ->

        } as T
    }

}