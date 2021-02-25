package me.zhengjie.modules.app.service;

import me.zhengjie.modules.app.domain.po.AppTelecomLink;
import me.zhengjie.modules.app.repository.AppTelecomLinkRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * AppTelecomLink service层
 * 主要实现电信提供APP解析入库以及静态分析功能
 * @author xinglei
 * @date 2021-02-24 18:20:06
 */
public interface AppTelecomLinkService {

    public void saveAppTelecomLink(AppTelecomLink appTelecomlink);
    void updateAppTelecomLink(AppTelecomLink appTelecomLink);
}
