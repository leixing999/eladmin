package me.zhengjie;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class AppDemo {
    private AppiumDriver driver;

    @BeforeClass
    public void setup() throws Exception {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(CapabilityType.BROWSER_NAME, "");
        cap.setCapability("platformName", "Android"); //指定测试平台
        cap.setCapability("deviceName", "127.0.0.1:62001"); //指定测试机的ID,通过adb命令`adb devices`获取
//        cap.setCapability("platformVersion", "7");


        //将上面获取到的包名和Activity名设置为值
        cap.setCapability("appPackage", "com.niwodai.universityloan");
        cap.setCapability("appActivity", "com.niwodai.loan.lead.WelComeAcV349");

//        //A new session could not be created的解决方法
//        cap.setCapability("appWaitActivity", "com.meizu.flyme.calculator.Calculator");
//        //每次启动时覆盖session，否则第二次后运行会报错不能新建session
//        cap.setCapability("sessionOverride", true);

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), cap);
    }

    @Test
    public void plus() throws Exception {
        Thread.sleep(6000);
//        int width = driver.manage().window().getSize().width;
//        int height = driver.manage().window().getSize().height;
//        int x0 = (int)(width * 0.8);  // 起始x坐标
//        int x1 = (int)(height * 0.2);  // 终止x坐标
//        int y = (int)(height * 0.5);  // y坐标
//        for (int i=0; i<5; i++) {
//            driver.swipe(x0, y, x1, y, 500);
//            Thread.sleep(1000);
//        }
//
//        driver.findElementById("com.youdao.calculator:id/guide_button").click();
//        for (int i=0; i<6; i++) {
//            driver.findElementByXPath("//android.webkit.WebView[@text='Mathbot Editor']").click();
//            Thread.sleep(1000);
//        }
//
//        String btn_xpath = "//*[@resource-id='com.youdao.calculator:id/view_pager_keyboard']/android.widget.GridView/android.widget.FrameLayout[%d]/android.widget.FrameLayout";
//        driver.findElementByXPath(String.format(btn_xpath, 7)).click();
//        driver.findElementByXPath(String.format(btn_xpath, 10)).click();
//        driver.findElementByXPath(String.format(btn_xpath, 8)).click();
//        Thread.sleep(3000);

//        List<WebElement> webList1 = driver.findElements("/");
//      JsonObject json = driver.getSettings();
//        Map<String,Object> maps = driver.getAppStringMap();
        List<WebElement> webList2 =  driver.findElementsByClassName("android.widget.TextView");
        List<WebElement> webList =  driver.findElementsByClassName("android.widget.Button");
//        List<WebElement> webList3 =  driver.findElementsByLinkText("我");


        for(WebElement web : webList){
           System.out.println(web.getText());
       }

    }

    @AfterClass
    public void tearDown() throws Exception {

        driver.quit();

    }
}
