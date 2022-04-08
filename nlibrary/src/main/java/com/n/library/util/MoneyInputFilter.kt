package com.n.library.util

import android.text.InputFilter
import android.text.Spanned

/**
 * 金额输入过滤器
 * 1、不允许第一个字符为.
 * 2、保留两位小数
 * 3、不允许出现0123、0456这类字符串(即不允许为0开头输入)
 * （以上第三点暂时注释掉，允许用户输入0）
 */
class MoneyInputFilter : InputFilter {

    companion object{
        const val POINT_LENGTH = 2 //保留小数点位数
    }

    override fun filter(
        source: CharSequence, //将要输入的字符串，如果是删除操作则为空
        start: Int, //将要输入的字符串起始下标，一般为0
        end: Int, //start+source字符串长度
        dest: Spanned, //输入之前文本框中的内容
        dstart: Int, //将被替换的起始位置
        dend: Int//dstart+将会被替换的字符串长度
    ): CharSequence {
        val start = dest.subSequence(0,dstart)
        val end = dest.subSequence(dend,dest.length)
        val target = start.toString()+source+end //字符串变化后的结果
        val backup = dest.subSequence(dstart,dend) //将要被替换的字符串

        if (target.indexOf(".") ==0){//不允许第一个字符为.
            return backup
        }

//        if (target.startsWith("0")
//                &&!target.startsWith("0.")
//                &&"0"==target){//不允许出现0123、0456这类字符串
//            return backup
//        }

        //保留两位小数
        val index = target.indexOf(".")
        if (index>=0&&index+ POINT_LENGTH+2<=target.length){
            return backup
        }
        return source
    }
}