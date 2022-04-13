package com.n.library.restful

/**
 * 代理CallFactory创建出来的call对象，从而实现拦截器的派发动作
 */
class Scheduler(
    val callFactory: NCall.Factory,
    val interceptors: MutableList<NInterceptor>
) {

    fun newCall(request: NRequest): NCall<*> {
        val newCall:NCall<*> = callFactory.newCall(request)
        return ProxyCall(newCall,request)
    }

    internal inner class ProxyCall<T>(
        val delegate: NCall<T>,
        val request: NRequest
    ) :NCall<T>{
        override fun execute(): NResponse<T> {
            dispatchInterceptor(request,null)

            val response = delegate.execute()

            dispatchInterceptor(request,response)

            return response
        }

        override fun enqueue(callback: NCallback<T>) {
            dispatchInterceptor(request,null)

            delegate.enqueue(object :NCallback<T>{
                override fun onSuccess(response: NResponse<T>) {
                    dispatchInterceptor(request,response)

                    if (callback!=null) callback.onSuccess(response)
                }

                override fun onFailed(throwable: Throwable) {
                    if (callback!=null) callback.onFailed(throwable)
                }

            })
        }

        private fun dispatchInterceptor(request: NRequest, response: NResponse<T>?) {
            InterceptorChain(request,response).dispatch()
        }

        internal inner class InterceptorChain(
            val request: NRequest,
            val response: NResponse<T>?
        ):NInterceptor.Chain{
            //代表的是 分发的第几个拦截器
            var callIndex: Int = 0
            override val isRequestPeriod: Boolean
                get() = response==null

            override fun request(): NRequest {
                return request
            }

            override fun response(): NResponse<*>? {
                return response
            }

            fun dispatch(){
                val interceptor = interceptors[callIndex]
                val intercept = interceptor.intercept(this)
                callIndex ++
                if (!intercept&&callIndex<interceptors.size){
                    dispatch()//如果不拦截，并且小于拦截器集合size。完成下一个分发动作
                }
            }

        }

    }
}