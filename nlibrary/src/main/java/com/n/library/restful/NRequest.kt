package com.n.library.restful

import androidx.annotation.IntDef
import java.lang.reflect.Type

open class NRequest {

    @METHOD
    var httpMethod:Int = 0
    var headers:MutableMap<String,String>?=null
    var parameters:MutableMap<String,String>?=null
    var domainUrl:String?=null
    var relativeUrl:String?=null //本次请求相对路径
    var returnType: Type? = null
    var formPost: Boolean = true

    @IntDef(value = [METHOD.GET,METHOD.POST])
    annotation class METHOD {
        companion object {
            const val GET = 0
            const val POST = 1
        }
    }

    //返回的是请求完整的url
    /**
     * scheme-host-port:443
     * https://api.devio.org/v1/ --relativeUrl:user/login ===> https://api.devio.org/v1/user/login
     *
     * 可能存在别的域名的场景
     * https://api.devio.org/v1/ --relativeUrl:/v2/user/login ===> https://api.devio.org/v2/user/login
     *
     */
    fun endPointUrl(): String {
        if (relativeUrl==null){
            throw IllegalStateException("relative url must not be null.")
        }
        if (!relativeUrl!!.startsWith("/")){
            return domainUrl+relativeUrl
        }

        val indexOf = domainUrl!!.indexOf("/")
        return domainUrl!!.substring(0,indexOf)+relativeUrl
    }

    fun addHeader(name: String, value: String) {
        if (headers==null){
            headers = mutableMapOf()
        }
        headers!![name] = value
    }
}
