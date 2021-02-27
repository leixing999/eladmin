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

     void saveAppWhite(AppTelecomWhitelist appTelecomWhitelist);
     void saveAppWhiteAndPermission(AppTelecomWhitelist appTelecomWhitelist, List<AppTelecomWhitelistPermission> appTelecomWhitelistPermissionList);
     void runAppWhiteList(ApkInfo apkInfo);

}
