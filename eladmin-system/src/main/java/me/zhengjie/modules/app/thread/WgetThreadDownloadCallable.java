package me.zhengjie.modules.app.thread;

import me.zhengjie.modules.app.domain.po.AppTelecomLink;
import me.zhengjie.modules.app.domain.vo.UrlPathVO;
import me.zhengjie.utils.DateUtil;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Callable;

public class WgetThreadDownloadCallable implements Callable<AppTelecomLink> {
    private UrlPathVO urlPathVO ;
    private String appSavePath;
    private String fileId;
    private long maxFileSize=0;


    public WgetThreadDownloadCallable(UrlPathVO urlPathVO, String appSavePath, String fileId, long maxFileSize){
        this.urlPathVO = urlPathVO;
        this.appSavePath = appSavePath;
        this.fileId = fileId;
        this.maxFileSize = maxFileSize;
    }

    @Override
    public AppTelecomLink call() throws Exception {
        AppTelecomLink appTelecomLink = null;

        String appSysFileName = UUID.randomUUID().toString()+".apk";
        String relativeFilePath = DateUtil.getDefaultDateStr();
        long beginTime = System.currentTimeMillis();
        HttpDownloadService mtd = new HttpDownloadService(
                urlPathVO.getRequestApkUrlPath(),
                appSavePath + relativeFilePath,
                1,appSysFileName);

        long fileSize = mtd.download(maxFileSize);
        boolean isLimit = mtd.getIsLimit();
        boolean isDown = mtd.getIsDown();
         long endTime = System.currentTimeMillis();

        if (isDown || isLimit) {
            appTelecomLink = new AppTelecomLink();
            appTelecomLink.setAppFileName(urlPathVO.getApkFileName());
            appTelecomLink.setAppOriginLink(urlPathVO.getOrignUrlPath());
            appTelecomLink.setAppFileSize(fileSize);
            appTelecomLink.setAppIsDown( isDown ? 1:-1);
            appTelecomLink.setId(UUID.randomUUID().toString());
            appTelecomLink.setAppRelFileId(this.fileId);
            appTelecomLink.setAppAddTime(new Date());
            appTelecomLink.setAppSysFileName(appSysFileName);
            appTelecomLink.setAppSysRelativePath(relativeFilePath);


            appTelecomLink.setAppDownloadSpendTime(Integer.parseInt(""+(endTime-beginTime)));
        }

        return appTelecomLink;
    }
}
