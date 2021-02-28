package me.zhengjie.modules.app.service.impl;

import me.zhengjie.domain.ApkInfo;
import me.zhengjie.modules.app.service.AppDownloadService;
import me.zhengjie.modules.app.service.AppTelecomLinkService;
import me.zhengjie.modules.app.service.AppTelecomWhitelistService;
import me.zhengjie.utils.ApkUtil;
import me.zhengjie.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class AppService implements me.zhengjie.modules.app.service.AppService {
    /***aapt路径***/
    @Value("${file.aapt.path}")
    String aaptPath;
    /***apk路径***/
    @Value("${file.apk.path}")
    String apkPath;

    @Autowired
    AppTelecomWhitelistService appTelecomWhitelistService;
    @Autowired
    AppDownloadService appDownloadService;

    @Autowired
    AppTelecomLinkService appTelecomLinkService;
    /****
     *保存电信传递的可疑诈骗APK文件地址信息
     */
    @Override
    public void saveAppUrlFiles() {
        appDownloadService.saveAppUrlFiles();
    }

    @Override
    public void parseAppUrlFiles() {

        appDownloadService.parseAppUrlFiles();
    }

    /***
     * 静态解析apk包信息
     */
    @Override
    public void runAppWhiteList() {

            File[] files = FileUtil.search(apkPath);
            for(File file : files){
                try {
                    ApkInfo apkInfo = new ApkUtil(aaptPath).getApkInfo(file.getPath());
                    String fileName = file.getName();
                    appTelecomWhitelistService.runAppWhiteList(apkInfo,fileName);
                }catch (Exception ex){
                    System.out.println(ex);
                }finally {
                    //删除静态分析入库文件
                    file.delete();
                }
            }

    }

    /****
     * 静态分析APP
     */
    @Override
    public void staticAnalyseApp() {
        appTelecomLinkService.staticAnalyseApp();
    }

}
