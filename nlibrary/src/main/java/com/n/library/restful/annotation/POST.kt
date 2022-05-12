package com.n.library.restful.annotation

/**
 * @POST("/cities/{province}")
 * fun test(@Path("province") int provinceId)
 * formPost = true 默认表单提交
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class POST (val value:String,val formPost:Boolean = true)