package me.zhengjie.modules.app.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

@Data
public class LinkPackageQueryCriteria {
    @Query(type = Query.Type.INNER_LIKE)
    private String linkPackageName;
}
