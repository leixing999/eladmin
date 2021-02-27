package me.zhengjie.modules.app.repository;

import me.zhengjie.modules.app.domain.po.AppTelecomWhitelistPermission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * AppTelecomWhitelistPermission Repository层
 *
 * @author xinglei
 * @date 2021-02-27 11:05:58
 */
public interface AppTelecomWhitelistPermissionRepository extends JpaRepository<AppTelecomWhitelistPermission, String> {

}
