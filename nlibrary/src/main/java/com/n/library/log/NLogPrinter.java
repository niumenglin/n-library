package com.n.library.log;

import androidx.annotation.NonNull;

/**
 * @desc: 日志打印接口
 */
public interface NLogPrinter {
    void print(@NonNull NLogConfig config,int level,String tag,@NonNull String printString);
}
