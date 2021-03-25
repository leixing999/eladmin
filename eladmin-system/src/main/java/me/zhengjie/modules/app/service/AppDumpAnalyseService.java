package me.zhengjie.modules.app.service;

import me.zhengjie.modules.app.domain.vo.AppDumpVO;

import java.util.List;

public interface AppDumpAnalyseService {

    /***
     * 通过adx反编译工具进行脱壳处理
     * @param apkPath apk原始目录
     * @param dumpDespPath apk脱壳后目录
     * @param runCommand 脱壳运行命令
     */
    AppDumpVO apkDump(String apkPath, String dumpDespPath, String runCommand);


    /****
     * apk脱壳分析处理流程
     * @return
     */
    boolean apkDumpAnalyse();

    /***
     * 获取脱壳后指定文件信息
     * @param dumpDespPath apk脱壳后目录
     * @param fileType  指定文件类型
     * @return
     */
    List<String> getApkDumpFileList(String dumpDespPath, String fileType);


}
