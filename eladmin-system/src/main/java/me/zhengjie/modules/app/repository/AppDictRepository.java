package me.zhengjie.modules.app.repository;

import me.zhengjie.modules.app.domain.po.AppDict;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * AppDict Repository层
 *
 * @author xinglei
 * @date 2021-02-28 13:09:58
 */
public interface AppDictRepository extends JpaRepository<AppDict, Long> {

}
