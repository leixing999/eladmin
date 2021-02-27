package me.zhengjie.modules.app.service;

import org.springframework.beans.factory.annotation.Value;

public interface AppService {

     /***
      * 静态解析apk包信息
      */
     void runAppWhiteList();
}
