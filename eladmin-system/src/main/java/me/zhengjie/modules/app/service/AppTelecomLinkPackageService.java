package me.zhengjie.modules.app.service;

import me.zhengjie.modules.app.domain.po.AppTelecomLinkPackage;
import me.zhengjie.modules.app.repository.AppTelecomLinkPackageRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * AppTelecomLinkPackage service层
 *
 * @author xinglei
 * @date 2021-02-24 18:20:01
 */
public interface AppTelecomLinkPackageService {

     void saveAppTelecomLinkPackage(AppTelecomLinkPackage appTelecomLinkPackage);

     void updateLinkPackage(String id,Integer currentLines);

     void updateLinkPackage(String id, Date beginTime, Integer currentLines);

     void updateLinkPackage(String id, Date EndTime, Integer currentLines,Integer status);


}