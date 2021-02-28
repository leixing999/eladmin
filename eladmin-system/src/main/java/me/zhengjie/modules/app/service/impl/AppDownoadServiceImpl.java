package me.zhengjie.modules.app.service.impl;

import me.zhengjie.modules.app.domain.po.AppTelecomLink;
import me.zhengjie.modules.app.domain.po.AppTelecomLinkPackage;
import me.zhengjie.modules.app.domain.vo.UrlPathVO;
import me.zhengjie.modules.app.service.AppDownloadService;
import me.zhengjie.modules.app.service.AppTelecomLinkPackageService;
import me.zhengjie.modules.app.service.AppTelecomLinkService;
import me.zhengjie.modules.app.thread.ThreadDownloadCallable;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class AppDownoadServiceImpl implements AppDownloadService {
    @Autowired
    AppTelecomLinkPackageService appTelecomLinkPackageService;
    @Autowired
    AppTelecomLinkService appTelecomLinkService;
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
     * 保存电信传递的可疑诈骗APK文件地址信息
     */
    @Override
    public void saveAppUrlFiles() {
        File files[] = this.searchAppUrlFile();
        for(File file : files){
            this.saveAppUrlFile(file);
        }

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
     * @param urlPathVOList url解析队列
     * @param maxThread 最大分页数
     * @param currentIndex 当前分页索引
     * @param isEnd 是否下载结束标识
     * @return
     */
    private List<UrlPathVO>pagesUrlPath(List<UrlPathVO> urlPathVOList,int maxThread,int currentIndex,Boolean isEnd){
        List<UrlPathVO> tempUrlPathVOList = null;
        int startIndex = currentIndex * maxThread;
        int endIndex = (currentIndex + 1) * maxThread;
        //获取本次队列要下载的APK路径队列
        if (endIndex < urlPathVOList.size()) {
            tempUrlPathVOList = urlPathVOList.subList(startIndex, endIndex);
        } else {
            tempUrlPathVOList = urlPathVOList.subList(startIndex, urlPathVOList.size()-1);
            isEnd = false;

        }
        return tempUrlPathVOList;
    }

    /***
     * 去掉重复的APP下载链接
     * @param urlPathVOList url解析队列
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
     * 去掉已经下载过的APP
     * @param urlPathVOList
     * @return
     */
    private List<UrlPathVO>delayFinishedDownloadUrlPath(List<UrlPathVO> urlPathVOList){

        List<UrlPathVO> noFinishedUrlPathList = new ArrayList<>();
        for(UrlPathVO urlPathVO : urlPathVOList){
            if(appTelecomLinkService.findAppLinkByAppName(urlPathVO.getApkFileName()).size()==0){
                noFinishedUrlPathList.add(urlPathVO);
            }
        }
        return noFinishedUrlPathList;
    }

    /***
     * 生成线程下载callable队列
     * @param urlPathVOList
     * @param appSavePath
     * @param fileId
     * @param maxFileSize
     * @return
     */
    private List<ThreadDownloadCallable>createThreadDownloadCallable(List<UrlPathVO> urlPathVOList,String appSavePath,String fileId,long maxFileSize){
      List<ThreadDownloadCallable>  threadCallableList = new ArrayList<>();
      for(UrlPathVO urlPathVO : urlPathVOList){
          ThreadDownloadCallable threadCallable = new ThreadDownloadCallable(urlPathVO,appSavePath,fileId,maxFileSize);
          threadCallableList.add(threadCallable);
      }
      return threadCallableList;
    }

    /****
     * 执行多线程下载队列
     * @param threadCallableList
     * @param executor
     * @return
     */
    private List<Future<AppTelecomLink>> executeDownloadThreads(List<ThreadDownloadCallable> threadCallableList,ExecutorService executor){
        List<Future<AppTelecomLink>> futures = null;
        try {
            //获取本批次线程执行队列结果信息
            futures = executor.invokeAll(threadCallableList);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return futures;
    }

    /****
     * 处理线程下载结果信息
     * @param futureList
     */
    private void delayAppThreadDownloadResult(List<Future<AppTelecomLink>> futureList){
        for(Future<AppTelecomLink> appTelecomLinkFuture : futureList){
            try {
                AppTelecomLink appTelecomLink = appTelecomLinkFuture.get();
                appTelecomLinkService.saveAppTelecomLink(appTelecomLink);
            }catch(Exception ex){
                System.out.println(ex);
            }
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
        //获取上次下载的电信APK包文件位置
        int nextIndex = Integer.parseInt(""+linkPackage.getLinkPackageParseLine());
        //获取一批次下载的URL路径信息
        List<UrlPathVO> pageList = null;
        //判断是否完成批量下载
        Boolean isEnd = true;
        String appSavePath ="";
        long maxFileSize = 10l;
        int maxThread = 10;
        //如果索引为0，则更新开始时间和索引位置
        if(nextIndex==0){
            appTelecomLinkPackageService.updateLinkPackage(linkPackage.getId(),new Date(),nextIndex);
        }
        while (isEnd) {
            //按照每个处理队列最大线程数进行分页（1）
            pageList = this.pagesUrlPath(urlPathVOList,maxThread,nextIndex,isEnd);
            //将分页的数据进行去重处理（2）
            pageList = this.delayRepeatUrlPath(pageList);
            //去掉已经下载过得APP(3)
            pageList = this.delayFinishedDownloadUrlPath(pageList);
            //获取多线程下载队列（4)
            List<ThreadDownloadCallable> threadCallableList = this.createThreadDownloadCallable(pageList,appSavePath, linkPackage.getId(), maxFileSize);
            //执行多线程下载队列(5)
            List<Future<AppTelecomLink>> appLinkFutureList = this.executeDownloadThreads(threadCallableList,executor);
            //处理线程处理结果信息（6）
            this.delayAppThreadDownloadResult(appLinkFutureList);
            //完成本批次下载，同时更新下载索引状态（7）
            appTelecomLinkPackageService.updateLinkPackage(linkPackage.getId(),nextIndex);
            //获取下批次线程索引值
            nextIndex++;


        }
        //更新执行完毕操作【完成时间以及完成状态】（8）
        appTelecomLinkPackageService.updateLinkPackage(linkPackage.getId(),new Date(),nextIndex,2);
        executor.shutdown();

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
