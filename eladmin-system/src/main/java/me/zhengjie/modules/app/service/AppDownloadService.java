package me.zhengjie.modules.app.service;

import me.zhengjie.modules.app.domain.po.AppTelecomLinkPackage;

import java.io.File;
import java.util.List;

public interface AppDownloadService {
    /*****
     * 扫描电信传递的可疑诈骗APK下载地址文件
     * @return
     */
    File[] searchAppUrlFile();

    /****
     * 保存指定的电信传递的可疑诈骗APK文件
     * @param file
     */
    void saveAppUrlFile(File file);

    /****
     *保存电信传递的可疑诈骗APK文件地址信息
     */
    void saveAppUrlFiles();

    /****
     * 解析电信传递App Url 文件
     * @param linkPackage
     */
    void parseAppUrlFiles(AppTelecomLinkPackage linkPackage);

    /***
     * 解析电信传递可疑电信诈骗 App URL 文件
     */
    void parseAppUrlFiles();
}
