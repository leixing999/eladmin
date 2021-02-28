package me.zhengjie.modules.app.service.impl;

import me.zhengjie.modules.app.domain.po.AppTelecomLink;
import me.zhengjie.modules.app.repository.AppTelecomLinkRepository;
import me.zhengjie.modules.app.service.AppTelecomLinkService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

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
}
