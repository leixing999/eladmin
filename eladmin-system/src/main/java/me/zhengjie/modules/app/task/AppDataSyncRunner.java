package me.zhengjie.modules.app.task;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import me.zhengjie.modules.app.domain.po.AppDynamicParseUrl;
import me.zhengjie.modules.app.domain.po.AppTelecomLink;
import me.zhengjie.modules.app.domain.vo.AppCsvVO;
import me.zhengjie.modules.app.domain.vo.UrlPathVO;
import me.zhengjie.modules.app.repository.AppDictRepository;
import me.zhengjie.modules.app.repository.AppDynamicParseUrlRepository;
import me.zhengjie.modules.app.repository.AppTelecomLinkRepository;
import me.zhengjie.modules.app.service.AppDictService;
import me.zhengjie.utils.FTPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/***
 * 启动同步app处理数据
 * @author xinglei
 * @date 2021-02-28
 */
@Component
@Order(1)
public class AppDataSyncRunner implements ApplicationRunner {
    @Autowired
    AppTelecomLinkRepository appTelecomLinkRepository;
    @Autowired
    AppDynamicParseUrlRepository appDynamicParseUrlRepository;
    //虚拟路径
    @Value("${appDynamic.virtualPath}")
    String virtualPath;


    //FTP下载配置信息
    @Value("${file.apk.ftp.upload.ip}")
    String ip;
    @Value("${file.apk.ftp.upload.port}")
    int port;
    @Value("${file.apk.ftp.upload.user}")
    String user;
    @Value("${file.apk.ftp.upload.password}")
    String password;

    //远程路径
    @Value("${file.apk.ftp.upload.dir}")
    String dir;

    //是否同步
    @Value("${file.apk.isSync}")
    Integer isSync;

    //临时文件存放路径
    @Value("${file.apk.ftp.upload.tempDir}")
    String tempDir;
    //ftp文件下载保存路径
    @Value("${appDynamic.apkDynamicLogPath}")
    String apkDynamicLogPath;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(isSync==1) {
            while (true) {
                try {
                    uploadToClient();
                    Thread.sleep(1000000);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        }

    }

    /***
     * 上传到客户端
     */
    private void uploadToClient(){
        List<AppTelecomLink> list = appTelecomLinkRepository.findAppTelecomLinkByAppIsAnalyseAndAppIsDownAndAppIsSync(1,1,0);


        //判断是否存在可上传的文件
        if(list.size()>0) {
            String csvPath = tempDir + File.separator + UUID.randomUUID() + ".csv";
            // 通过工具类创建writer，默认创建xls格式
            CsvWriter writer = CsvUtil.getWriter(csvPath, CharsetUtil.CHARSET_GBK);
            try {
                for (AppTelecomLink appTelecomLink : list) {
                    //组合出下载app地址信息
                    String appDownloadUrl = virtualPath + appTelecomLink.getAppSysRelativePath() + File.separator + appTelecomLink.getAppSysFileName();
                   //组合写入到csv文件中的值
                    writer.write(new String[]{
                            appTelecomLink.getAppApplicationName(),
                            appTelecomLink.getAppPackageName(),
                            appTelecomLink.getAppClassName(),
                            appTelecomLink.getAppAddTime().toString(),
                            appDownloadUrl,
                            this.getAppDomains(appTelecomLink.getId())});
                }
                writer.flush();
                List fileList = new ArrayList();
                fileList.add(new File(csvPath));
                //上传FTP指定目录上
                new FTPUtil(ip, port, user, password).uploadFile(fileList, dir);

                //更新状态
                for (AppTelecomLink appTelecomLink : list) {
                    try {
                        appTelecomLinkRepository.updateAppTelecomLinkBySync(appTelecomLink.getId(), 1);
                    }catch (Exception ex){
                        System.out.println(ex);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 关闭writer，释放内存
                writer.close();
            }
        }
    }

    /****
     * 获取app的域名信息
     * @param appId
     * @return
     */
    private String getAppDomains(String appId){

        List<AppDynamicParseUrl> list = appDynamicParseUrlRepository.findByAppIdAndType(appId,1);
        List<AppDynamicParseUrl> noRepeatList =  list.stream().collect(
                collectingAndThen(
                        toCollection(() -> new TreeSet<>(Comparator.comparing(AppDynamicParseUrl::getUrl))), ArrayList::new));
        StringBuffer buffer = new StringBuffer();
        for(AppDynamicParseUrl appDynamicParseUrl : noRepeatList){
            buffer.append(appDynamicParseUrl.getUrl()).append("|");
        }
        return buffer.toString();
    }
}
