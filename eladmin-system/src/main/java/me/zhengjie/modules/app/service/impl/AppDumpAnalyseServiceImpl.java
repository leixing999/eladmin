package me.zhengjie.modules.app.service.impl;

import me.zhengjie.modules.app.domain.po.AppDynamicResult;
import me.zhengjie.modules.app.domain.po.AppTelecomLink;
import me.zhengjie.modules.app.domain.vo.AppDumpVO;
import me.zhengjie.modules.app.repository.AppTelecomLinkRepository;
import me.zhengjie.modules.app.service.AppDumpAnalyseService;
import me.zhengjie.modules.app.service.AppDynamicAnalyseService;
import me.zhengjie.modules.app.service.AppDynamicParseUrlService;
import me.zhengjie.modules.app.service.AppDynamicService;
import me.zhengjie.utils.FileSearcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppDumpAnalyseServiceImpl implements AppDumpAnalyseService {

    //app 下载保存的路径
    @Value("${file.apk.appSavePath}")
    String  appSavePath;

    //app apk脱壳执行命令
    @Value("${appDumpAnalyse.runCommand}")
    String  runCommand;

    //apk脱壳文件存放路径
    @Value("${appDumpAnalyse.appDumpPath}")
    String  appDumpPath;



    @Autowired
    AppDynamicAnalyseService appDynamicAnalyseService;
    @Autowired
    AppTelecomLinkRepository appTelecomLinkRepository;

    @Autowired
    AppDynamicParseUrlService appDynamicParseUrlService;

    /***
     * 通过adx反编译工具进行脱壳处理
     * @param apkPath apk原始目录
     * @param dumpDespPath apk脱壳后目录
     * @param runCommand 脱壳运行命令
     */
    @Override
    public AppDumpVO apkDump(String apkPath, String dumpDespPath, String runCommand) {
        AppDumpVO appDumpVO = new AppDumpVO();
        try {
            appDumpVO.setDump(true);

            File file = new File(apkPath);
            String cmdStr =  "cmd /c E: && cd E:\\software\\jadx\\jadx1\\bin && jadx -d ";
            String cmd = "";
            if("".equals(runCommand)){
                cmd = cmdStr + dumpDespPath + File.separator + file.getName().substring(0, file.getName().length() - 4) + " " +
                        file.getAbsolutePath();
            }else{
                cmd = runCommand + dumpDespPath + File.separator + file.getName().substring(0, file.getName().length() - 4) + " " +
                        file.getAbsolutePath();
            }
            System.out.println("cmd【"+cmd+"】");
            //解包APK
            Process process = Runtime.getRuntime().exec(cmd);
            final InputStream in = process.getInputStream();
            int ch;
            while ((ch = in.read()) != -1) {
                System.out.print((char) ch);
            }
            appDumpVO.setDespFilePath(dumpDespPath + File.separator + file.getName().substring(0, file.getName().length() - 4));
            //关闭进程
            process.destroy();
            return appDumpVO;

        } catch (Exception e) {
            appDumpVO.setDump(false);

            e.printStackTrace();
            return appDumpVO;
        }
    }

    /****
     * apk脱壳分析处理流程
     * @return
     */
    @Override
    public boolean apkDumpAnalyse() {

        try{

            List<AppTelecomLink> list = appTelecomLinkRepository.findByAppIsDownAndAppTypeAndAppIsDump(1,1,0);
            for(AppTelecomLink appTelecomLink : list){
                String fileName = appTelecomLink.getAppFileName();
                String sysFilePath = appSavePath+appTelecomLink.getAppSysRelativePath()+File.separator+appTelecomLink.getAppSysFileName();
                AppDumpVO appDumpVO = this.apkDump(sysFilePath,appDumpPath,runCommand.replace("'",""));
                if(appDumpVO.isDump()){

                    //脱壳分析json数据
                    List<String> dumpDesFileList = this.getApkDumpFileList(appDumpVO.getDespFilePath(),"*.json");
                    for(String fileStr : dumpDesFileList){
                        StringBuffer buffer = appDynamicAnalyseService.getAppDynamicParseLog(fileStr,"utf-8");
                        AppDynamicResult appDynamicResult =  appDynamicAnalyseService.analysisJson(buffer);
                        appDynamicResult.setAppId(appTelecomLink.getId());
                        appDynamicParseUrlService.saveAppDynamicAnylasisResult(appDynamicResult,1);

                    }

                    //脱壳分析xml数据
                    List<String> dumpXmlDesFileList = this.getApkDumpFileList(appDumpVO.getDespFilePath(),"*.xml");

                    for(String fileStr : dumpXmlDesFileList){
                        StringBuffer buffer = appDynamicAnalyseService.getAppDynamicParseLog(fileStr,"utf-8");
                        AppDynamicResult appDynamicResult = appDynamicAnalyseService.analysisXml(buffer);
                        appDynamicResult.setAppId(appTelecomLink.getId());
                        appDynamicParseUrlService.saveAppDynamicAnylasisResult(appDynamicResult,1);
                    }

                    appTelecomLinkRepository.updateAppTelecomLinkByDump(appTelecomLink.getId(),1);


                }
            }

        }catch(Exception ex){

            System.out.println(ex);
        }
        return false;
    }

    /***
     * 获取脱壳后指定文件信息
     * @param dumpDespPath apk脱壳后目录
     * @param fileType  指定文件类型
     * @return
     */
    @Override
    public List<String> getApkDumpFileList(String dumpDespPath, String fileType) {
        List<String> list = new ArrayList<>();
        FileSearcher.findFiles(dumpDespPath, fileType,list);
        return list;
    }
}
