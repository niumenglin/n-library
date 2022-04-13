package com.n.library.restful.annotation

/**
 * 传入接口的相对路径
 * @GET("/cities/all")
 * fun test(@Field("province") int provinceId)
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class GET (val value:String)