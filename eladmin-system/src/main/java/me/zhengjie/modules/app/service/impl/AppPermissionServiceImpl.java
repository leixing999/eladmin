package me.zhengjie.modules.app.service.impl;

import me.zhengjie.modules.app.domain.po.AppPermission;
import me.zhengjie.modules.app.repository.AppPermissionRepository;
import me.zhengjie.modules.app.service.AppPermissionService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;

/**
 * AppPermission 自定义Service实现层
 *
 * @author xinglei
 * @date 2021-02-26 16:11:46
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "appPermission")
public class AppPermissionServiceImpl implements AppPermissionService {

	private final AppPermissionRepository appPermissionRepository;
	public void saveAppPermission(AppPermission appPermission){
		appPermissionRepository.save(appPermission);
	}
}
