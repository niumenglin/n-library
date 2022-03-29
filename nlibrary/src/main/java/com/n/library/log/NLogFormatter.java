package com.n.library.log;

/**
 * @desc: 日志格式化
 */
public interface NLogFormatter<T> {
    String format(T data);
}
