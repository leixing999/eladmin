package me.zhengjie.modules.app.service.impl;

import me.zhengjie.modules.app.domain.po.AppPermission;
import me.zhengjie.modules.app.repository.AppPermissionRepository;
import me.zhengjie.modules.app.service.AppPermissionService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

	/****
	 * 批量保存app权限信息
	 * @param list
	 */
	@Override
	public void saveBatchAppPermission(List<AppPermission> list) {
		appPermissionRepository.saveAll(list);
	}

	@Override
	public void saveBatchAppPermission(String appId, List<String> list) {
		//获取APP权限列表
		List<AppPermission> permissionList = new ArrayList<>();
		for (String permission : list) {
			AppPermission appPermission = new AppPermission();
			appPermission.setId(UUID.randomUUID().toString());
			appPermission.setAppLinkId(appId);
			appPermission.setAppPermissionName(permission);
			permissionList.add(appPermission);
		}
		//批量保存APP权限信息
		this.saveBatchAppPermission(permissionList);
	}
}
