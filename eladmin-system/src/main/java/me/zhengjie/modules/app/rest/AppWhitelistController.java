package me.zhengjie.modules.app.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.rest.AnonymousPostMapping;
import me.zhengjie.modules.app.service.AppService;
import me.zhengjie.modules.app.service.AppTelecomLinkService;
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
    @Log("解析App白名单APK")
    @ApiOperation("解析App白名单APK")
    @AnonymousPostMapping("/runAppWhiteList")
    public void runAppWhiteList(){
        appService.runAppWhiteList();
    }
}
