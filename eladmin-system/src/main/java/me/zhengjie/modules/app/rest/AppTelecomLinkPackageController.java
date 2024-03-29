package me.zhengjie.modules.app.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.rest.AnonymousGetMapping;
import me.zhengjie.annotation.rest.AnonymousPostMapping;
import me.zhengjie.modules.app.domain.po.AppTelecomLinkPackage;
import me.zhengjie.modules.app.service.AppTelecomLinkPackageService;
import me.zhengjie.modules.app.service.dto.LinkPackageQueryCriteria;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.RequestHolder;
import me.zhengjie.utils.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Api(tags="电信App文件")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/AppTelecomLinkPackage")
public class AppTelecomLinkPackageController {
    private final AppTelecomLinkPackageService appTelecomLinkPackageService;

    @Log("查询电信APP信息")
    @ApiOperation("查询电信APP信息")
    @AnonymousGetMapping(value="/query")
    public ResponseEntity<Object> query(LinkPackageQueryCriteria linkPackageQueryCriteria, Pageable pageable, HttpServletRequest request){
        return new ResponseEntity<>(appTelecomLinkPackageService.findAll(linkPackageQueryCriteria,pageable),HttpStatus.OK);
    }


    @Log("保存电信App文件包信息")
    @ApiOperation("保存电信App文件包信息")
    @AnonymousGetMapping(value="/saveAppTelecomLinkPackage1")
    public void saveAppTelecomLinkPackage(@Validated @RequestBody AppTelecomLinkPackage appTelecomLinkPackage){
        appTelecomLinkPackageService.saveAppTelecomLinkPackage(appTelecomLinkPackage);
    }

    @Log("修改电信App文件包信息")
    @ApiOperation("修改电信App文件包信息")
    @AnonymousPostMapping(value="/updateLinkPackage")
    public void updateLinkPackage(@RequestParam String id, @RequestParam Integer currentLines){
        appTelecomLinkPackageService.updateLinkPackage(id,currentLines);
    }

    @Log("修改电信App文件包信息解析开始时间")
    @ApiOperation("修改电信App文件包信息解析开始时间")
    @AnonymousPostMapping(value="/updateLinkPackageBeginTime")
    public void updateLinkPackage(@RequestParam String id, @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date beginTime, @RequestParam Integer currentLines){
        appTelecomLinkPackageService.updateLinkPackage(id,beginTime,currentLines);
    }


    @Log("修改电信解析包状态、当前行和状态")
    @ApiOperation("修改电信解析包状态、当前行和状态")
    @AnonymousPostMapping(value="/updateLinkPackageEndTime")
    public void updateLinkPackage(@RequestParam String id, @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date endTime, @RequestParam Integer currentLines,Integer status){
        appTelecomLinkPackageService.updateLinkPackage(id,endTime,currentLines,status);
    }
}
