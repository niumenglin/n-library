package com.n.library.restful

import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap


open class NRestful constructor(val baseUrl: String, val callFactory: NCall.Factory) {
    private var interceptors: MutableList<NInterceptor> = mutableListOf()
    //支持并发情况
    private var methodService:ConcurrentHashMap<Method,MethodParser> = ConcurrentHashMap()
    private var scheduler:Scheduler

    init {
        scheduler = Scheduler(callFactory,interceptors)
    }

    fun addInterceptor(interceptor: NInterceptor) {
        interceptors.add(interceptor)
    }

    /**
     * API模型
     * interface ApiService {
     *    @Headers("auth-token:token","userId:123456")
     *    @BaseUrl("https://api.devio.org/as/")
     *    @POST("/cities/{province}")
     *    @GET("/cities")
     *    fun listCities(@Path("province") province:Int,@Field(page) page:Int):NCall<JsonObject>
     * }
     */
    fun <T> create(service: Class<T>): T {
        return Proxy.newProxyInstance(
            service.classLoader,
            arrayOf<Class<*>>(service)
        ) { proxy, method, args ->
            var methodParser = methodService.get(method)
            if (methodParser == null){
                methodParser = MethodParser.parse(baseUrl, method, args)
                methodService.put(method,methodParser)
            }
            val request = methodParser.newRequest()
            //callFactory.newCall(request)
            scheduler.newCall(request)
        } as T
    }

}