package me.zhengjie.modules.app.repository;

import me.zhengjie.modules.app.domain.po.AppTelecomWhitelist;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * AppTelecomWhitelist Repository层
 *
 * @author xinglei
 * @date 2021-02-27 10:52:25
 */
public interface AppTelecomWhitelistRepository extends JpaRepository<AppTelecomWhitelist, String> {

}
