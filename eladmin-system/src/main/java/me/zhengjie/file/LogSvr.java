package me.zhengjie.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LogSvr {
    private SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 将信息记录到日志文件
     * @param logFile 日志文件
     * @param mesInfo 信息
     * @throws IOException
     */
    public void logMsg(File logFile, String mesInfo) throws IOException {
        if(logFile == null) {
            throw new IllegalStateException("logFile can not be null!");
        }
        Writer txtWriter = new FileWriter(logFile,true);
        txtWriter.write(dateFormat.format(new Date()) +"\t"+mesInfo+"\n");
        txtWriter.flush();
    }

    public static void main(String[] args) throws Exception{

        final LogSvr logSvr = new LogSvr();
        final File tmpLogFile = new File("mock.log");
        if(!tmpLogFile.exists()) {
            tmpLogFile.createNewFile();
        }
        //启动一个线程每5秒钟向日志文件写一次数据
        ScheduledExecutorService exec =
                Executors.newScheduledThreadPool(1);
        exec.scheduleWithFixedDelay(new Runnable(){
            public void run() {
                try {
                    logSvr.logMsg(tmpLogFile, " hello, test !");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, 5, TimeUnit.SECONDS);
    }
}
