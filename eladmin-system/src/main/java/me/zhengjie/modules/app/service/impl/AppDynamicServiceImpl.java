package me.zhengjie.modules.app.service.impl;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import me.zhengjie.modules.app.domain.po.AppTelecomLink;
import me.zhengjie.modules.app.service.AppDynamicService;
import me.zhengjie.modules.app.service.AppTelecomLinkService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;

@Service
public class AppDynamicServiceImpl implements AppDynamicService {

    @Autowired
    AppTelecomLinkService appTelecomLinkService;

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

    /****
     * 动态解析app
     */
    @Override
    public void dynamicApp() {

        List<AppTelecomLink> list = appTelecomLinkService.findAppLinkByDynamicConditions(1,0);
    }
}
