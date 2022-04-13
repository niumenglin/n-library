package com.n.library.restful.annotation

/**
 * 用于替换相对路径的占位符
 * @GET("/cities/{province}")
 * fun test(@Path("province") int provinceId)
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Path (val value:String)