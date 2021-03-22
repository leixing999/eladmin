package me.zhengjie.modules.app.service.impl;

import me.zhengjie.modules.app.repository.AppDynamicParseUrlDetailRepository;
import me.zhengjie.modules.app.service.AppDynamicParseUrlDetailService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;

/**
 * AppDynamicParseUrlDetail 自定义Service实现层
 *
 * @author xinglei
 * @date 2021-03-22 13:39:31
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "appDynamicParseUrlDetail")
public class AppDynamicParseUrlDetailServiceImpl implements AppDynamicParseUrlDetailService {

	private final AppDynamicParseUrlDetailRepository appDynamicParseUrlDetailRepository;
}
