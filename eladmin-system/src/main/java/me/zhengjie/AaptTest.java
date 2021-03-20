package me.zhengjie;

import me.zhengjie.domain.ApkInfo;
import me.zhengjie.modules.app.domain.vo.UrlPathVO;
import me.zhengjie.modules.app.service.impl.UrlPathService;
import me.zhengjie.modules.app.thread.HttpDownloadService;
import me.zhengjie.utils.ApkUtil;
import me.zhengjie.utils.PageUtil;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.*;
public class AaptTest {
    public static void main(String[] args) throws Exception {
        BlockingQueue blockingQueue = new ArrayBlockingQueue<>(30);
        List<UrlPathVO> urlPathVOList = new UrlPathService().parseApkUrlPath("e:\\1111.txt");
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20, 30, 1, TimeUnit.MINUTES, blockingQueue);
        int index = 1;
        int isPage =1;
        while(true){


            if(threadPoolExecutor.getActiveCount()<10) {
                System.out.println(">>>>>>>>>>>>>>>>>> 第【"+index+"】批");

                List<UrlPathVO> pageList = PageUtil.toPage(index,10,urlPathVOList);
                for (int i = 0; i < pageList.size(); i++) {
                    try {
                        Runnable runnable = new TaskWithoutResult(pageList.get(i));
                        threadPoolExecutor.submit(runnable);
                    }catch (Exception ex){
                        System.out.println(ex);
                    }
                }
                index++;
            }
            Thread.sleep(2000);

        }

        //threadPoolExecutor.shutdown();//不会触发中断
       // threadPoolExecutor.shutdownNow();//会触发中断
    }
}

/**
 * 无返回值的任务
 *
 * @author songxu
 */
class TaskWithoutResult implements Runnable {
    private int sleepTime = 1000;//默认睡眠时间1s

    private UrlPathVO urlPathVO;

    public TaskWithoutResult(UrlPathVO urlPathVO) {
        this.urlPathVO = urlPathVO;
    }

    @Override
    public void run() {
        try {
//            HttpDownloadService mtd = new HttpDownloadService(
//                    urlPathVO.getRequestApkUrlPath(),
//                    "e://apkdown//" + urlPathVO.getApkFileName(),
//                    1);
//
//            long fileSize = mtd.download(104857600);
            URL urlPath = new URL(urlPathVO.getRequestApkUrlPath());
//            File file = new File(dir);
//            if (!file.exists()) {
//                file.mkdirs();
//            }

            long fileSize = getHttpFileLenth(urlPathVO.getRequestApkUrlPath());
            if(fileSize>10000 && fileSize<104857600)
                 FileUtils.copyURLToFile(urlPath, new File("e://apkdown//" + urlPathVO.getApkFileName()));
          //  System.out.println(urlPathVO.getApkFileName()+">>>>"+fileSize);


        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    /**
     * 获取http文件大小
     *
     * @param path 待下载的文件地址
     * @return
     * @throws IOException
     */
    public  long getHttpFileLenth(String path) throws IOException {
        long length = 0;
        try {
            URL url = new URL(path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            length = urlConnection.getContentLengthLong();
            urlConnection.disconnect();
        }catch(Exception ex){
            System.out.println(ex);
        }
        return length;
    }


}