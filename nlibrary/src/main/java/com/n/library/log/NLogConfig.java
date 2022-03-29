package com.n.library.log;

/**
 * 配置
 */
public abstract class NLogConfig {

    static int MAX_LEN = 512;
    static NStackTraceFormatter N_STACKTRACE_FORMATTER = new NStackTraceFormatter();
    static NThreadFormatter N_THREAD_FORMATTER = new NThreadFormatter();

    public JsonParser injectJsonParser(){
        return null;
    }

    //不传tag，使用全局的tag
    public String getGlobalTag(){
        return "NLog";
    }

    //默认启动NLog
    public boolean enable(){
        return true;
    }

    //日志里面是否包含线程信息， 默认不包含
    public boolean includeThread(){
        return false;
    }

    public int stackTraceDepth(){
        return 5;
    }

    public NLogPrinter[] printers(){
        return null;
    }

    //序列化交给调用者实现，序列化与框架解耦（SDK不需要依赖Gson、FastJson工具）
    public interface JsonParser{
        String toJson(Object src);
    }

}
