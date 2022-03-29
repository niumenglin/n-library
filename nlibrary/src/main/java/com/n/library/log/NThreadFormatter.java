package com.n.library.log;

/**
 * @desc: 线程格式化
 */
public class NThreadFormatter implements NLogFormatter<Thread>{

    /**
     * 返回线程名称
     * @param data
     * @return
     */
    @Override
    public String format(Thread data) {
        return "Thread:"+data.getName();
    }

}
