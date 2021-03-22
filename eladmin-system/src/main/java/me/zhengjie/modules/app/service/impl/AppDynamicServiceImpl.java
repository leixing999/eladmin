package me.zhengjie.modules.app.service.impl;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import me.zhengjie.modules.app.domain.po.AppTelecomLink;
import me.zhengjie.modules.app.service.AppDynamicAnalyseService;
import me.zhengjie.modules.app.service.AppDynamicParseUrlService;
import me.zhengjie.modules.app.service.AppDynamicService;
import me.zhengjie.modules.app.service.AppTelecomLinkService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
            cap.setCapability("app", appPath);
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

        List<AppTelecomLink> list = appTelecomLinkService.findAppLinkByDynamicConditions(1,0);
        for(AppTelecomLink appLink : list){
            //解析静态分析完成的APP
            if(appLink.getAppIsAnalyse()==1) {
                int isDynamic = 1;
                try {
                    //清空动态解析APP日志文件（1）
                    //安装APP（2）
                    this.installApp(appLink.getAppFileName(), appiumUrl, virtualMachineUrl);
                    //获取动态解析APP日志文件(3)
                    //动态解析APP（4）
                    appDynamicParseUrlService.saveAppDynamicAnylasisResult(responsePath,requestPath,appLink.getId());
                    //卸载APP(5)
                    this.uninstallApp(appLink.getAppPackageName(), appiumUrl, virtualMachineUrl);
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
