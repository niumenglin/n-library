package com.n.library.restful.annotation

/**
 * 域名
 * @BaseUrl("https://api.n.org/as/")
 * fun testMethod(@Filed("province") int provinceId)
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseUrl (val value:String)