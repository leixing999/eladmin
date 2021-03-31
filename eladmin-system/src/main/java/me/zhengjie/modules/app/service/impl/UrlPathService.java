package me.zhengjie.modules.app.service.impl;

import me.zhengjie.modules.app.domain.vo.UrlPathVO;
import me.zhengjie.modules.app.service.AppDictService;
import me.zhengjie.utils.ConverPercent;
import me.zhengjie.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/****
 * apk url转换服务
 * @author xinglei
 * @date 2021-02-21
 */
@Service
public class UrlPathService {
    private static final Logger log = LoggerFactory.getLogger(UrlPathService.class);
    @Autowired
    AppDictService appDictService;

    //广告点击数（开始结束）过滤条件
    @Value("${file.apk.filter.adStart}")
    long adStart;
    @Value("${file.apk.filter.adEnd}")
    long adEnd;
    //访问数（开始，结束）过滤条件
    @Value("${file.apk.filter.visitStartNum}")
    long visitStartNum;
    @Value("${file.apk.filter.visitEndNum}")
    long visitEndNum;

    public List<UrlPathVO>parseApkUrlPath(String apkFilePath){
        List<String> list = FileUtil.getFileRecords(apkFilePath);
        List<UrlPathVO> urlPathVOList = this.parseApkUrlPath(list);
        return urlPathVOList;
    }



    /***
     * 解析文本文件原始行信息转换成实际apk文件路径信息
     * @param urlTextList
     * @return urlPathVOList
     */
    public List<UrlPathVO> parseApkUrlPath(List<String> urlTextList){
        List<UrlPathVO> urlPathVOList = new ArrayList<>();
        for(String path : urlTextList){
            UrlPathVO pathVO = new UrlPathVO();
            String orignUrlPath = path;
            //解析url进行分割
            String [] urlSplit = orignUrlPath.split("\\t");
            if(urlSplit.length==3){
                String decodeUrlPath = URLDecoderString(ConverPercent.convertPercent(urlSplit[0]));
                boolean isApk = decodeUrlPath.lastIndexOf(".apk")>0 ? true: false;
                String requestUrlPath = isApk ? getRequestUrlPath(decodeUrlPath) : "";
                String apkFileName = isApk ? getApkFileName(requestUrlPath) : "";
                pathVO.setOrignUrlPath(urlSplit[0]);
                pathVO.setDecodeUrlPath(decodeUrlPath);
                pathVO.setRequestApkUrlPath(requestUrlPath);
                pathVO.setApk(isApk);
                pathVO.setApkFileName(apkFileName);

                pathVO.setAdNum(Long.parseLong(urlSplit[1]));
                pathVO.setVisitNum(Long.parseLong(urlSplit[2]));

                //判断是否满足过滤条件，不满足添加进去
                if((pathVO.getVisitNum()>visitStartNum && pathVO.getVisitNum()<=visitEndNum)
                        &&(pathVO.getAdNum()>adStart && pathVO.getAdNum()<=adEnd)){
                    //去掉干扰信息过滤
                    if(appDictService.appDictFilter(0,requestUrlPath).size()==0){
                        urlPathVOList.add(pathVO);
                    }
                }
//                urlPathVOList.add(pathVO);

            }
        }

        //去掉重复记录按照apk下载地址
        return urlPathVOList.stream().collect(
                collectingAndThen(
                        toCollection(() -> new TreeSet<>(Comparator.comparing(UrlPathVO::getRequestApkUrlPath))), ArrayList::new)
        );
    }

    /***
     * 获取实际请求url路径
     * @param urlPathVOList
     * @return
     */
    public List<UrlPathVO> getActualApkUrlPath(List<UrlPathVO> urlPathVOList){
                List<UrlPathVO> actualApkUrlPathList = urlPathVOList.stream().filter(urlPathVO ->
                             urlPathVO.isApk() && urlPathVO.getApkFileName().length()>0
                  ).collect(Collectors.toList());
                return actualApkUrlPathList;
    }

    /***
     * 获取apk文件名称
     * @param url
     * @return
     */
    private  String getApkFileName(String url){
        String apkFileName = "";
        try {
            int apkStartPosition = url.lastIndexOf("/");
            int apkEndPosition = url.lastIndexOf(".apk");
            if(apkEndPosition>apkStartPosition){
                apkFileName = url.substring(apkStartPosition+1, apkEndPosition + 4);
            }
        }catch(Exception ex){
            log.error("获取apk文件名称【getApkFileName】："+ex);
        }
        return apkFileName.replace("?","");
    }

    /***
     * 获取真正读取apk的url路径
     * @param url
     * @return
     */
    private  String getRequestUrlPath(String url){
        String requestUrlPath = "";
        try {
            //获取apk文件起始位置
            int apkLastPosition = url.lastIndexOf(".apk");
            int apkFristPosition = url.indexOf(".apk");
            //获取http位置
            int httpPosition = url.lastIndexOf("https:") > 0 ? url.lastIndexOf("https:") : url.lastIndexOf("http:");
            if (httpPosition > 0 && httpPosition< apkLastPosition && (apkLastPosition>apkFristPosition)) {
                requestUrlPath = url.substring(httpPosition, apkLastPosition + 4);
            } else {
                requestUrlPath = url.substring(0, apkFristPosition + 4);
            }
        }catch(Exception ex){
            log.error("获取apk文件名称【getRequestUrlPath】："+url);
        }
        return requestUrlPath;

    }

    /**
     * 对
     * @param str
     * @return
     */
    public  String URLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
