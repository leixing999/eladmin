package me.zhengjie.modules.app.repository;

import me.zhengjie.modules.app.domain.po.AppPermission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * AppPermission Repository层
 *
 * @author xinglei
 * @date 2021-02-26 16:11:46
 */
public interface AppPermissionRepository extends JpaRepository<AppPermission, String> {

}
