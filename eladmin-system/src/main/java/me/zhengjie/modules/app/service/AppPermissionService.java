package me.zhengjie.modules.app.service;

import me.zhengjie.modules.app.domain.po.AppPermission;
import me.zhengjie.modules.app.repository.AppPermissionRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * AppPermission service层
 *
 * @author xinglei
 * @date 2021-02-26 16:11:46
 */
public interface AppPermissionService {

    /***
     * 保存单条权限信息
     * @param appPermission
     */
    public void saveAppPermission(AppPermission appPermission);

    /***
     * 批量保存权限信息
     * @param list
     */
    public void saveBatchAppPermission(List<AppPermission> list);

    /***
     * 批量保存权限信息（重载）
     * @param appId
     * @param list
     */
    public void saveBatchAppPermission(String appId,List<String> list);
}
