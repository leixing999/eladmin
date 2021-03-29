package me.zhengjie.modules.app.domain.vo;

import lombok.Data;

@Data
public class AppCsvVO {
    //app应用名
    private String appName;
    //app包名
    private String appPackage;
    //app主类
    private String appMainClass;
    //app下载地址
    private String appDownloadUrl;
    //app获取时间
    private String appTime;
    //拦截的url地址
    private String urlDomains;
}
