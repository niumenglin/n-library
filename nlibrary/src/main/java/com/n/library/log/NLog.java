package com.n.library.log;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

/**
 * Tips:
 * 1.打印堆栈信息
 * 2.File输出
 * 3.模拟控制台
 */
public class NLog {

    public static void v(Object... contents){
        log(NLogType.V,contents);
    }

    /**
     * 带tag
     * @param tag
     * @param contents
     */
    public static void vt(String tag,Object... contents){
        log(NLogType.V,tag,contents);
    }


    public static void d(Object... contents){
        log(NLogType.D,contents);
    }

    public static void dt(String tag,Object... contents){
        log(NLogType.D,tag,contents);
    }

    public static void i(Object... contents){
        log(NLogType.I,contents);
    }

    public static void it(String tag,Object... contents){
        log(NLogType.I,tag,contents);
    }

    public static void w(Object... contents){
        log(NLogType.W,contents);
    }

    public static void wt(String tag,Object... contents){
        log(NLogType.W,tag,contents);
    }

    public static void e(Object... contents){
        log(NLogType.E,contents);
    }

    public static void et(String tag,Object... contents){
        log(NLogType.E,tag,contents);
    }

    public static void a(Object... contents){
        log(NLogType.A,contents);
    }

    public static void at(String tag,Object... contents){
        log(NLogType.A,tag,contents);
    }


    public static void log(@NLogType.TYPE int type, Object... contents){
        log(type,NLogManager.getInstance().getConfig().getGlobalTag(),contents);
    }

    public static void log(@NLogType.TYPE int type,@NonNull String tag, Object... contents){
        log(NLogManager.getInstance().getConfig(),type,tag,contents);
    }

    public static void log(@NonNull NLogConfig config, @NLogType.TYPE int type,@NonNull String tag, Object... contents){
        if (!config.enable()){
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (config.includeThread()){
            String threadInfo = NLogConfig.N_THREAD_FORMATTER.format(Thread.currentThread());
            sb.append(threadInfo).append("\n");
        }
        if (config.stackTraceDepth()>0){
            String stackTraceInfo = NLogConfig.N_STACKTRACE_FORMATTER.format(new Throwable().getStackTrace());
            sb.append(stackTraceInfo).append("\n");
        }
        String body = parseBody(contents,config);
        sb.append(body);
        List<NLogPrinter> printers = config.printers()!=null? Arrays.asList(config.printers()):NLogManager.getInstance().getPrinters();
        if (printers==null){
            return;
        }
        //打印log
        for (NLogPrinter printer:printers) {
            printer.print(config,type,tag,sb.toString());
        }
    }

    private static String parseBody(@NonNull Object[] contents,@NonNull NLogConfig config){
        if (config.injectJsonParser()!=null){
            return config.injectJsonParser().toJson(contents);
        }
        StringBuilder sb = new StringBuilder();
        for (Object o:contents) {
            sb.append(o.toString()).append(";");
        }
        if (sb.length()>0){
            sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString();
    }

}
