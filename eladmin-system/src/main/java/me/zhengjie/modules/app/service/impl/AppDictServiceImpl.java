package me.zhengjie.modules.app.service.impl;

import me.zhengjie.modules.app.domain.po.AppDict;
import me.zhengjie.modules.app.repository.AppDictRepository;
import me.zhengjie.modules.app.service.AppDictService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * AppDict 自定义Service实现层
 *
 * @author xinglei
 * @date 2021-02-28 13:09:58
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "appDict")
public class AppDictServiceImpl implements AppDictService {

	private static List<AppDict> appDictList = new ArrayList<>();
	private final AppDictRepository appDictRepository;

	@Override
	public void initAppDict(List<AppDict> list) {
		appDictList = list;
	}

	@Override
	public List<AppDict> getAppDicts() {
		return appDictList;
	}
}
