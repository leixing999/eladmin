package me.zhengjie.modules.app.repository;

import me.zhengjie.modules.app.domain.po.AppTelecomLink;
import me.zhengjie.modules.app.domain.po.AppTelecomLinkPackage;
import me.zhengjie.modules.system.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * AppTelecomLinkPackage Repository层
 *
 * @author xinglei
 * @date 2021-02-24 18:20:01
 */
public interface AppTelecomLinkPackageRepository extends JpaRepository<AppTelecomLinkPackage, String>, JpaSpecificationExecutor<AppTelecomLinkPackage> {
    /****
     * 按照文件名找指定电信App解析包是
     * @param linkPackageName
     * @return
     */

    List<AppTelecomLinkPackage> findAppTelecomLinkPackageByLinkPackageName(String linkPackageName);

    /****
     * 按照解析状态待处理的文件
     * 默认找status(0)的文件
     * @param status
     * @return
     */
    List<AppTelecomLinkPackage> findAppTelecomLinkPackageByLinkPackageStatus(Integer status);

    /****
     * 修改电信解析包状态
     * @param id
     * @param currentLines
     */
    @Modifying
    @Transactional
    @Query(value=" update app_telecom_link_package set link_package_parse_line= ?2 where id= ?1 ",nativeQuery = true)
    void updateLinkPackage( String id, Integer currentLines);

    /***
     * 修改电信解析包当前行和开始时间
     * @param id
     * @param beginTime
     * @param currentLines
     */
    @Modifying
    @Transactional
    @Query(value=" update app_telecom_link_package " +
                 " set link_package_parse_line= ?3 ," +
                 " link_package_parse_begin_time=?2 " +
                 " where id= ?1 ",nativeQuery = true)
    void updateLinkPackage(@Param("id") String id, @Param("beginTime")  Date beginTime,@Param("currentLines") Integer currentLines);


    /****
     * 修改电信解析包状态、当前行和状态
     * @param id
     * @param endTime
     * @param currentLines
     * @param status
     */
    @Modifying
    @Transactional
    @Query(value=" update app_telecom_link_package " +
            " set link_package_parse_line= ?3 ," +
            " link_package_parse_end_time=?2 ," +
            " link_package_status=?4" +
            " where id= ?1 ",nativeQuery = true)
    void updateLinkPackage(String id, Date endTime, Integer currentLines,Integer status);



}
