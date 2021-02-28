package me.zhengjie.modules.app.service;

import org.springframework.beans.factory.annotation.Value;

public interface AppService {

     /****
      *保存电信传递的可疑诈骗APK文件地址信息
      */
     void saveAppUrlFiles();
     /***
      * 解析电信传递可疑电信诈骗 App URL 文件
      */
     void parseAppUrlFiles();

     /***
      * 静态解析apk包信息
      */
     void runAppWhiteList();
}
