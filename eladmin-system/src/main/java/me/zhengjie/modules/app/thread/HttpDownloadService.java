package me.zhengjie.modules.app.thread;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpDownloadService {
    private String str_url;
    private String storagePath;

    private String appSysFileName;
    private String appSysRelativeFileName;
    private int threadNumber;
    private long downloadByteCount;
    private int failThreadNum ;
    private boolean isDown;
    private boolean isLimit;

   public HttpDownloadService(String str_url, String storagePath, int threadNumber,String appSysFileName) {
        this.str_url = str_url;
        this.storagePath = storagePath;
        this.appSysFileName = appSysFileName;
        this.threadNumber = threadNumber;
        this.failThreadNum = 0;
        this.isDown = false;
        this.isLimit = false;
    }
    public int getFailThreadNum(){
        return failThreadNum;
    }

    public boolean getIsDown(){
        return isDown;
    }

    public boolean getIsLimit(){
        return isLimit;
    }

    /***
     *
     * @param maxFileSize
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public long download(long maxFileSize) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        System.out.println("Download......");

        /*
         *  首先设置本地文件的大小
         *  当然这是个null数据的文件
         *  这样才能通过RandomAccessFile的数组下标机制达到随机位置写入
         */
        URL url = new URL(str_url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        long fileLength = conn.getContentLengthLong(); // 得到需要下载的文件大小
        conn.disconnect();
        if(fileLength>maxFileSize ){
            this.isLimit = true;
            return fileLength;
        }
        if( fileLength<10000){
            return fileLength;
        }

        File sysFileDir = new File(storagePath);
        if(sysFileDir.isDirectory()==false){
            sysFileDir.mkdir();
        }
        RandomAccessFile file = new RandomAccessFile(storagePath+File.separator+this.appSysFileName, "rwd");
        file.setLength(fileLength); // 关键方法 ： 设置本地文件长度
        file.close();

        /*
         *  计算每条线程下载的字节数，以及每条线程起始下载位置与结束的下载位置，
         *  因为不一定平均分，所以最后一条线程下载剩余的字节
         *  然后创建线程任务并启动
         *  Main线程等待每条线程结束(join()方法)
         */
        long oneThreadReadByteLength = fileLength / threadNumber;
        for (int i = 0; i < threadNumber; i++) {
            long startPosition = i * oneThreadReadByteLength;
            long endPosition = i == threadNumber - 1 ? fileLength : (i + 1) * oneThreadReadByteLength - 1;
            actualDownload(startPosition,endPosition);
        }

        /*
         *  检查文件是否下载完整，不完整则删除
         */
        if (downloadByteCount == fileLength) {
            this.isDown = true;
            System.out.println("ALL Thread Download OK.");
            System.out.println("time = " + ((System.currentTimeMillis() - startTime) / 1000) + " S");
        } else {
            failThreadNum++;
            System.out.println("Download Error.");
            new File(storagePath).delete();
        }
        return downloadByteCount;
    }

    /***
     * 实际下载方法
     * @param startPosition
     * @param endPosition
     */
    public void actualDownload(long startPosition,long endPosition ){
        try{
            URL url = new URL(str_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Length","8192");
            conn.setRequestProperty("Range", "bytes=" + startPosition + "-" + endPosition); // 关键方法: 每条线程请求的字节范围
            if (conn.getResponseCode() == HttpURLConnection.HTTP_PARTIAL) { // 关键响应码 ：206，请求成功 + 请求数据字节范围成功
                RandomAccessFile file = new RandomAccessFile(storagePath+File.separator+this.appSysFileName, "rwd");
                file.seek(startPosition); // 关键方法 ：每条线程起始写入文件的位置
                InputStream in = conn.getInputStream();
                byte[] buf = new byte[8*1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    file.write(buf, 0, len);
                    downloadByteCount += len;
                }
                // 关闭网络连接及本地流
                in.close();
                file.close();
                conn.disconnect();
                System.out.println(Thread.currentThread().getName() + ": download OK");
            }
        } catch (IOException e) {
            failThreadNum++;
            System.out.println(Thread.currentThread().getName() + "_Error : " + e);
        }
    }
}
