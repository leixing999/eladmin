package me.zhengjie.modules.app.service.impl;

import me.zhengjie.domain.ApkInfo;
import me.zhengjie.modules.app.service.AppDownloadService;
import me.zhengjie.modules.app.service.AppDynamicService;
import me.zhengjie.modules.app.service.AppTelecomLinkService;
import me.zhengjie.modules.app.service.AppTelecomWhitelistService;
import me.zhengjie.utils.ApkUtil;
import me.zhengjie.utils.FTPUtil;
import me.zhengjie.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

@Service
public class AppService implements me.zhengjie.modules.app.service.AppService {
    /***aapt路径***/
    @Value("${file.aapt.path}")
    String aaptPath;
    /***apk路径***/
    @Value("${file.apk.path}")
    String apkPath;


    //FTP下载配置信息
    @Value("${file.apk.ftp.download.ip}")
    String downFtpIp;
    @Value("${file.apk.ftp.download.port}")
    int downFtpPort;
    @Value("${file.apk.ftp.download.user}")
    String downFtpUser;
    @Value("${file.apk.ftp.download.password}")
    String downFtpPassword;

    //远程路径
    @Value("${file.apk.ftp.download.dir}")
    String dir;
    //ftp文件下载保存路径
    @Value("${file.apk.urlPath}")
    String urlPath;


    //appium请求网关地址
    @Value("${appVirtualMachine.appiumUrl}")
    String appiumUrl;
    //app 模拟器地址
    @Value("${appVirtualMachine.appVirtualMachineUrl}")
    String appVirtualMachineUrl;

    @Autowired
    AppTelecomWhitelistService appTelecomWhitelistService;
    @Autowired
    AppDownloadService appDownloadService;

    @Autowired
    AppTelecomLinkService appTelecomLinkService;


    @Autowired
    AppDynamicService appDynamicService;

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
        for (File file : files) {
            try {
                ApkInfo apkInfo = new ApkUtil(aaptPath).getApkInfo(file.getPath());
                String fileName = file.getName();
                appTelecomWhitelistService.runAppWhiteList(apkInfo, fileName);
            } catch (Exception ex) {
                System.out.println(ex);
            } finally {
                //删除静态分析入库文件
                // file.delete();
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
    /***
     * 动态分析App信息
     */
    @Override
    public void dynamicAnalyseApp() {
        appDynamicService.dynamicApp(appiumUrl,appVirtualMachineUrl);
    }

    /****
     * Ftp下载服务
     */
    @Override
    public void ftpDownloadAppUrlFiles() {


        long start = System.currentTimeMillis();
        try {
            FTPUtil ftp = new FTPUtil(this.downFtpIp, this.downFtpPort, this.downFtpUser, this.downFtpPassword);
            List<String> fileList = ftp.getFiles(dir);

            for(String fileName : fileList){
                ftp.downFile(dir,fileName,urlPath);
            }


        } catch (Exception ex) {
            System.out.println(ex);
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000);
    }

}
