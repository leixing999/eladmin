package me.zhengjie.modules.app.task;

import me.zhengjie.modules.app.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/***
 * 启动默认扫描电信apk文件扫描和解析服务
 * @author xinglei
 * @date 2021-02-21
 */
@Component
@Order(1)
public class AppRunner implements ApplicationRunner {

    @Autowired
    AppService appService;
    @Override
    public void run(ApplicationArguments args) throws Exception {

        //启动静态分析线程
        new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        appService.staticAnalyseApp();
                        Thread.sleep(100000);
                    }catch (Exception ex){
                        System.out.println(ex);
                    }

                }
            }
        }.start();

        //启动文件分析处理方式
        while(true){
            try {
                appService.saveAppUrlFiles();
                appService.parseAppUrlFiles();
                //appService.runAppWhiteList();
                //appService.staticAnalyseApp();

                Thread.sleep(10000);
            }catch (Exception ex){
                System.out.println(ex);
            }
        }

    }
}
