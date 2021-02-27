package me.zhengjie.modules.app.service.impl;

import me.zhengjie.domain.ApkInfo;
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

    /***
     * 静态解析apk包信息
     */
    @Override
    public void runAppWhiteList() {

            File[] files = FileUtil.search(apkPath);
            for(File file : files){
                try {
                    ApkInfo apkInfo = new ApkUtil(aaptPath).getApkInfo(file.getPath());
                    appTelecomWhitelistService.runAppWhiteList(apkInfo);
                }catch (Exception ex){

                    System.out.println(ex);
                }
            }


    }
}
