package com.n.library.restful


interface NCallback <T>{
    fun onSuccess(response: NResponse<T>)
    fun onFailed(throwable: Throwable)
}