package me.zhengjie.modules.app.service.impl;

import me.zhengjie.modules.app.domain.po.AppTelecomWhitelist;
import me.zhengjie.modules.app.domain.po.AppTelecomWhitelistPermission;
import me.zhengjie.modules.app.repository.AppTelecomWhitelistRepository;
import me.zhengjie.modules.app.service.AppTelecomWhitelistPermissionService;
import me.zhengjie.modules.app.service.AppTelecomWhitelistService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

/**
 * AppTelecomWhitelist 自定义Service实现层
 * App白名单服务类
 * @author xinglei
 * @date 2021-02-27 10:52:25
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "appTelecomWhitelist")
public class AppTelecomWhitelistServiceImpl implements AppTelecomWhitelistService {

	private final AppTelecomWhitelistRepository appTelecomWhitelistRepository;
	private final AppTelecomWhitelistPermissionService appTelecomWhitelistPermissionService;


	/***
	 * 增加 app 白名单
	 * @param appTelecomWhitelist
	 */
	@Override
	public void saveAppWhite(AppTelecomWhitelist appTelecomWhitelist) {
		appTelecomWhitelistRepository.save(appTelecomWhitelist);
	}

	/****
	 * app 白名单和App白名单权限综合方法类
	 * @param appTelecomWhitelist app白名单
	 * @param appTelecomWhitelistPermissionList app白名单下的权限信息
	 */
	@Override
	public void saveAppWhiteAndPermission(AppTelecomWhitelist appTelecomWhitelist, List<AppTelecomWhitelistPermission> appTelecomWhitelistPermissionList) {

		try{
			String appWhiteId = appTelecomWhitelist.getAppApplicationName()+"_"+appTelecomWhitelist.getAppClassName()+"_"+appTelecomWhitelist.getAppPackageName();
			appTelecomWhitelist.setId(appWhiteId);
			this.saveAppWhite(appTelecomWhitelist);
			for(AppTelecomWhitelistPermission appTelecomWhitelistPermission : appTelecomWhitelistPermissionList){
				String appWhitePermissionId = appWhiteId + appTelecomWhitelistPermission.getPermission();
				appTelecomWhitelistPermission.setId(appWhitePermissionId);
				appTelecomWhitelistPermissionService.saveAppWhitePermission(appTelecomWhitelistPermission);
			}

		}catch(Exception ex){

			System.out.println(ex);
		}
	}


}