package me.zhengjie.modules.app.repository;

import me.zhengjie.modules.app.domain.po.AppTelecomWhitelist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * AppTelecomWhitelist Repository层
 *
 * @author xinglei
 * @date 2021-02-27 10:52:25
 */
public interface AppTelecomWhitelistRepository extends JpaRepository<AppTelecomWhitelist, String> {

    /***
     * 通过应用名、类名以及包名比对是否APP在白名单中
     * @param appName
     * @param appClassName
     * @param appPackName
     * @return
     */
    List<AppTelecomWhitelist> findByAppApplicationNameAndAndAppClassNameAndAndAppPackageName(String appName, String appClassName, String appPackName);

    /****
     * 判断增加app是否在白名单里
     * @param appName
     * @param appPackageName
     * @return
     */
    List<AppTelecomWhitelist> findByAppApplicationNameAAndAppPackageName(String appName,String appPackageName);
}
