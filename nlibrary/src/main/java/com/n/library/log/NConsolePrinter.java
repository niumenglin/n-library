package com.n.library.log;

import static com.n.library.log.NLogConfig.MAX_LEN;

import android.util.Log;

import androidx.annotation.NonNull;
/**
 * @desc: 控制台打印器
 */
public class NConsolePrinter implements NLogPrinter{

    @Override
    public void print(@NonNull NLogConfig config, int level, String tag, @NonNull String printString) {
        int len = printString.length();
        int countOfSub = len/MAX_LEN;
        if (countOfSub>0){
            int index = 0 ;
            for (int i = 0; i < countOfSub; i++) {
                Log.println(level,tag,printString.substring(index,index+MAX_LEN));
                index+=MAX_LEN;
            }
            if (index!=len){
                Log.println(level,tag,printString.substring(index,len));
            }
        }else{
            Log.println(level,tag,printString);
        }
    }
}
