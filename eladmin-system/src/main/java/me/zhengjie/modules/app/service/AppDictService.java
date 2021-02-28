package me.zhengjie.modules.app.service;

import me.zhengjie.modules.app.domain.po.AppDict;
import me.zhengjie.modules.app.repository.AppDictRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * AppDict service层
 *
 * @author xinglei
 * @date 2021-02-28 13:09:58
 */
public interface AppDictService {
    void initAppDict(List<AppDict> list);
    List<AppDict> getAppDicts();

}
