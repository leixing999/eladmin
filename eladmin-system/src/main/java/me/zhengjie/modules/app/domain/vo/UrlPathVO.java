package me.zhengjie.modules.app.domain.vo;

/***
 * apk文本文件
 * 每行对应的url 地址信息
 */
public class UrlPathVO {
    //原始url未解码路径
    private String orignUrlPath;
    //解码url路径
    private String decodeUrlPath;
    //实际apk文件路径
    private String requestApkUrlPath;
    //每个url对应的apk文件名
    private String apkFileName;
    //判断路径是否存在apk（true存在，false不存在）
    private boolean isApk;
    //判断文件是否下载成功(true成功，false失败）
    private boolean isDown;
    //文件对应大小
    private long fileSize;

    //广告数量
    private long adNum;
    //访问数
    private long visitNum;

    public String getOrignUrlPath() {
        return orignUrlPath;
    }

    public void setOrignUrlPath(String orignUrlPath) {
        this.orignUrlPath = orignUrlPath;
    }

    public String getDecodeUrlPath() {
        return decodeUrlPath;
    }

    public void setDecodeUrlPath(String decodeUrlPath) {
        this.decodeUrlPath = decodeUrlPath;
    }

    public String getRequestApkUrlPath() {
        return requestApkUrlPath;
    }

    public void setRequestApkUrlPath(String requestApkUrlPath) {
        this.requestApkUrlPath = requestApkUrlPath;
    }

    public String getApkFileName() {
        return apkFileName;
    }

    public void setApkFileName(String apkFileName) {
        this.apkFileName = apkFileName;
    }

    public boolean isApk() {
        return isApk;
    }

    public void setApk(boolean apk) {
        isApk = apk;
    }

    public boolean isDown() {
        return isDown;
    }

    public void setDown(boolean down) {
        isDown = down;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getAdNum() {
        return adNum;
    }

    public void setAdNum(long adNum) {
        this.adNum = adNum;
    }

    public long getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(long visitNum) {
        this.visitNum = visitNum;
    }
}
