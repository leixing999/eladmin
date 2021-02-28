package me.zhengjie.modules.app.task;

import me.zhengjie.modules.app.repository.AppDictRepository;
import me.zhengjie.modules.app.service.AppDictService;
import me.zhengjie.modules.app.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/***
 * 启动默认加载字典信息
 * @author xinglei
 * @date 2021-02-28
 */
@Component
@Order(1)
public class AppDictRunner implements ApplicationRunner {
    @Autowired
    AppDictService appDictService;
    @Autowired
    AppDictRepository appDictRepository;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        appDictService.initAppDict(appDictRepository.findAll());
    }
}
