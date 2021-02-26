package me.zhengjie.modules.app.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.rest.AnonymousPostMapping;
import me.zhengjie.modules.app.domain.po.AppPermission;
import me.zhengjie.modules.app.domain.po.AppTelecomLink;
import me.zhengjie.modules.app.service.AppPermissionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags="app电信权限同步")
@RequestMapping("/api/appTelecomLinkPermission")
public class AppTelecomLinkPermissionController {

   private final AppPermissionService appPermissionService;
    @Log("新增App权限解析")
    @ApiOperation("新增App权限解析")
    @AnonymousPostMapping("/saveAppPermission")
    public void saveAppPermission(@Validated @RequestBody AppPermission appPermission){

        appPermissionService.saveAppPermission(appPermission);
    }
}
