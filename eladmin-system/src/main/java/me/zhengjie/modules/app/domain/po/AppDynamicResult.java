package me.zhengjie.modules.app.domain.po;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class AppDynamicResult {
    private Set<String> appSensiveSet = new HashSet<>();
    private Set<String> appUrlSet = new HashSet<>();
    private Set<String> appUrlByImageSet = new HashSet<>();
    private Set<String> appUrlByHtmlSet = new HashSet<>();

    private String appId;

}
