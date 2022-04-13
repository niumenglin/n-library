package com.n.library.restful.annotation

/**
 * 标记参数别名
 * @BaseUrl("https://api.n.org/as/")
 * fun testMethod(@Filed("province") int provinceId)
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Field (val value: String)