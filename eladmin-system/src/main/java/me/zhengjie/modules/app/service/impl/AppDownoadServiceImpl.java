package me.zhengjie.modules.app.service.impl;

import me.zhengjie.modules.app.domain.po.AppTelecomLinkPackage;
import me.zhengjie.modules.app.domain.vo.UrlPathVO;
import me.zhengjie.modules.app.service.AppDownloadService;
import me.zhengjie.modules.app.service.AppTelecomLinkPackageService;
import me.zhengjie.modules.app.thread.ThreadDownloadCallable;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class AppDownoadServiceImpl implements AppDownloadService {
    @Autowired
    AppTelecomLinkPackageService appTelecomLinkPackageService;
    @Autowired
    UrlPathService urlPathService;
   // 存放电信推送电信诈骗url文件地址
    @Value("file.apk.urlPath")
    String urlPath;
    /*****
     * 扫描电信传递的可以诈骗APK下载地址文件
     * @return
     */
    @Override
    public File[] searchAppUrlFile() {
        File files[] = FileUtil.searchApkUrl(urlPath);
        return files;
    }

    /***
     * 保存指定的电信传递的可疑诈骗APK文件
     * @param file
     */
    @Override
    public void saveAppUrlFile(File file) {
        long fileRecords = FileUtil.getFileRecords(file.getPath()).size();
        String fileName = file.getName();
        //判断文件是否已经存在
        if(appTelecomLinkPackageService.findLinkPackageByFileName(fileName).size()==0){
            AppTelecomLinkPackage linkPackage = new AppTelecomLinkPackage();
            linkPackage.setLinkPackageLines(fileRecords);
            linkPackage.setLinkPackagePath(file.getPath());
            linkPackage.setLinkPackageName(file.getName());
            linkPackage.setLinkPackageSize(file.length());
            linkPackage.setLinkPackageAddTime(new Date());
            linkPackage.setLinkPackageStatus(0);
            linkPackage.setId(HashUtil.uuid(file.getName()));
            //将电信传递的可疑诈骗文件信息入库
            appTelecomLinkPackageService.saveAppTelecomLinkPackage(linkPackage);

        }
    }

    /***
     * 实现分页查询 下载链接队列
     * @param urlPathVOList
     * @param maxThread
     * @param currentIndex
     * @return
     */
    private List<UrlPathVO>pagesUrlPath(List<UrlPathVO> urlPathVOList,int maxThread,int currentIndex){
        List<UrlPathVO> tempUrlPathVOList = null;
        int startIndex = currentIndex * maxThread;
        int endIndex = (currentIndex + 1) * maxThread;
        //获取本次队列要下载的APK路径队列
        if (endIndex < urlPathVOList.size()) {
            tempUrlPathVOList = urlPathVOList.subList(startIndex, endIndex);
        } else {
            tempUrlPathVOList = urlPathVOList.subList(startIndex, urlPathVOList.size()-1);

        }
        return tempUrlPathVOList;
    }

    /***
     * 去掉重复的APP下载链接
     * @param urlPathVOList
     * @return
     */
    private List<UrlPathVO>delayRepeatUrlPath(List<UrlPathVO> urlPathVOList){
        List<UrlPathVO> uniqueUrlPath = urlPathVOList.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(UrlPathVO::getApkFileName))), ArrayList::new)
        );
        return uniqueUrlPath;
    }
    /***
     * 保存电信传递的可疑诈骗APK文件地址信息
     */
    @Override
    public void saveAppUrlFiles() {
        File files[] = this.searchAppUrlFile();
        for(File file : files){
            this.saveAppUrlFile(file);
        }

    }
    /****
     * 解析电信传递App Url 文件
     * @param linkPackage
     */
    @Override
    public void parseAppUrlFiles(AppTelecomLinkPackage linkPackage) {
        //对电信APK文件下载进行归类为对应的URL 路径列表
        List<UrlPathVO> urlPathVOList = urlPathService.parseApkUrlPath(linkPackage.getLinkPackagePath());
        //配置每次下载启动10个线程，每个线程下载对应的一个文件
        ExecutorService executor = Executors.newFixedThreadPool(10);
        //apk现在线程队列
        List<ThreadDownloadCallable> myThreadList = null;
        //获取上次下载的电信APK包文件位置
        int nextIndex = Integer.parseInt(""+linkPackage.getLinkPackageParseLine());
        //每次线程批量下载的开始索引
        int startIndex = 0;
        //每次线程批量下载的结束索引
        int endIndex = 0;
        //获取一批次下载的URL路径信息
        List<UrlPathVO> pageList = null;
        //判断是否完成批量下载
        boolean isEnd = true;
        int maxThread = 10;
        while (isEnd) {
            //按照每个处理队列最大线程数进行分页
            pageList = this.pagesUrlPath(urlPathVOList,maxThread,nextIndex);
            //将分页的数据进行去重处理
            pageList = this.delayRepeatUrlPath(pageList);

        }

    }
    /***
     * 解析电信传递可疑电信诈骗 App URL 文件
     */
    @Override
    public void parseAppUrlFiles() {

        List<AppTelecomLinkPackage> list = appTelecomLinkPackageService.findLinkPackageByStatus(0);
        for(AppTelecomLinkPackage linkPackage : list){
            this.parseAppUrlFiles(linkPackage);
        }
    }
}
