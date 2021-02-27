package me.zhengjie.modules.app.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.rest.AnonymousPostMapping;
import me.zhengjie.domain.ApkInfo;
import me.zhengjie.modules.app.domain.po.AppTelecomLink;
import me.zhengjie.modules.app.service.AppTelecomLinkService;
import me.zhengjie.utils.ApkUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags="电信链接分析")
@RequestMapping("/api/appTelecomLink")
public class AppTelecomLinkController {
    private final AppTelecomLinkService appTelecomLinkService;
    @Value("${file.aapt.path}")
    String aaptPath;
    @Log("新增App解析")
    @ApiOperation("新增App解析")
    @AnonymousPostMapping("/saveAppTelecomLink")
    public void saveAppTelecomLink(@Validated @RequestBody AppTelecomLink appTelecomLink){

        appTelecomLinkService.saveAppTelecomLink(appTelecomLink);
    }

    @Log("修改App解析")
    @ApiOperation("修改App解析")
    @AnonymousPostMapping("/updateAppTelecomLink")
    public void updateAppTelecomLink(@Validated @RequestBody AppTelecomLink appTelecomLink){
       // appTelecomLinkService.updateAppTelecomLink(appTelecomLink);

        try {
            ApkUtil apkUtil = new ApkUtil(aaptPath);
            ApkInfo apkInfo = apkUtil.getApkInfo("D:\\备份\\apk\\未检测\\Poker_installer.apk");
            System.out.println(apkInfo);
        }catch (Exception ex){
            System.out.println(ex);
        }

    }
}
