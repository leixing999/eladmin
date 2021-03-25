package me.zhengjie.modules.app.service.impl;

import me.zhengjie.modules.app.domain.po.AppDynamicParseUrl;
import me.zhengjie.modules.app.domain.po.AppDynamicParseWord;
import me.zhengjie.modules.app.domain.po.AppDynamicResult;
import me.zhengjie.modules.app.repository.AppDynamicParseUrlRepository;
import me.zhengjie.modules.app.repository.AppDynamicParseWordRepository;
import me.zhengjie.modules.app.service.AppDynamicAnalyseService;
import me.zhengjie.modules.app.service.AppDynamicParseUrlService;
import me.zhengjie.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;

import java.util.*;

/**
 * AppDynamicParseUrl 自定义Service实现层
 *
 * @author xinglei
 * @date 2021-03-22 13:39:39
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "appDynamicParseUrl")
public class AppDynamicParseUrlServiceImpl implements AppDynamicParseUrlService {

	private final AppDynamicParseUrlRepository appDynamicParseUrlRepository;

	private final AppDynamicParseWordRepository appDynamicParseWordRepository;

	@Autowired
	AppDynamicAnalyseService appDynamicAnalyseService;
	/***
	 * 保存APP动态解析结果信息
	 * @param appDynamicResult
	 * @param channel
	 */
	@Override
	public void saveAppDynamicAnylasisResult(AppDynamicResult appDynamicResult,int channel) {

		List<AppDynamicParseUrl> appUrlList = new ArrayList<>();

		List<AppDynamicParseWord> appWordList = new ArrayList<>();
		//解析请求域名信息(1)
		Iterator<String> urlIterator = appDynamicResult.getAppUrlSet().iterator();
		while (urlIterator.hasNext()){
			AppDynamicParseUrl appDynamicParseUrl = new AppDynamicParseUrl();
			appDynamicParseUrl.setId(UUID.randomUUID().toString());
			appDynamicParseUrl.setAppId(appDynamicResult.getAppId());
			appDynamicParseUrl.setUrl(urlIterator.next());
			appDynamicParseUrl.setType(1);
			appDynamicParseUrl.setChannel(channel);
			appUrlList.add(appDynamicParseUrl);

		}
		//解析请求图片地址信息(2)
		urlIterator = appDynamicResult.getAppUrlByImageSet().iterator();
		while (urlIterator.hasNext()){
			AppDynamicParseUrl appDynamicParseUrl = new AppDynamicParseUrl();
			appDynamicParseUrl.setId(UUID.randomUUID().toString());
			appDynamicParseUrl.setAppId(appDynamicResult.getAppId());
			appDynamicParseUrl.setUrl(urlIterator.next());
			appDynamicParseUrl.setType(3);
			appDynamicParseUrl.setChannel(channel);
			appUrlList.add(appDynamicParseUrl);
		}

		//解析请求html地址信息(3)
		urlIterator = appDynamicResult.getAppUrlByHtmlSet().iterator();
		while (urlIterator.hasNext()){
			AppDynamicParseUrl appDynamicParseUrl = new AppDynamicParseUrl();
			appDynamicParseUrl.setId(UUID.randomUUID().toString());
			appDynamicParseUrl.setAppId(appDynamicResult.getAppId());
			appDynamicParseUrl.setUrl(urlIterator.next());
			appDynamicParseUrl.setType(2);
			appDynamicParseUrl.setChannel(channel);
			appUrlList.add(appDynamicParseUrl);
		}

		//解析App 动态敏感词
		urlIterator = appDynamicResult.getAppSensiveSet().iterator();
		while (urlIterator.hasNext()){
			AppDynamicParseWord appDynamicParseWord = new AppDynamicParseWord();
			appDynamicParseWord.setId(UUID.randomUUID().toString());
			appDynamicParseWord.setAppId(appDynamicResult.getAppId());
			appDynamicParseWord.setChannel(channel);
			appDynamicParseWord.setContent(urlIterator.next());
			appWordList.add(appDynamicParseWord);
		}
		//批量保存APP请求的URL
		for(AppDynamicParseUrl appDynamicParseUrl : appUrlList){
			try{

				appDynamicParseUrlRepository.save(appDynamicParseUrl);
			}catch (Exception ex){
				System.out.println("保存APP请求的URL"+ex);
			}
		}
		//批量保存APP动态解析敏感词
		for(AppDynamicParseWord appDynamicParseWord : appWordList) {
			try {
				appDynamicParseWordRepository.save(appDynamicParseWord);
			} catch (Exception ex) {
				System.out.println("保存APP请求的敏感词" + ex);
			}
		}

	}
	/****
	 * 保存APP动态解析结果信息
	 * @param requestPath 请求结果路径
	 * @param responsePath 相应结果路径
	 * @param appId
	 */
	@Override
	public void saveAppDynamicAnylasisResult(String requestPath, String responsePath,String appId) {

		AppDynamicResult appDynamicResult = appDynamicAnalyseService.getAppDynamicAnalysisResult(requestPath,responsePath);
		appDynamicResult.setAppId(appId);
		this.saveAppDynamicAnylasisResult(appDynamicResult,0);
	}
}
