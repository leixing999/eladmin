package me.zhengjie.modules.app.service;

import me.zhengjie.modules.app.domain.po.AppTelecomLink;
import me.zhengjie.modules.app.repository.AppTelecomLinkRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * AppTelecomLink service层
 * 主要实现电信提供APP解析入库以及静态分析功能
 * @author xinglei
 * @date 2021-02-24 18:20:06
 */
public interface AppTelecomLinkService {

    /****
     * 增加电信诈骗APP信息
     * @param appTelecomlink
     */
    void saveAppTelecomLink(AppTelecomLink appTelecomlink);

    /****
     * 更新电信诈骗APP信息
     * @param appTelecomLink
     */
    void updateAppTelecomLink(AppTelecomLink appTelecomLink);

    /***
     * 按照名字查找电信APP信息
     * @param fileName
     * @return
     */
    List<AppTelecomLink> findAppLinkByAppName(String fileName);

    /****
     * 通过isAnalyse和isdown组合条件查找
     * 已下载但是为静态分析的app
     * @param isAnalyse
     * @param isDown
     * @return
     */
    List<AppTelecomLink> findAppLinkByConditions(int isAnalyse,int isDown);

    /****
     * 静态分析APP
     */
    void staticAnalyseApp();
}
