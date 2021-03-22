package me.zhengjie.modules.app.service;

import me.zhengjie.modules.app.domain.po.AppDynamicResult;

import java.util.Set;

public interface AppDynamicAnalyseService {

    /***
     * 动态将文本的内容解析为记录集
     * @param buffer
     * @return
     */
    Set getAppDynamicParseRecord(StringBuffer buffer);

    /****
     * 按照动态解析将日志文件内容读取到字符串缓冲区里
     * @param logPath
     * @return
     */
    StringBuffer getAppDynamicParseLog(String logPath);

    /****
     * 将json数据转换为结果对象结果集
     * @param objJson
     * @param appDynamicResult
     */
    void  analysisJson(Object objJson, AppDynamicResult appDynamicResult);

    /****
     * 将json数据转换为结果对象结果集
     * @param logPath
     * @return
     */
    AppDynamicResult analysisJson(String logPath);

}
