package me.zhengjie.modules.app.repository;

import me.zhengjie.modules.app.domain.po.AppDynamicParseUrl;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * AppDynamicParseUrl Repository层
 *
 * @author xinglei
 * @date 2021-03-22 13:39:39
 */
public interface AppDynamicParseUrlRepository extends JpaRepository<AppDynamicParseUrl, String> {

}
