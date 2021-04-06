package me.zhengjie.modules.app.service;

import me.zhengjie.modules.app.domain.po.AppDict;
import me.zhengjie.modules.app.repository.AppTelecomLinkRelDictRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * AppTelecomLinkRelDict service层
 *
 * @author xinglei
 * @date 2021-04-06 15:12:32
 */
public interface AppTelecomLinkRelDictService {

    void saveAppRelDict(String appId, List<AppDict> list);
}
