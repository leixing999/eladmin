package me.zhengjie.modules.app.service;

import me.zhengjie.modules.app.domain.po.AppTelecomLinkPackage;
import me.zhengjie.modules.app.repository.AppTelecomLinkPackageRepository;
import me.zhengjie.modules.app.service.dto.LinkPackageQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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
     List<AppTelecomLinkPackage> findLinkPackageByFileName(String fileName);
     List<AppTelecomLinkPackage> findLinkPackageByStatus(Integer status);
     Object findAll(LinkPackageQueryCriteria linkPackageQueryCriteria,Pageable pageable);


}
