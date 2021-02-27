package me.zhengjie.modules.app.service;

import me.zhengjie.modules.app.domain.po.AppTelecomWhitelistPermission;
import me.zhengjie.modules.app.repository.AppTelecomWhitelistPermissionRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * AppTelecomWhitelistPermission service层
 *
 * @author xinglei
 * @date 2021-02-27 11:05:58
 */
public interface AppTelecomWhitelistPermissionService {

    void saveAppWhitePermission(AppTelecomWhitelistPermission appTelecomWhitelistPermission);

}
