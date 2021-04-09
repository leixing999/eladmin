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

    /***
     * 通过应用名和类型判断是否存在过
     * @param appName
     * @param packageName
     * @return
     */
    List<AppTelecomLink> findByAppApplicationNameAndAppPackageName(String appName,String packageName);

    /*****
     * 搜索没有静态分析的APP
     * @param isAnalyse
     * @param isDown
     * @return
     */
    List<AppTelecomLink> findAppTelecomLinkByAppIsAnalyseAndAppIsDown(int isAnalyse,int isDown);

    /***
     * 搜索没有动态分析的APP
     * @param isDown
     * @param isDynamic
     * @return
     */
    List<AppTelecomLink> findAppTelecomLinkByAppIsDownAndAppIsDynamic(int isDown,int isDynamic);

    /****
     * 搜索没有动态分析的APP(针对涉黑名单）
     * @param isDown
     * @param appType
     * @param isDynamic
     * @return
     */
    List<AppTelecomLink> findByAppIsDownAndAppTypeAndAppIsDynamic(int isDown,int appType,int isDynamic);

    /***
     * 对数据进行同步分析（电信涉黑名单）
     * @param isDown
     * @param appType
     * @param isDynamic
     * @param isSync
     * @return
     */
    List<AppTelecomLink> findByAppIsDownAndAppTypeAndAppIsDynamicAndAppIsSync(int isDown,int appType,int isDynamic,int isSync);

    /****
     * 按照指定条件获取指定apk文件
     * @param isDown
     * @param appType
     * @param isAnalyse
     * @return
     */
    List<AppTelecomLink> findByAppIsDownAndAppTypeAndAppIsAnalyse(int isDown,int appType,int isAnalyse);
    /***
     * 搜索没有脱壳分析的APP
     * @param isDown
     * @param isDump
     * @return
     */
    List<AppTelecomLink> findAppTelecomLinkByAppIsDownAndAppIsDump(int isDown,int isDump);

    /****
     * 针对脱壳且是黑名单里得数据进行分析
     * @param isDown
     * @param appType
     * @param isDump
     * @return
     */
    List<AppTelecomLink> findByAppIsDownAndAppTypeAndAppIsDump(int isDown,int appType,int isDump);


    /*****
     * 搜索没有静态分析的APP
     * @param isAnalyse
     * @param isDown
     * @param isSync
     * @return
     */
    List<AppTelecomLink> findAppTelecomLinkByAppIsAnalyseAndAppIsDownAndAppIsSync(int isAnalyse,int isDown,int isSync);

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
            " where  id=:#{#appTelecomLink.id}",nativeQuery = true)
    void updateAppTelecomLink(@Param("appTelecomLink") AppTelecomLink appTelecomLink);

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
            " app_update_time=:#{#appTelecomLink.appUpdateTime}," +
            " app_is_analyse=:#{#appTelecomLink.appIsAnalyse}, " +
            " app_type=:#{#appTelecomLink.appType} " +
            " where  id=:#{#appTelecomLink.id}",nativeQuery = true)
    void updateAppLink(@Param("appTelecomLink") AppTelecomLink appTelecomLink);


    /****
     * 通过对app进行动态分析对此App的参数信息进行跟新
     * @param id
     * @param isDynamic
     */
    @Modifying
    @Transactional
    @Query(value=" update app_telecom_link " +
            " set " +
            " app_is_dynamic=?2 " +
            " where  id=?1",nativeQuery = true)
    void updateAppTelecomLink(String id,Integer isDynamic);

    /****
     * 通过对app进行脱壳分析对此App的参数信息进行跟新
     * @param id
     * @param isDump
     */
    @Modifying
    @Transactional
    @Query(value=" update app_telecom_link " +
            " set " +
            " app_is_dump=?2 " +
            " where  id=?1",nativeQuery = true)
    void updateAppTelecomLinkByDump(String id,Integer isDump);

    /****
     * 通过对app进行同步状态
     * @param id
     * @param isDump
     */
    @Modifying
    @Transactional
    @Query(value=" update app_telecom_link " +
            " set " +
            " app_is_sync=?2 " +
            " where  id=?1",nativeQuery = true)
    void updateAppTelecomLinkBySync(String id,Integer isDump);


    /****
     * 修改指定APP的类型
     * @param id
     * @param appType
     */
    @Modifying
    @Transactional
    @Query(value=" update app_telecom_link " +
            " set " +
            " app_type=?2 " +
            " where  id=?1",nativeQuery = true)
    void updateAppType(String id,Integer appType);

}
