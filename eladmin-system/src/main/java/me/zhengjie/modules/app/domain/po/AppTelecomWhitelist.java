package me.zhengjie.modules.app.domain.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import lombok.Data;

import java.io.Serializable;


/**
 * App电信白名单库
 *
 * @author xinglei
 * @date 2021-02-27 10:52:25
 */
@Data
@Entity
@Table(name = "app_telecom_whitelist")
@ApiModel(value = "App电信白名单库")
public class AppTelecomWhitelist implements Serializable {

	/**
	 * 主键
	 * nullable : false
	 * default  : null
	 */
	@Id
	@ApiModelProperty(value = "主键")
	@Column(name = "id", nullable = true, length = 64)
	private String id;

	/**
	 * app 文件名
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "app 文件名")
	@Column(name = "app_filename", nullable = true, length = 64)
	private String appFilename;

	/**
	 * app静态分析对应类名
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "app静态分析对应类名")
	@Column(name = "app_class_name", nullable = true, length = 200)
	private String appClassName;

	/**
	 * app静态分析应用名
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "app静态分析应用名")
	@Column(name = "app_application_name", nullable = true, length = 200)
	private String appApplicationName;

	/**
	 * app静态分析对应的包名
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "app静态分析对应的包名")
	@Column(name = "app_package_name", nullable = true, length = 200)
	private String appPackageName;

	/**
	 * app静态分析对应版本号
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "app静态分析对应版本号")
	@Column(name = "app_version", nullable = true, length = 100)
	private String appVersion;

	/**
	 * 状态：1：启用，0：禁用
	 * nullable : true
	 * default  : 1
	 */
	@ApiModelProperty(value = "状态：1：启用，0：禁用")
	@Column(name = "status", nullable = true, length = 11)
	private Integer status;
}
