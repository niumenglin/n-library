package com.n.library.log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @desc: NLog管理类
 */
public class NLogManager {
    private NLogConfig config;
    private static NLogManager instance;
    private List<NLogPrinter> printers = new ArrayList<>();

    public NLogManager(NLogConfig config,NLogPrinter[] printers) {
        this.config = config;
        this.printers.addAll(Arrays.asList(printers));
    }

    public static NLogManager getInstance() {
        return instance;
    }

    public static void init(@NonNull NLogConfig config,NLogPrinter... printers){
        instance = new NLogManager(config,printers);
    }

    public NLogConfig getConfig() {
        return config;
    }

    /**
     * 获取打印器
     * @return
     */
    public List<NLogPrinter> getPrinters() {
        return printers;
    }

    /**
     * 添加打印器
     * @param printer
     */
    public void addPrinter(NLogPrinter printer) {
        printers.add(printer);
    }

    /**
     * 移除打印器
     * @param printer
     */
    public void removePrinter(NLogPrinter printer) {
        if (printers!=null){
            printers.remove(printer);
        }
    }
}
