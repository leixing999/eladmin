package me.zhengjie.modules.app.task;

import me.zhengjie.modules.app.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    //是否开启静态分析(1开启，0：关闭）
    @Value("${file.apk.isStatic}")
    int isStatic;
    //是否开启解析URL(1开启，0：关闭）
    @Value("${file.apk.isParse}")
    int isParse;
    //是否开启白名单(1开启，0：关闭）
    @Value("${file.apk.isWhitelist}")
    int isWhitelist;

    //是否开启扫描文件夹(1开启，0：关闭）
    @Value("${file.apk.isSearch}")
    int isSearch;

    //是否启用FTP 下载(1开启，0：关闭）
    @Value("${file.apk.isFtpDownload}")
    int isFtpDownload;
    //是否启用动态解析 下载(1开启，0：关闭）
    @Value("${file.apk.isDynamicParse}")
    int isDynamicParse;


    //是否启用脱壳 下载(1开启，0：关闭）
    @Value("${file.apk.isDump}")
    int isDump;


    @Autowired
    AppService appService;

    @Autowired
    AppDataSyncRunner appDataSyncRunner;
    @Override
    public void run(ApplicationArguments args) throws Exception {

        //启动静态分析线程
        new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        //是否开启静态分析(1开启，0：关闭）
                        if(isStatic==1){
                            appService.staticAnalyseApp();
                        }

                        Thread.sleep(100000);
                    }catch (Exception ex){
                        System.out.println(ex);
                    }

                }
            }
        }.start();


        //启动FTP下载进程
        new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        //启动FTP下载进程(1开启，0：关闭）
                        if(isFtpDownload==1){
                            appService.ftpDownloadAppUrlFiles();
                        }

                        Thread.sleep(100000);
                    }catch (Exception ex){
                        System.out.println(ex);
                    }

                }
            }
        }.start();


        //启动动态解析进程
        new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        //启动动态解析进程(1开启，0：关闭）
                        if(isDynamicParse==1){
                            appService.dynamicAnalyseApp();
                        }

                        Thread.sleep(100000);
                    }catch (Exception ex){
                        System.out.println(ex);
                    }

                }
            }
        }.start();


        //启动白名单进程
        new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        //是否开启白名单(1开启，0：关闭）
                        if(isWhitelist==1){
                            appService.runAppWhiteList();
                        }

                        Thread.sleep(100000);
                    }catch (Exception ex){
                        System.out.println(ex);
                    }

                }
            }
        }.start();


        //启动脱壳进程
        new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        //是否开启脱壳(1开启，0：关闭）
                        if(isDump==1){
                            appService.dumpAnalyseApp();
                        }

                        Thread.sleep(100000);
                    }catch (Exception ex){
                        System.out.println(ex);
                    }

                }
            }
        }.start();


        //启动同步数据进程
        new Thread(){
            @Override
            public void run() {
                try {
                    appDataSyncRunner.run();
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        }.start();





        //启动文件分析处理方式
        while(true){
            try {
                //是否开启扫描文件夹(1开启，0：关闭）
                if(isSearch==1){
                    appService.saveAppUrlFiles();
                }

                //是否开启解析URL(1开启，0：关闭）
                if(isParse==1){
                    appService.parseAppUrlFiles();

                }
//                //是否开启白名单(1开启，0：关闭）
//                if(isWhitelist==1){
//                    appService.runAppWhiteList();
//                }
//

                //appService.staticAnalyseApp();

               //

                Thread.sleep(10000);
            }catch (Exception ex){
                System.out.println(ex);
            }
        }

    }
}
