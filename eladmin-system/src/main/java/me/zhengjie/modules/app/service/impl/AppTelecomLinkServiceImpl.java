package me.zhengjie.modules.app.service.impl;

import me.zhengjie.domain.ApkInfo;
import me.zhengjie.modules.app.domain.po.AppPermission;
import me.zhengjie.modules.app.domain.po.AppTelecomLink;
import me.zhengjie.modules.app.repository.AppTelecomLinkRepository;
import me.zhengjie.modules.app.repository.AppTelecomWhitelistRepository;
import me.zhengjie.modules.app.service.AppDictService;
import me.zhengjie.modules.app.service.AppPermissionService;
import me.zhengjie.modules.app.service.AppTelecomLinkService;
import me.zhengjie.utils.ApkUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * AppTelecomLink 自定义Service实现层
 *
 * @author xinglei
 * @date 2021-02-24 18:20:06
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "appTelecomLink")
public class AppTelecomLinkServiceImpl implements AppTelecomLinkService {

	private final AppTelecomLinkRepository appTelecomLinkRepository;
	private final AppDictService appDictService;
	private final AppPermissionService appPermissionService;
	private final AppTelecomWhitelistRepository appTelecomWhitelistRepository;
	/***aapt路径***/
	@Value("${file.aapt.path}")
	String aaptPath;
	/***apk路径***/
	@Value("${file.apk.appSavePath}")
	String appSavePath;
	@Value("${file.apk.isPermission}")
	int isPermission;

	/***
	 * 对电信App下载信息进行插入保存
	 * @param appTelecomlink
	 */
	@Override
	public void saveAppTelecomLink(AppTelecomLink appTelecomlink) {
		if(appTelecomLinkRepository.findAppTelecomLinkByAppFileName(appTelecomlink.getAppFileName()).size()==0){
			appTelecomLinkRepository.save(appTelecomlink);
		}else{
			System.out.println("已存在");
		}

	}

	/****
	 * 对静态分析的电信App，将分析出来的结果更新到相应标曲
	 * @param appTelecomLink
	 */
	@Override
	public void updateAppTelecomLink(AppTelecomLink appTelecomLink) {
		appTelecomLinkRepository.updateAppTelecomLink(appTelecomLink);
	}

	@Override
	public List<AppTelecomLink> findAppLinkByAppName(String fileName) {
		return appTelecomLinkRepository.findAppTelecomLinkByAppFileName(fileName);
	}

	/****
	 * 获取没有静态分析的APP信息
	 * @param isAnalyse
	 * @param isDown
	 * @return
	 */
	@Override
	public List<AppTelecomLink> findAppLinkByConditions(int isAnalyse, int isDown) {
		return appTelecomLinkRepository.findAppTelecomLinkByAppIsAnalyseAndAppIsDown(isAnalyse,isDown);
	}

	/****
	 * 对APP静态分析
	 */
	@Override
	public void staticAnalyseApp() {
		List<AppTelecomLink> appTelecomLinks = this.findAppLinkByConditions(0,1);
		for(AppTelecomLink appTelecomLink : appTelecomLinks){

			System.out.println(appTelecomLink.getAppFileName());
			String appPath = appSavePath+appTelecomLink.getAppFileName();
			appTelecomLink.setAppIsAnalyse(1);
			try {
				ApkInfo apkInfo = new ApkUtil(aaptPath).getApkInfo(appPath);
				appTelecomLink.setAppApplicationName(apkInfo.getApplicationLable());
				appTelecomLink.setAppClassName(apkInfo.getLaunchableActivity());
				appTelecomLink.setAppPackageName(apkInfo.getPackageName());
				appTelecomLink.setAppVersion(apkInfo.getVersionName());
				appTelecomLink.setAppType(2);
				//判断APP是否在白名单里
				if(appTelecomWhitelistRepository.findByAppApplicationNameAndAndAppClassNameAndAndAppPackageName(
						appTelecomLink.getAppApplicationName(),
						appTelecomLink.getAppClassName(),
						appTelecomLink.getAppPackageName()).size()>0){
					appTelecomLink.setAppType(3);

				}
				//判断是否在黑名单里
				else if (appDictService.appDictFilter(1,appTelecomLink.getAppApplicationName()).size()>0){
					appTelecomLink.setAppType(1);
				}
				//是否开启权限操作
				if(isPermission==1) {
					//获取APP权限列表
					List<AppPermission> permissionList = new ArrayList<>();
					for (String permission : apkInfo.getUsesPermissions()) {
						AppPermission appPermission = new AppPermission();
						appPermission.setId(UUID.randomUUID().toString());
						appPermission.setAppLinkId(appTelecomLink.getId());
						appPermission.setAppPermissionName(permission);
						permissionList.add(appPermission);
					}
					//批量保存APP权限信息
					appPermissionService.saveBatchAppPermission(permissionList);
				}

			}catch (Exception ex){
				System.out.println(ex);
				appTelecomLink.setAppIsAnalyse(-1);
			}finally {
				appTelecomLinkRepository.updateAppLink(appTelecomLink);
			}

		}
	}
}
