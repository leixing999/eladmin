package me.zhengjie.modules.app.repository;

import me.zhengjie.modules.app.domain.po.AppDynamicParseUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * AppDynamicParseUrl Repository层
 *
 * @author xinglei
 * @date 2021-03-22 13:39:39
 */
public interface AppDynamicParseUrlRepository extends JpaRepository<AppDynamicParseUrl, String> {

    /***
     * 获取指定APP解析的域名信息
     * @param appId
     * @param type
     * @return
     */
    List<AppDynamicParseUrl> findByAppIdAndType(String appId,Integer type);
}
