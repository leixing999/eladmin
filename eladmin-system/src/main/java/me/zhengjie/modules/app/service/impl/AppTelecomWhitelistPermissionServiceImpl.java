package me.zhengjie.modules.app.service.impl;

import me.zhengjie.modules.app.domain.po.AppTelecomWhitelistPermission;
import me.zhengjie.modules.app.repository.AppTelecomWhitelistPermissionRepository;
import me.zhengjie.modules.app.service.AppTelecomWhitelistPermissionService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

/**
 * AppTelecomWhitelistPermission 自定义Service实现层
 * 白名单App 权限操作服务类
 * @author xinglei
 * @date 2021-02-27 11:05:58
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "appTelecomWhitelistPermission")
public class AppTelecomWhitelistPermissionServiceImpl implements AppTelecomWhitelistPermissionService {

	private final AppTelecomWhitelistPermissionRepository appTelecomWhitelistPermissionRepository;

	/***
	 * 增加白名单app 解析出来的权限信息
	 * @param appTelecomWhitelistPermission
	 */
	@Override
	public void saveAppWhitePermission(AppTelecomWhitelistPermission appTelecomWhitelistPermission) {
		appTelecomWhitelistPermissionRepository.save(appTelecomWhitelistPermission);
	}

	@Override
	public void saveBatchAppWhitePermission(List<AppTelecomWhitelistPermission> list) {
		appTelecomWhitelistPermissionRepository.saveAll(list);
	}
}
