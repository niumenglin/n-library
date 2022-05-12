package com.n.library.restful

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap


open class NRestful constructor(val baseUrl: String, val callFactory: NCall.Factory) {
    private var interceptors: MutableList<NInterceptor> = mutableListOf()
    //支持并发情况
    private var methodService:ConcurrentHashMap<Method,MethodParser> = ConcurrentHashMap()
    private var scheduler:Scheduler = Scheduler(callFactory,interceptors)

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
            arrayOf<Class<*>>(service),object :InvocationHandler{
                //bugFix:此处需要考虑空参数
                override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any {
                    var methodParser = methodService[method]
                    if (methodParser == null){
                        methodParser = MethodParser.parse(baseUrl, method)
                        methodService.put(method,methodParser)
//                        methodService[method] = methodParser
                    }

                    //bugFix:此处应当考虑到 methodParser复用，每次调用都应当解析入参
                    val request = methodParser.newRequest(method, args)
                    return scheduler.newCall(request)
                }
            }) as T
    }

}