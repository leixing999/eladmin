package me.zhengjie.modules.app.service.impl;

import me.zhengjie.modules.app.domain.po.AppDict;
import me.zhengjie.modules.app.domain.po.AppTelecomLinkRelDict;
import me.zhengjie.modules.app.repository.AppTelecomLinkRelDictRepository;
import me.zhengjie.modules.app.service.AppTelecomLinkRelDictService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;
import java.util.UUID;

/**
 * AppTelecomLinkRelDict 自定义Service实现层
 *
 * @author xinglei
 * @date 2021-04-06 15:12:32
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "appTelecomLinkRelDict")
public class AppTelecomLinkRelDictServiceImpl implements AppTelecomLinkRelDictService {

	private final AppTelecomLinkRelDictRepository appTelecomLinkRelDictRepository;

	@Override
	public void saveAppRelDict(String appId, List<AppDict> list) {

		for(AppDict appDict : list){
			AppTelecomLinkRelDict appTelecomLinkRelDict = new AppTelecomLinkRelDict();
			appTelecomLinkRelDict.setAppId(appId);
			appTelecomLinkRelDict.setDictId(appDict.getId());
			appTelecomLinkRelDict.setId(UUID.randomUUID().toString());
			appTelecomLinkRelDictRepository.save(appTelecomLinkRelDict);
		}
	}
}
