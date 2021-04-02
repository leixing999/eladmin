package me.zhengjie.modules.app.service.impl;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import me.zhengjie.modules.app.domain.po.AppTelecomLink;
import me.zhengjie.modules.app.service.AppDynamicAnalyseService;
import me.zhengjie.modules.app.service.AppDynamicParseUrlService;
import me.zhengjie.modules.app.service.AppDynamicService;
import me.zhengjie.modules.app.service.AppTelecomLinkService;
import me.zhengjie.utils.FTPUtil;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.util.List;

@Service
public class AppDynamicServiceImpl implements AppDynamicService {

    @Autowired
    AppTelecomLinkService appTelecomLinkService;
    @Autowired
    AppDynamicParseUrlService appDynamicParseUrlService;
    //动态解析请求路径
    @Value("${appDynamic.requestPath}")
    String requestPath;
    //动态解析响应路径
    @Value("${appDynamic.responsePath}")
    String responsePath;


    //FTP下载配置信息
    @Value("${file.apk.ftp.virtualMachineDownload.ip}")
    String downFtpIp;
    @Value("${file.apk.ftp.virtualMachineDownload.port}")
    int downFtpPort;
    @Value("${file.apk.ftp.virtualMachineDownload.user}")
    String downFtpUser;
    @Value("${file.apk.ftp.virtualMachineDownload.password}")
    String downFtpPassword;

    //远程路径
    @Value("${file.apk.ftp.virtualMachineDownload.dir}")
    String dir;
    //ftp文件下载保存路径
    @Value("${appDynamic.apkDynamicLogPath}")
    String apkDynamicLogPath;


    //apk保存的路径
    @Value("${file.apk.appSavePath}")
    String appSavePath;


    //是否开启虚拟路径（1：开启，0：禁用）
    @Value("${appDynamic.isVirtualPath}")
    int isVirtualPath;

    //虚拟路径
    @Value("${appDynamic.virtualPath}")
    String virtualPath;

    /****
     * 删除远程服务的apk动态生成的日志文件
     */
    private void deleteRemoteApkDynamicLog(){
        try{
            FTPUtil ftp = new FTPUtil(this.downFtpIp, this.downFtpPort, this.downFtpUser, this.downFtpPassword);
            ftp.deleteFile(dir,requestPath.substring(requestPath.lastIndexOf("\\")+1));
            ftp.deleteFile(dir,responsePath.substring(responsePath.lastIndexOf("\\")+1));
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    /***
     * 删除本地日志文件
     */
    private void deleteLocalApkDynamicLog(){
        try{
            File resFile = new File(responsePath);
            if(resFile.exists()){
                resFile.delete();
            }

            File reqFile = new File(requestPath);
            if(reqFile.exists()){
                reqFile.delete();
            }
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    /***
     * 下载apk动态生成的日志文件
     */
    private void downloadRemoteApkDynamicLog(){
        long start = System.currentTimeMillis();
        try {
            FTPUtil ftp = new FTPUtil(this.downFtpIp, this.downFtpPort, this.downFtpUser, this.downFtpPassword);
            List<String> fileList = ftp.getFiles(dir);
            for(String fileName : fileList){
                ftp.downFile(dir,fileName,apkDynamicLogPath);
            }


        } catch (Exception ex) {
            System.out.println(ex);
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000);
    }

    /****
     * 远程安装app
     * @param appPath
     * @param appiumUrl appium网关地址
     * @param virtualMachineUrl 模拟器地址
     */
    @Override
    public void installApp(String appPath,String appiumUrl,String virtualMachineUrl) {
        AppiumDriver driver;
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(CapabilityType.BROWSER_NAME, "");
        cap.setCapability("platformName", "Android"); //指定测试平台
        cap.setCapability("deviceName", virtualMachineUrl);

        try {
            if(isVirtualPath==1) {
                cap.setCapability("app", this.virtualPath + appPath);
            }else{
                cap.setCapability("app", appSavePath + appPath);
            }
            driver = new AndroidDriver(new URL(appiumUrl), cap);
            driver.installApp("app");
            driver.quit();

        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    /****
     * 卸载App
     * @param packageName
     * @param appiumUrl appium网关地址
     * @param virtualMachineUrl 模拟器地址
     */
    @Override
    public void uninstallApp(String packageName,String appiumUrl,String virtualMachineUrl) {
        AppiumDriver driver;
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(CapabilityType.BROWSER_NAME, "");
        cap.setCapability("platformName", "Android"); //指定测试平台
        cap.setCapability("deviceName", virtualMachineUrl);

        try {
            driver = new AndroidDriver(new URL(appiumUrl), cap);
            driver.removeApp(packageName);
            driver.quit();

        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    /***
     * 动态解析app
     * @param appiumUrl
     * @param virtualMachineUrl
     */
    @Override
    public void dynamicApp(String appiumUrl,String virtualMachineUrl) {

        List<AppTelecomLink> list = appTelecomLinkService.findAppLinkByDynamicConditions(1,1,0);
        for(AppTelecomLink appLink : list){
            //解析静态分析完成的APP
            if(appLink.getAppIsAnalyse()==1) {
                int isDynamic = 1;
                try {
                    //清空动态解析APP日志文件（1）
                    this.deleteRemoteApkDynamicLog();
                    this.deleteLocalApkDynamicLog();

                    String sysRelativeFilePath = appLink.getAppSysRelativePath()+ File.separator+appLink.getAppSysFileName();
                    //安装APP（2）
                    this.installApp(sysRelativeFilePath, appiumUrl, virtualMachineUrl);
                    Thread.sleep(10000);
                    //卸载APP(3)
                    this.uninstallApp(appLink.getAppPackageName(), appiumUrl, virtualMachineUrl);
                    //获取动态解析APP日志文件(4)
                    this.downloadRemoteApkDynamicLog();
                    //动态解析APP（5）
                    appDynamicParseUrlService.saveAppDynamicAnylasisResult(responsePath,requestPath,appLink.getId());

                }catch (Exception ex){
                    System.out.println(ex);
                    isDynamic = -1;
                }finally {
                    //更新动态解析状态（6）
                    appTelecomLinkService.updateAppTelecomLink(appLink.getId(),isDynamic);
                }
            }

        }
    }
}
