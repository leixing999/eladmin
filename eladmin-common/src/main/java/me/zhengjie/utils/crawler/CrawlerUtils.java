package me.zhengjie.utils.crawler;
import me.zhengjie.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CrawlerUtils {
   // public   String APK_DOWNLOAD_PATH = "/home/app/data/";
    private  Logger LOGGER = LoggerFactory.getLogger(CrawlerUtils.class);

    /**
     * 使用wget下载文件
     *
     * @param displayName  appName
     * @param apkDownloadPath 下载路径
     *
     * @param download_url 下载地址
     * @return 成功返回文件路径，失败返回null
     */
    public  String downloadFileByWget(String displayName, String apkDownloadPath, String download_url,String sysFileName) {

        String fileName = download_url.substring(download_url.lastIndexOf("/")+1);
        String dir = apkDownloadPath +  "/";
        File file = new File(dir);
        if(!file.exists()){
            file.mkdir();
        }
        String filePath = dir+ sysFileName ;
        int retry = 2;
        int res = -1;
        int time = 1;
        while (retry-- > 0) {
            ProcessBuilder pb = new ProcessBuilder("wget", download_url, "-t", "2", "-T", "10", "-O", filePath);
            LOGGER.info("wget shell: {}", pb.command());
            Process ps = null;
            try {
                ps = pb.start();
            } catch (IOException e) {
                LOGGER.error("IOException", e);
            }
            res = doWaitFor(ps, 90 * time++);
            if (res != 0) {
                LOGGER.warn("Wget download failed...");
            } else {
                break;
            }
        }
        if (res != 0) {
            return null;
        }
        return filePath;
    }


    /**
     * @param ps      sub process
     * @param timeout 超时时间，SECONDS
     * @return 正常结束返回0
     */
    private  int doWaitFor(Process ps, int timeout) {
        int res = -1;
        if (ps == null) {
            return res;
        }
        List<String> stdoutList = new ArrayList<>();
        List<String> erroroutList = new ArrayList<>();
        boolean finished = false;
        int time = 0;
        ThreadUtil stdoutUtil = new ThreadUtil(ps.getInputStream(), stdoutList);
        ThreadUtil erroroutUtil = new ThreadUtil(ps.getErrorStream(), erroroutList);
        //启动线程读取缓冲区数据
        stdoutUtil.start();
        erroroutUtil.start();
        while (!finished) {
            time++;
            if (time >= timeout) {
                LOGGER.info("Process wget timeout 30s, destroyed!");
                ps.destroy();
                break;
            }
            try {
                res = ps.exitValue();
                finished = true;
            } catch (IllegalThreadStateException e) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e1) {

                }
            }
        }
        return res;
    }
}
