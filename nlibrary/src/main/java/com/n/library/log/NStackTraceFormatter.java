package com.n.library.log;

/**
 * @author: niumenglin
 * @time: 2021/12/24 12:49 下午
 * @email: menglin.nml@ncarzone.com
 * @desc: 堆栈信息格式化器
 */
public class NStackTraceFormatter implements NLogFormatter<StackTraceElement[]>{
    @Override
    public String format(StackTraceElement[] stackTrace) {
        StringBuilder sb = new StringBuilder(128);
        if (stackTrace==null||stackTrace.length==0){
            return null;
        } else if (stackTrace.length==1){
            return "\t- "+stackTrace[0].toString();
        } else {
            for (int i = 0,len = stackTrace.length;i<len; i++) {
                if (i==0){
                    sb.append("stackTrace: \n");
                }
                if (i==len-1){
                    sb.append("\t|- ");
                    sb.append(stackTrace[i].toString());
                    sb.append("\n");
                } else {
                    sb.append("\t「 ");
                    sb.append(stackTrace[i].toString());
                    sb.append("\n");
                }
            }
            return sb.toString();
        }
    }
}
