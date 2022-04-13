package com.n.library.restful.annotation

/**
 * @Headers({"connection:keep-alive","auth-token:token"})
 * fun test(@Field("province") int provinceId)
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Headers (vararg val value:String)