package me.zhengjie.modules.app.service;

import me.zhengjie.modules.app.domain.po.AppDynamicResult;
import me.zhengjie.modules.app.repository.AppDynamicParseUrlRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * AppDynamicParseUrl service层
 *
 * @author xinglei
 * @date 2021-03-22 13:39:39
 */
public interface AppDynamicParseUrlService {

    /***
     * 保存APP动态解析结果信息
     * @param appDynamicResult
     */
    void saveAppDynamicAnylasisResult(AppDynamicResult appDynamicResult);

    /****
     * 保存APP动态解析结果信息
     * @param requestPath 请求结果路径
     * @param responsePath 相应结果路径
     * @param appId
     */
    void saveAppDynamicAnylasisResult(String requestPath,String responsePath,String appId);
}
