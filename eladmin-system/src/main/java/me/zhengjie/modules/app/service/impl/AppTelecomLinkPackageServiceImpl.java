package me.zhengjie.modules.app.service.impl;

import me.zhengjie.modules.app.domain.po.AppTelecomLinkPackage;
import me.zhengjie.modules.app.repository.AppTelecomLinkPackageRepository;
import me.zhengjie.modules.app.service.AppTelecomLinkPackageService;
import me.zhengjie.utils.RequestHolder;
import me.zhengjie.utils.StringUtils;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * AppTelecomLinkPackage 自定义Service实现层
 *
 * @author xinglei
 * @date 2021-02-24 18:20:01
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "appTelecomLinkPackage")
public class AppTelecomLinkPackageServiceImpl implements AppTelecomLinkPackageService {

	private final AppTelecomLinkPackageRepository appTelecomLinkPackageRepository;

	/***
	 * 增加对电信App文件包进行保存操作
	 * @param appTelecomLinkPackage
	 */
	@Override
	public void saveAppTelecomLinkPackage(AppTelecomLinkPackage appTelecomLinkPackage) {

		try {
			HttpServletRequest request = RequestHolder.getHttpServletRequest();
			String ip = StringUtils.getIp(request);
			appTelecomLinkPackage.setIp(ip);
		}catch(Exception ex){
			System.out.println(ex);
		}
		if(appTelecomLinkPackageRepository.findAppTelecomLinkPackageByLinkPackageName(appTelecomLinkPackage.getLinkPackageName()).size()==0){
			appTelecomLinkPackageRepository.save(appTelecomLinkPackage);
		}
	}

	/****
	 * 增加对电信解析服务队当前状态更新
	 * @param id
	 * @param currentLines
	 */
	@Override
	public void updateLinkPackage(String id, Integer currentLines) {
		appTelecomLinkPackageRepository.updateLinkPackage(id,currentLines);
	}

	/****
	 * 修改电信解析包当前行和开始时间
	 * @param id
	 * @param beginTime
	 * @param currentLines
	 */
	@Override
	public void updateLinkPackage(String id, Date beginTime, Integer currentLines) {
		appTelecomLinkPackageRepository.updateLinkPackage(id,beginTime,currentLines);
	}

	/***
	 *修改电信解析包状态、当前行和状态
	 * @param id
	 * @param endTime
	 * @param currentLines
	 * @param status
	 */
	@Override
	public void updateLinkPackage(String id, Date endTime, Integer currentLines, Integer status) {
		appTelecomLinkPackageRepository.updateLinkPackage(id,endTime,currentLines,status);
	}

	/***
	 * 通过文件名完全匹配是否文件已经入库
	 * @param fileName
	 * @return
	 */
	@Override
	public List<AppTelecomLinkPackage> findLinkPackageByFileName(String fileName) {
		return	appTelecomLinkPackageRepository.findAppTelecomLinkPackageByLinkPackageName(fileName);
	}

	@Override
	public List<AppTelecomLinkPackage> findLinkPackageByStatus(Integer status) {
		return appTelecomLinkPackageRepository.findAppTelecomLinkPackageByLinkPackageStatus(status);
	}
}
