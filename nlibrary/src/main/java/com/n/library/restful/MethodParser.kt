package com.n.library.restful

import com.n.library.restful.annotation.*
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 方法解析器
 */
class MethodParser(
    var baseUrl: String,
    method: Method,
    args: Array<Any>
) {

    private lateinit var domainUrl: String
    private var formPost: Boolean = true
    private var httpMethod: Int = -1
    private lateinit var relativeUrl: String
    private lateinit var returnType: Type
    private val headers: MutableMap<String, String> = mutableMapOf()
    private val parameters: MutableMap<String, Any> = mutableMapOf()


    init {
        //parse method annotations such as GET,POST,Headers,baseUrl
        parseMethodAnnotations(method)

        //parse method parameters such as Path，Field
        parseMethodParameters(method, args)

        //parse method generic return type
        parseMethodReturnType(method)
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
    //解析方法泛型返回值类型
    private fun parseMethodReturnType(method: Method) {
        if (method.returnType != NCall::class) {
            //print method name
            throw IllegalStateException(
                String.format(
                    "method %s must be type of NCall.class",
                    method.name
                )
            )
        }
        //拿到方法的泛型返回参数
        val genericReturnType = method.genericReturnType
        if (genericReturnType is ParameterizedType) {
            val actualTypeArguments = genericReturnType.actualTypeArguments
            //require(boolean) 用来检测方法的参数，当参数boolean为false时，抛出 IllegalArgumentException
            require(actualTypeArguments.size == 1) { "method %s can only has one generic return type" }
            returnType = actualTypeArguments[0]
        } else {
            throw IllegalStateException(
                String.format(
                    "method %s must has one generic return type",
                    method.name
                )
            )
        }
    }

    //解析方法入参
    private fun parseMethodParameters(method: Method, args: Array<Any>) {
        //@Path("province") province:Int,@Field(page) page:Int
        val parameterAnnotations = method.parameterAnnotations
        val equals = parameterAnnotations.size == args.size
        require(equals) {
            String.format(
                "arguments annotations count %s don't match expect count %s",
                parameterAnnotations.size,
                args.size
            )
        }

        //args
        for (index in args.indices){
            val annotations = parameterAnnotations[index]
            require(annotations.size <= 1){"filed can only has one annotation :index = $index"}

            val value = args[index]
            //效验，不符合基本数据类规则
            require(isPrimitive(value)){"8 basic types are supported for now,index=$index"}

            val annotation = annotations[0]
            if (annotation is Field){
                val key = annotation.value
                val value = args[index]
                parameters[key] = value
            } else if (annotation is Path){
                val replaceName = annotation.value
                val replacement = value.toString()
                if (replaceName!=null&&replacement!=null){
                    val newRelativeUrl = relativeUrl.replace("{$replaceName}",replacement)
                    relativeUrl = newRelativeUrl
                }
            } else {
                throw IllegalStateException("cannot handle parameter annotation :"+annotation.javaClass.toString())
            }
        }
    }

    private fun isPrimitive(value: Any):Boolean {
        //String
        if (value.javaClass == String::class.java){
            return true
        }
        try {
            //int byte short long boolean char double float
            val field = value.javaClass.getField("TYPE")
            val clazz = field[null] as Class<*>
            if (clazz.isPrimitive){//8 basic types
                return true
            }
        }catch (e:IllegalStateException){
            e.printStackTrace()
        }catch (e:NoSuchFileException){
            e.printStackTrace()
        }
        return false
    }

    //解析方法注解
    private fun parseMethodAnnotations(method: Method) {
        val annotations = method.annotations
        for (annotation in annotations) {
            if (annotation is GET) {
                relativeUrl = annotation.value
                httpMethod = NRequest.METHOD.GET
            } else if (annotation is POST) {
                relativeUrl = annotation.value
                httpMethod = NRequest.METHOD.POST
                formPost = annotation.formPost
            } else if (annotation is Headers) {
                val headerArray = annotation.value
                for (header in headerArray) {
                    val colon = header.indexOf(":")//get : index
                    //check(boolean)用来检测对象的状态（属性），如果boolean为false，抛出异常 IllegalStateException
                    check(!(colon == 0 || colon == -1)) {
                        String.format(
                            "@Headers value must be in the form[name:value] ,but found [%s]",
                            header
                        )
                    }
                    val name = header.substring(0, colon)
                    val value = header.substring(colon + 1).trim()
                    headers[name] = value
                }
            } else if (annotation is BaseUrl) {
                domainUrl = annotation.value
            } else {
                //抛出注解异常
                throw IllegalStateException("cannot handle method annotation:" + annotation.javaClass.toString())
            }

            require(!(httpMethod != NRequest.METHOD.GET) && !(httpMethod != NRequest.METHOD.POST)) {
                String.format("method %s must has one of GET,POST", method.name)
            }
        }

        if (domainUrl == null){
            domainUrl = baseUrl
        }
    }


    companion object {
        fun parse(baseUrl: String, method: Method, args: Array<Any>): MethodParser {
            return MethodParser(baseUrl, method, args)
        }
    }

}