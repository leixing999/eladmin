package me.zhengjie.modules.app.service.impl;

import me.zhengjie.domain.ApkInfo;
import me.zhengjie.modules.app.domain.po.AppTelecomWhitelist;
import me.zhengjie.modules.app.domain.po.AppTelecomWhitelistPermission;
import me.zhengjie.modules.app.repository.AppTelecomWhitelistRepository;
import me.zhengjie.modules.app.service.AppTelecomLinkService;
import me.zhengjie.modules.app.service.AppTelecomWhitelistPermissionService;
import me.zhengjie.modules.app.service.AppTelecomWhitelistService;
import me.zhengjie.utils.HashUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;

import java.util.ArrayList;
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
	@Value("${file.apk.isPermission}")
	int isPermission;


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
			appWhiteId = ""+ HashUtil.uuid(appWhiteId);
			appTelecomWhitelist.setId(appWhiteId);
			this.saveAppWhite(appTelecomWhitelist);
			///是否开启权限入库
			if(isPermission==1){
				for(AppTelecomWhitelistPermission appTelecomWhitelistPermission : appTelecomWhitelistPermissionList){
				String appWhitePermissionId = appWhiteId + appTelecomWhitelistPermission.getPermission();
				appWhitePermissionId = ""+HashUtil.uuid(appWhitePermissionId);
				appTelecomWhitelistPermission.setId(appWhitePermissionId);
				appTelecomWhitelistPermission.setRelId(appWhiteId);
			  }
				appTelecomWhitelistPermissionService.saveBatchAppWhitePermission(appTelecomWhitelistPermissionList);
			}


		}catch(Exception ex){

			System.out.println(ex);
		}
	}

	@Override
	public void runAppWhiteList(ApkInfo apkInfo,String fileName) {
		AppTelecomWhitelist appTelecomWhitelist = new AppTelecomWhitelist();
		appTelecomWhitelist.setAppApplicationName(apkInfo.getApplicationLable());
		appTelecomWhitelist.setAppClassName(apkInfo.getLaunchableActivity());
		appTelecomWhitelist.setAppPackageName(apkInfo.getPackageName());
		appTelecomWhitelist.setStatus(1);
		appTelecomWhitelist.setAppFilename(fileName);
		appTelecomWhitelist.setAppVersion("");
		List<AppTelecomWhitelistPermission> permissionList = new ArrayList<>();
		for(String permission : apkInfo.getUsesPermissions()){
			AppTelecomWhitelistPermission appTelecomWhitelistPermission = new AppTelecomWhitelistPermission();
			appTelecomWhitelistPermission.setPermission(permission);
			permissionList.add(appTelecomWhitelistPermission);
		}
		this.saveAppWhiteAndPermission(appTelecomWhitelist,permissionList);

	}


}
