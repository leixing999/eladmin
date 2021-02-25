package me.zhengjie.modules.app.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.modules.app.domain.po.AppTelecomLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * AppTelecomLink Repository层
 *
 * @author xinglei
 * @date 2021-02-24 18:20:06
 */
public interface AppTelecomLinkRepository extends JpaRepository<AppTelecomLink, String> {

    /****
     * 通过AppFileName查找App链接信息
     * @param appFileName
     * @return
     */
    List<AppTelecomLink> findAppTelecomLinkByAppFileName(String appFileName);

    /****
     * 通过对app进行静态分析对此App的参数信息进行跟新
     * @param appTelecomLink
     */
    @Modifying
    @Transactional
    @Query(value=" update app_telecom_link " +
            " set " +
            " app_class_name=:#{#appTelecomLink.appClassName}," +
            " app_application_name=:#{#appTelecomLink.appApplicationName}," +
            " app_package_name=:#{#appTelecomLink.appPackageName}," +
            " app_version=:#{#appTelecomLink.appVersion}," +
            " app_update_time=:#{#appTelecomLink.appUpdateTime}" +
            " where  app_file_name=:#{#appTelecomLink.appFileName}",nativeQuery = true)
    void updateAppTelecomLink(@Param("appTelecomLink") AppTelecomLink appTelecomLink);

}
