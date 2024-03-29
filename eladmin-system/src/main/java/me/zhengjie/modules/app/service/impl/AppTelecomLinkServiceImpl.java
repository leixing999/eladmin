package me.zhengjie.modules.app.service.impl;

import me.zhengjie.domain.ApkInfo;
import me.zhengjie.modules.app.domain.po.*;
import me.zhengjie.modules.app.repository.AppTelecomLinkRelDictRepository;
import me.zhengjie.modules.app.repository.AppTelecomLinkRepository;
import me.zhengjie.modules.app.repository.AppTelecomWhitelistRepository;
import me.zhengjie.modules.app.service.AppDictService;
import me.zhengjie.modules.app.service.AppPermissionService;
import me.zhengjie.modules.app.service.AppTelecomLinkRelDictService;
import me.zhengjie.modules.app.service.AppTelecomLinkService;
import me.zhengjie.utils.ApkUtil;
import me.zhengjie.utils.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

	private final AppTelecomLinkRelDictService appTelecomLinkRelDictService;
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
	/***
	 * 通过对app进行动态分析对此App的参数信息进行跟新
	 * @param id
	 * @param isDynamic
	 */
	@Override
	public void updateAppTelecomLink(String id, Integer isDynamic) {
		appTelecomLinkRepository.updateAppTelecomLink(id,isDynamic);
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
	 * 通过isDynamic和isdown组合条件查找
	 * 已下载但是为东态分析的app
	 * @param isDynamic
	 * @param isDown
	 * @return
	 */
	@Override
	public List<AppTelecomLink> findAppLinkByDynamicConditions(int isDown, int isDynamic) {
		return appTelecomLinkRepository.findAppTelecomLinkByAppIsDownAndAppIsDynamic(isDown,isDynamic);
	}

	/****
	 * 获取动态条件
	 * @param isDown
	 * @param appType
	 * @param isDynamic
	 * @return
	 */
	@Override
	public List<AppTelecomLink> findAppLinkByDynamicConditions(int isDown, int appType, int isDynamic) {

		return appTelecomLinkRepository.findByAppIsDownAndAppTypeAndAppIsDynamic(isDown,appType,isDynamic);
	}

	/****
	 * 对APP静态分析
	 */
	@Override
	public void staticAnalyseApp() {
		List<AppTelecomLink> appTelecomLinks = this.findAppLinkByConditions(0,1);
		for(AppTelecomLink appTelecomLink : appTelecomLinks){

			System.out.println(appTelecomLink.getAppFileName());
//			String appPath = appSavePath+appTelecomLink.getAppFileName();
			String appPath = appSavePath+appTelecomLink.getAppSysRelativePath()+ File.separator+appTelecomLink.getAppSysFileName();
			appTelecomLink.setAppIsAnalyse(1);
			try {
				ApkInfo apkInfo = new ApkUtil(aaptPath).getApkInfo(appPath);
				appTelecomLink.setAppApplicationName(apkInfo.getApplicationLable());
				appTelecomLink.setAppClassName(apkInfo.getLaunchableActivity());
				appTelecomLink.setAppPackageName(apkInfo.getPackageName());
				appTelecomLink.setAppVersion("");
				appTelecomLink.setAppType(2);

				//跟涉及敏感词关联的关键词信息
				List<AppDict> keyWordList = appDictService.appDictFilter(1,appTelecomLink.getAppApplicationName());
				//判断APP是否在白名单里
				if(appTelecomWhitelistRepository.findByAppApplicationNameAndAppPackageName(
						appTelecomLink.getAppApplicationName(),
						appTelecomLink.getAppPackageName()).size()>0){
					appTelecomLink.setAppType(3);
					//删除白名单APP文件信息
					this.delApp(appPath);

				}
				//判断是否在黑名单里
				else if (keyWordList.size()>0){
					appTelecomLink.setAppType(1);
					//添加关联黑名单信息
					appTelecomLinkRelDictService.saveAppRelDict(appTelecomLink.getId(),keyWordList);

				}
				//判断app是否已经存在了
			  if(appTelecomLinkRepository.findByAppApplicationNameAndAppPackageName(
						appTelecomLink.getAppApplicationName(),
						appTelecomLink.getAppPackageName()).size()>0){
					appTelecomLink.setAppType(4);

				}
				//是否开启权限操作
				if(isPermission==1) {
					//批量保存APP权限信息
					appPermissionService.saveBatchAppPermission(appTelecomLink.getId(),apkInfo.getUsesPermissions());
				}

			}catch (Exception ex){
				System.out.println(ex);
				appTelecomLink.setAppIsAnalyse(-1);
			}finally {
				appTelecomLinkRepository.updateAppLink(appTelecomLink);
			}

		}
	}

	/****
	 * 按照APP路径删除指定APP文件
	 * @param appPath
	 */
	@Override
	public void delApp(String appPath) {

		try{
			FileUtil.del(appPath);
		}catch(Exception ex){
			System.out.println(ex);
		}
	}

	/***
	 * 修改APP类型
	 * @param appId
	 * @param appType
	 * @return
	 */
	@Override
	public boolean updateAppType(String appId, Integer appType) {
		boolean isSuccess = true;
		try{
			appTelecomLinkRepository.updateAppType(appId,appType);
			/***如果设置成白名单，则需要将App文件删除****/
			if(appType==3){
				AppTelecomLink appTelecomLink = this.getAppByAppId(appId);
				String appPath = appSavePath+appTelecomLink.getAppSysRelativePath()+ File.separator+appTelecomLink.getAppSysFileName();
				FileUtil.del(appPath);
				AppTelecomWhitelist appTelecomWhitelist = new AppTelecomWhitelist();
				appTelecomWhitelist.setStatus(1);
				appTelecomWhitelist.setAppFilename(appTelecomLink.getAppFileName());
				appTelecomWhitelist.setId(appTelecomLink.getId());
				appTelecomWhitelist.setAppClassName(appTelecomLink.getAppClassName());
				appTelecomWhitelist.setAppPackageName(appTelecomLink.getAppPackageName());
				appTelecomWhitelist.setAppApplicationName(appTelecomLink.getAppApplicationName());
				appTelecomWhitelistRepository.save(appTelecomWhitelist);
			}

		}catch(Exception ex){
			isSuccess = false;
		}
		return isSuccess;
	}

	@Override
	public AppTelecomLink getAppByAppId(String appId) {
		Optional<AppTelecomLink> appTelecomLink = appTelecomLinkRepository.findById(appId);
		return appTelecomLink.get();
	}
}
