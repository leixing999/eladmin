package me.zhengjie.modules.app.service;

import me.zhengjie.domain.ApkInfo;
import me.zhengjie.modules.app.domain.po.AppTelecomWhitelist;
import me.zhengjie.modules.app.domain.po.AppTelecomWhitelistPermission;
import me.zhengjie.modules.app.repository.AppTelecomWhitelistRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * AppTelecomWhitelist service层
 *
 * @author xinglei
 * @date 2021-02-27 10:52:25
 */
public interface AppTelecomWhitelistService {

     /***
      * 增加app 白名单库
      * @param appTelecomWhitelist
      */
     void saveAppWhite(AppTelecomWhitelist appTelecomWhitelist);

     /****
      * 增加APP 白名单库下的权限信息
      * @param appTelecomWhitelist
      * @param appTelecomWhitelistPermissionList
      */
     void saveAppWhiteAndPermission(AppTelecomWhitelist appTelecomWhitelist, List<AppTelecomWhitelistPermission> appTelecomWhitelistPermissionList);

     /****
      * 对APP进行静态分析
      * @param apkInfo
      * @param fileName
      */
     void runAppWhiteList(ApkInfo apkInfo,String fileName);

}
