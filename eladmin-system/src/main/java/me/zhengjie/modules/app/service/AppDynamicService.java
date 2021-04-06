package me.zhengjie.modules.app.service;

public interface AppDynamicService {
    /****
     * 远程安装app
     * @param appPath
     * @param appiumUrl appium网关地址
     * @param virtualMachineUrl 模拟器地址
     */
    int installApp(String appPath,String appiumUrl,String virtualMachineUrl);

    /****
     * 卸载App
     * @param packageName
     * @param appiumUrl appium网关地址
     * @param virtualMachineUrl 模拟器地址
     */
    void uninstallApp(String packageName,String appiumUrl,String virtualMachineUrl);

    /***
     * 动态解析app
     * @param appiumUrl
     * @param virtualMachineUrl
     */
    void dynamicApp(String appiumUrl,String virtualMachineUrl);
}
