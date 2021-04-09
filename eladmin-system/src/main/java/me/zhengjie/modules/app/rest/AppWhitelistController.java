package me.zhengjie.modules.app.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.rest.AnonymousPostMapping;
import me.zhengjie.modules.app.service.AppService;
import me.zhengjie.modules.app.service.AppTelecomLinkService;
import me.zhengjie.modules.app.task.AppDataSyncRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags="App白名单")
@RequestMapping("/api/appWhitelist")
public class AppWhitelistController {
    private final AppTelecomLinkService appTelecomLinkService;
    private final AppService appService;
    private final AppDataSyncRunner appDataSyncRunner;
    @Log("解析App白名单APK")
    @ApiOperation("解析App白名单APK")
    @AnonymousPostMapping("/runAppWhiteList")
    public void runAppWhiteList(){
        appService.runAppWhiteList();
    }

    @Log("拷贝问题APK")
    @ApiOperation("拷贝问题APK")
    @AnonymousPostMapping("/copyQuesttionApp")
    public void copyQuesttionApp(){
        appDataSyncRunner.copyQuesttionApp();
    }

    @Log("拷贝灰色问题APK")
    @ApiOperation("拷贝灰色问题APK")
    @AnonymousPostMapping("/copyHsQuesttionApp")
    public void copyHsQuesttionApp(){
        appDataSyncRunner.copyHsQuesttionApp();
    }
}
