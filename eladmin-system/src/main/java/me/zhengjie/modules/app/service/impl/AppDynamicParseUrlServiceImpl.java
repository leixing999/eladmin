package me.zhengjie.modules.app.service.impl;

import me.zhengjie.modules.app.repository.AppDynamicParseUrlRepository;
import me.zhengjie.modules.app.service.AppDynamicParseUrlService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;

/**
 * AppDynamicParseUrl 自定义Service实现层
 *
 * @author xinglei
 * @date 2021-03-22 13:39:39
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "appDynamicParseUrl")
public class AppDynamicParseUrlServiceImpl implements AppDynamicParseUrlService {

	private final AppDynamicParseUrlRepository appDynamicParseUrlRepository;
}
