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

    public void saveAppPermission(AppPermission appPermission);
    public void saveBatchAppPermission(List<AppPermission> list);
}
