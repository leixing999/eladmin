package me.zhengjie.modules.app.service.impl;

import me.zhengjie.modules.app.repository.AppDynamicParseWordRepository;
import me.zhengjie.modules.app.service.AppDynamicParseWordService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;

/**
 * AppDynamicParseWord 自定义Service实现层
 *
 * @author xinglei
 * @date 2021-03-22 13:39:22
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "appDynamicParseWord")
public class AppDynamicParseWordServiceImpl implements AppDynamicParseWordService {

	private final AppDynamicParseWordRepository appDynamicParseWordRepository;
}
