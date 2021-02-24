package me.zhengjie.modules.app.service.impl;

import me.zhengjie.modules.app.repository.AppTelecomLinkPackageRepository;
import me.zhengjie.modules.app.service.AppTelecomLinkPackageService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;

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
}
