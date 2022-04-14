package com.n.library.restful

import java.lang.reflect.Type

/**
 * 数据解析
 * 由业务端决定使用Gson、还是FastJson
 */
interface NConvert {
    fun<T> convert(rawData:String,dataType:Type):NResponse<T>
}