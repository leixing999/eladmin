package me.zhengjie.modules.app.service.impl;

import me.zhengjie.modules.app.repository.AppTelecomLinkRepository;
import me.zhengjie.modules.app.service.AppTelecomLinkService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;

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
}
