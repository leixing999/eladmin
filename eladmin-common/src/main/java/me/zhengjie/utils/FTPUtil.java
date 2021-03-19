package me.zhengjie.utils;


import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by YangFan.
 * @date 2019/8/7
 * 功能: ftp工具类
 */
public class FTPUtil {

    private Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    public FTPUtil() {

    }

    public FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.username = user;
        this.password = pwd;
    }

    public boolean uploadFile(List<File> fileList, String fileDirectory) throws IOException {
        logger.info("开始连接ftp服务器");
        boolean result = uploadFile(fileDirectory, fileList, "1", "1");
        logger.info("开始连接ftp服务器,结束上传,上传结果:{}");
        return result;
    }

    /***
     *
     * @param fileList
     * @param fileDirectory
     * @param isPassive 1:被动，0：主动模式
     * @return
     * @throws IOException
     */
    public boolean uploadFile(List<File> fileList, String fileDirectory, String isPassive, String isCreateDir) throws IOException {
        logger.info("开始连接ftp服务器");
        System.out.println(isPassive);
        boolean result = uploadFile(fileDirectory, fileList, isPassive, isCreateDir);
        logger.info("开始连接ftp服务器,结束上传,上传结果:{}");
        return result;
    }

    /**
     * 上传文件
     *
     * @param pathname    ftp服务保存地址
     * @param fileName    上传到ftp的文件名
     * @param inputStream 输入文件流
     * @return
     */
    public boolean uploadFile(String pathname, String fileName, InputStream inputStream) {
        boolean flag = false;
        try {
            System.out.println("开始上传文件");
            if (connectServer(this.ip, this.port, this.username, this.password)) {
                makeDirectory(pathname);
                ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
                CreateDirecroty(pathname);
                ftpClient.makeDirectory(pathname);
                ftpClient.changeWorkingDirectory(pathname);
                ftpClient.storeFile(fileName, inputStream);
                inputStream.close();
                ftpClient.logout();
                flag = true;
            }
            System.out.println("上传文件成功");
        } catch (Exception e) {
            System.out.println("上传文件失败");
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    //创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
    public boolean CreateDirecroty(String remote) throws IOException {
        boolean success = true;
        String directory = remote + "/";
        // 如果远程目录不存在，则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(new String(directory))) {
            int start = 0;
            int end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf("/", start);
            String path = "";
            String paths = "";
            while (true) {
                String subDirectory = new String(remote.substring(start, end).getBytes("GBK"),
                        "iso-8859-1");
                path = path + "/" + subDirectory;
                if (!existFile(path)) {
                    if (makeDirectory(subDirectory)) {
                        changeWorkingDirectory(subDirectory);
                    } else {
                        System.out.println("创建目录[" + subDirectory + "]失败");
                        changeWorkingDirectory(subDirectory);
                    }
                } else {
                    changeWorkingDirectory(subDirectory);
                }

                paths = paths + "/" + subDirectory;
                start = end + 1;
                end = directory.indexOf("/", start);
                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
        return success;
    }

    //判断ftp服务器文件是否存在
    public boolean existFile(String path) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        if (ftpFileArr.length > 0) {
            flag = true;
        }
        return flag;
    }

    //创建目录
    public boolean makeDirectory(String dir) {
        boolean flag = true;
        try {
            flag = ftpClient.makeDirectory(dir);
            if (flag) {
                System.out.println("创建文件夹" + dir + " 成功！");

            } else {
                System.out.println("创建文件夹" + dir + " 失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    //改变目录路径
    public boolean changeWorkingDirectory(String directory) {
        boolean flag = true;
        try {
            flag = ftpClient.changeWorkingDirectory(directory);
            if (flag) {
                System.out.println("进入文件夹" + directory + " 成功！");

            } else {
                System.out.println("进入文件夹" + directory + " 失败！开始创建文件夹");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return flag;
    }

    /***
     *
     * @param remotePath
     * @param fileList
     * @param isPassive
     * @return
     * @throws IOException
     */
    private boolean uploadFile(String remotePath, List<File> fileList, String isPassive, String isCreateDir) throws IOException {
        boolean uploaded = true;
        FileInputStream fis = null;
        //连接FTP服务器
        if (connectServer(this.ip, this.port, this.username, this.password)) {
            try {
                if ("1".equals(isCreateDir)) {
                    makeDirectory(remotePath);
                }

                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                if ("1".equals(isPassive)) {
                    ftpClient.enterLocalPassiveMode();

                } else {
                    ftpClient.enterLocalActiveMode();
                }
                for (File fileItem : fileList) {
                    try {
                        fis = new FileInputStream(fileItem);
                        if ("1".equals(isPassive)) {
                            ftpClient.enterLocalPassiveMode();

                        } else {
                            ftpClient.enterLocalActiveMode();
                        }

                        String remote = new String(fileItem.getName().getBytes("GBK"), "iso-8859-1");
                        ftpClient.storeFile(remote, fis);
                    } catch (Exception ex) {
                        logger.error("上传文件异常", ex);
                    } finally {
                        fis.close();
                        fis = null;
                    }
                }

            } catch (IOException e) {
                logger.error("上传文件异常", e);
                uploaded = false;
                e.printStackTrace();
            } finally {

                ftpClient.disconnect();
            }
        }
        return uploaded;
    }

    private InputStream downloadFile(String remotePath, String fileName) {
        InputStream result = null;
        if (connectServer(this.ip, this.port, this.username, this.password)) {
            try {
                int reply;
                reply = ftpClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftpClient.disconnect();
                    return null;
                }
                // 转移到FTP服务器目录
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.changeWorkingDirectory(remotePath);
                FTPFile[] fs = ftpClient.listFiles();
                // 下载文件是否存在
                boolean flag = false;
                for (FTPFile ff : fs) {
                    byte[] bytes = ff.getName().getBytes("iso-8859-1");
                    String name = new String(bytes, "GBK");
                    if (name.equals(fileName)) {
                        result = ftpClient.retrieveFileStream(ff.getName());
                        flag = true;
                    }
                }
                if (!flag) {
                    logger.info("文件: " + fileName + "不存在 ！");
                } else {
                    logger.info("下载成功 ！");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
//                if (ftpClient.isConnected()) {
//                    try {
//                        ftpClient.disconnect();
//                    } catch (IOException ioe) {
//                    }
//                }
            }
        }
        return result;
    }


    /****
     * 得到此路徑下的所有文件信息
     * @param remotePath
     * @return
     */
    public List<String> getFiles(String remotePath) {
       List<String > fileList = new ArrayList<>();
        if (connectServer(this.ip, this.port, this.username, this.password)) {
            try {
                int reply;
                reply = ftpClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftpClient.disconnect();
                    return null;
                }
                // 转移到FTP服务器目录
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.changeWorkingDirectory(remotePath);

                FTPFile[] fs = ftpClient.listFiles();
                for (FTPFile ff : fs) {
                    byte[] bytes = ff.getName().getBytes("iso-8859-1");
                    String name = new String(bytes, "GBK");
                    if(name.length()>3){
                        fileList.add(name);
                    }



                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
               this.close();
            }
        }
        return fileList;
    }



    /**
     * * 删除文件 *
     *
     * @param pathname
     *            FTP服务器保存目录 *
     * @param filename
     *            要删除的文件名称 *
     * @return
     */
    public boolean deleteFile(String pathname, String filename) {
        boolean flag = false;

        if (connectServer(this.ip, this.port, this.username, this.password)) {
            try {
                int reply;
                reply = ftpClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftpClient.disconnect();
                    return false;
                }
                // 转移到FTP服务器目录
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.changeWorkingDirectory(pathname);
                ftpClient.dele(filename);
               // ftpClient.logout();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                this.close();
            }
        }

        return true;

    }
    public void close() {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.disconnect();
            } catch (IOException ioe) {
                logger.error(""+ioe);
            }
        }
    }

    /**
     * 从FTP服务器下载文件
     *
     * @param remotePath FTP服务器上的相对路径
     * @param fileName   要下载的文件名
     * @return 结果
     */
    public InputStream downFile(String remotePath, String fileName) {
        InputStream result = downloadFile(remotePath, fileName);
        return result;
    }

    /****
     * 重载下载方法，支持下载并保存
     * @param remotePath
     * @param fileName
     * @param savePath
     */
    public void downFile(String remotePath, String fileName,String savePath) {

        try {
            BufferedInputStream  inputStream = new BufferedInputStream (this.downFile(remotePath, fileName));
            BufferedOutputStream  outputStream = new BufferedOutputStream(new FileOutputStream(savePath + fileName));

            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }

            inputStream.close();
            outputStream.close();
            //删除文件
            this.deleteFile(remotePath,fileName);
            //关闭连接
            this.close();
        }catch (Exception ex){
            logger.error("下载异常", ex);
        }
    }

    private boolean connectServer(String ip, int port, String user, String pwd) {

        boolean isSuccess = false;

        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user, pwd);
        } catch (IOException e) {
            logger.error("连接FTP服务器异常", e);
        }
        return isSuccess;
    }


    private String ip;
    private int port;
    private String username;
    private String password;
    private FTPClient ftpClient;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
