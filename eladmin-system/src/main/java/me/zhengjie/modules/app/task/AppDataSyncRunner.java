package me.zhengjie.modules.app.task;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.util.CharsetUtil;
import me.zhengjie.modules.app.domain.po.AppDynamicParseUrl;
import me.zhengjie.modules.app.domain.po.AppTelecomLink;
import me.zhengjie.modules.app.domain.vo.UrlPathVO;
import me.zhengjie.modules.app.repository.AppDynamicParseUrlRepository;
import me.zhengjie.modules.app.repository.AppTelecomLinkRepository;
import me.zhengjie.modules.app.service.AppDictService;
import me.zhengjie.modules.test.service.ITestService;
import me.zhengjie.utils.DateUtil;
import me.zhengjie.utils.FTPUtil;
import me.zhengjie.utils.SFTPUtil;
import me.zhengjie.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/***
 * 启动同步app处理数据
 * @author xinglei
 * @date 2021-02-28
 */
@Service
//@Order(1)
public class AppDataSyncRunner {
    @Autowired
    AppDictService appDictService;
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

    //app 下载保存的路径
    @Value("${file.apk.appSavePath}")
    String  appSavePath;

    //app 导出文件大小
    @Value("${file.apk.exportFileSize}")
    long  exportFileSize;
    //app 导出文件分页大小
    @Value("${file.apk.exportPageSize}")
    int  exportPageSize;


    //是否开启SFTP同步结果(1开启，0：关闭）
    @Value("${file.apk.isSftpSync}")
    int  isSftpSync;

    public void run() throws Exception {
        if(isSync==1) {
            while (true) {
                try {
                    //copyQuesttionApp();
                    uploadToClient();
                    //12小时同步一次
                    Thread.sleep(1000*60*60*12);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        }

    }

    /****
     * 将问题app处理
     */
    public void copyQuesttionApp(){
        List<AppTelecomLink> list = appTelecomLinkRepository.findByAppIsDownAndAppTypeAndAppIsAnalyseOrderByAppAddTimeDesc(1,1,1);

        for(AppTelecomLink appTelecomLink : list){
           String   appDownloadUrl = appSavePath + appTelecomLink.getAppSysRelativePath() + File.separator + appTelecomLink.getAppSysFileName();
          // String   appSaveTarget = appSavePath +File.separator +"question/"+ appTelecomLink.getAppSysFileName();
            String   appSaveTarget = appSavePath +File.separator +"question/"+ appTelecomLink.getAppApplicationName()+".apk";
            int index = 0;
           try {
               if(index<exportPageSize) {
                   if(appTelecomLink.getAppFileSize()<exportFileSize) {
                       IoUtil.copy(new FileInputStream(new File(appDownloadUrl)), new FileOutputStream(new File(appSaveTarget)));
                       index++;
                   }
               }else{
                   break;
               }
           }catch (Exception ex){
               System.out.println(ex);
           }
        }
    }

    /****
     * 将问题app处理
     */
    public void copyHsQuesttionApp(){
        List<AppTelecomLink> list = appTelecomLinkRepository.findByAppIsDownAndAppTypeAndAppIsAnalyseOrderByAppAddTimeDesc(1,2,1);

        int index = 0;
        for(AppTelecomLink appTelecomLink : list){
            String   appDownloadUrl = appSavePath + appTelecomLink.getAppSysRelativePath() + File.separator + appTelecomLink.getAppSysFileName();
/*
            String   appSaveTarget = appSavePath +File.separator +"hsquestion/"+ appTelecomLink.getAppSysFileName();
*/
            String   appSaveTarget = appSavePath +File.separator +"hsquestion/"+ appTelecomLink.getAppApplicationName()+".apk";

            try {
                if(index<exportPageSize) {
                    if(appTelecomLink.getAppFileSize()<exportFileSize) {
                        IoUtil.copy(new FileInputStream(new File(appDownloadUrl)), new FileOutputStream(new File(appSaveTarget)));
                        index++;
                    }
                }else{
                    break;
                }

            }catch (Exception ex){
                System.out.println(ex);
            }
        }
    }
    /***
     * 上传到客户端
     */
    private void uploadToClient(){
        /********开始同步黑名单信息****************/
        List<AppTelecomLink> list = appTelecomLinkRepository.findByAppIsDownAndAppTypeAndAppIsDynamicAndAppIsSync(1,1,-1,0);
        //去重
        list =  list.stream().filter(s->s.getAppApplicationName()!=null).collect(Collectors.toList());

        List<AppTelecomLink>  nopeatList= list.stream().collect(
                collectingAndThen(
                        toCollection(() -> new TreeSet<>(Comparator.comparing(AppTelecomLink::getAppApplicationName))), ArrayList::new)
        );
        //同步黑名单信息
        createSyncData(list,nopeatList,1);
        /********结束同步黑名单信息****************/

        /********开始同步灰名单信息****************/
        //获取机器不能处理APP信息

        list = appTelecomLinkRepository.findByAppIsDownAndAppTypeAndAppIsDynamicAndAppIsSync(1,2,0,0);
        //去重
        list =  list.stream().filter(s->s.getAppApplicationName()!=null).collect(Collectors.toList());
        nopeatList= list.stream().collect(
                collectingAndThen(
                        toCollection(() -> new TreeSet<>(Comparator.comparing(AppTelecomLink::getAppApplicationName))), ArrayList::new)
        );
        createSyncData(list,nopeatList,2);
        /********结束同步灰名单信息****************/
    }


    /***
     * @param list 全部信息
     * @param nopeatList
     * @param syncDataType 1:黑名单，2：不能处理
     */
    private void createSyncData( List<AppTelecomLink> list ,List<AppTelecomLink> nopeatList,int syncDataType){
        //判断是否存在可上传的文件
        if(nopeatList.size()>0) {
            String fileName = UUID.randomUUID()+"_"+syncDataType + ".csv";
            String csvPath = tempDir + File.separator + fileName;
            // 通过工具类创建writer，默认创建xls格式
            CsvWriter writer = CsvUtil.getWriter(csvPath, CharsetUtil.CHARSET_GBK);
            try {
                for (AppTelecomLink appTelecomLink : nopeatList) {
                    //组合出下载app地址信息
                    String appDownloadUrl = appSavePath + appTelecomLink.getAppSysRelativePath() + File.separator + appTelecomLink.getAppSysFileName();
                    String domainUrl =   this.getAppDomains(appTelecomLink.getId());
                    //组合写入到csv文件中的值
                    if(syncDataType==2){
                        writer.write(new String[]{
                                appTelecomLink.getId(),
                                appTelecomLink.getAppApplicationName(),
                                appTelecomLink.getAppPackageName(),
                                DateUtil.getDefaultDateStr("yyyy-MM-dd HH:mm:ss", appTelecomLink.getAppAddTime()),
                                "",

                                appDownloadUrl
                        });
                    }else {
                        if (domainUrl != null && domainUrl.length() > 0) {
                            writer.write(new String[]{
                                    appTelecomLink.getId(),
                                    appTelecomLink.getAppApplicationName(),
                                    appTelecomLink.getAppPackageName(),
                                    DateUtil.getDefaultDateStr("yyyy-MM-dd HH:mm:ss", appTelecomLink.getAppAddTime()),
                                    domainUrl,

                                    appDownloadUrl
                            });
                        }
                    }
                }
                writer.flush();
                writer.close();

                /***beginSFTP上传文件**/

                if(isSftpSync==1) {
                    sftpSync(csvPath, fileName, "/", dir);
                }

                /***endSFTP上传文件**/

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
            }
        }
    }


    /****
     * sftp数据同步
     * @param fileNamePath
     * @param fileName
     * @param basePath
     * @param dir
     */
    private void sftpSync(String fileNamePath,String fileName,String basePath,String dir){
        try {
            SFTPUtil sftp = new SFTPUtil(user, password, ip, port);
            sftp.login();
            File file = new File(fileNamePath);
            InputStream is = new FileInputStream(file);

            sftp.upload(basePath, dir, fileName, is);
            sftp.logout();
        }catch(Exception ex){
            System.out.println(ex);
        }
    }
    /****
     * 获取app的域名信息
     * @param appId
     * @return
     */
    private String getAppDomains(String appId){

        //对从数据库查询出来的域名信息进程处理
        List<AppDynamicParseUrl> list = parseDomainUrl(appDynamicParseUrlRepository.findByAppIdAndType(appId,1));
        List<AppDynamicParseUrl> noRepeatList =  list.stream().collect(
                collectingAndThen(
                        toCollection(() -> new TreeSet<>(Comparator.comparing(AppDynamicParseUrl::getUrl))), ArrayList::new));
        StringBuffer buffer = new StringBuffer();
        for(AppDynamicParseUrl appDynamicParseUrl : noRepeatList){
            buffer.append(appDynamicParseUrl.getUrl()).append("|");
        }
        return buffer.toString();
    }

    /****
     * 对域名结果进行处理
     * @param list
     * @return
     */
    private List<AppDynamicParseUrl>parseDomainUrl( List<AppDynamicParseUrl> list ){
        List<AppDynamicParseUrl> deleayList = new ArrayList<>();
        for(AppDynamicParseUrl parseUrl : list){
            String url = parseUrl.getUrl();
            if(url.lastIndexOf(":")>0){
                url = url.substring(0,url.lastIndexOf(":"));

            }
            if(StringUtils.isIP(url)){
                parseUrl.setUrl(url);
            }else{
                String [] urlSplits  =url.split("\\.");
                int urlLens = urlSplits.length;
                try{
                    if(urlLens>=4) {
                        url = "www." + urlSplits[urlLens - 3]  + urlSplits[urlLens - 2]+ "." + urlSplits[urlLens - 1];
                    } if(urlLens==3){
                        url = "www." + urlSplits[urlLens - 2] + "." + urlSplits[urlLens - 1];
                    }
                }catch(Exception ex){
                    System.out.println(ex);
                }
                parseUrl.setUrl(url);
            }

            //判断url是否存在白名单里
            if(appDictService.appDictFilter(2,parseUrl.getUrl()).size()==0){
                deleayList.add(parseUrl);
            }

        }
        return deleayList;

    }
}
