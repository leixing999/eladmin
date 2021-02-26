package me.zhengjie.modules.app.domain.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import lombok.Data;

import java.io.Serializable;


/**
 * app下载链接信息
 *
 * @author xinglei
 * @date 2021-02-24 18:20:06
 */
@Data
@Entity
@Table(name = "app_telecom_link")
@ApiModel(value = "app下载链接信息")
public class AppTelecomLink implements Serializable {

	/**
	 * 自增量，主键
	 * nullable : false
	 * default  : null
	 */
	@Id
	@ApiModelProperty(value = "自增量，主键")
	@Column(name = "id", nullable = true, length = 30)
	private String id;

	/**
	 * app关联电信诈骗APP文件编号
	 * nullable : false
	 * default  : null
	 */
	@ApiModelProperty(value = "app关联电信诈骗APP文件编号")
	@Column(name = "app_rel_file_id", nullable = true, length = 32)
	private String appRelFileId;

	/**
	 * app对应文件名称
	 * nullable : false
	 * default  : null
	 */
	@ApiModelProperty(value = "app对应文件名称")
	@Column(name = "app_file_name", nullable = true, length = 600)
	private String appFileName;

	/**
	 * app对应的下载的地址
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "app对应的下载的地址")
	@Column(name = "app_origin_link", nullable = true, length = 600)
	private String appOriginLink;

	/**
	 * app下载链接对应在电信文件包的行号
	 * nullable : false
	 * default  : null
	 */
	@ApiModelProperty(value = "app下载链接对应在电信文件包的行号")
	@Column(name = "app_origin_text_line", nullable = true, length = 20)
	private Long appOriginTextLine;

	/**
	 * 文件大小
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "文件大小")
	@Column(name = "app_file_size", nullable = true, length = 20)
	private Long appFileSize;

	/**
	 * app下载时间
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "app下载时间")
	@Column(name = "app_download_time", nullable = true, length = 30)
	private String appDownloadTime;

	/**
	 * app是否下载（0：未下载，1：下载成功，-1：下载异常）
	 * nullable : true
	 * default  : 0
	 */
	@ApiModelProperty(value = "app是否下载（0：未下载，1：下载成功，-1：下载异常）")
	@Column(name = "app_is_down", nullable = true, length = 11)
	private Integer appIsDown;

	/**
	 * app下载花费时间
	 * nullable : true
	 * default  : 0
	 */
	@ApiModelProperty(value = "app下载花费时间")
	@Column(name = "app_download_spend_time", nullable = true, length = 11)
	private Integer appDownloadSpendTime;

	/**
	 * app静态分析对应的包名
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "app静态分析对应的包名")
	@Column(name = "app_package_name", nullable = true, length = 200)
	private String appPackageName;

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
	 * app静态分析对应版本号
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "app静态分析对应版本号")
	@Column(name = "app_version", nullable = true, length = 100)
	private String appVersion;

	/**
	 * 增加时间
	 * nullable : true
	 * default  : CURRENT_TIMESTAMP
	 */
	@ApiModelProperty(value = "增加时间")
	@Column(name = "app_add_time", nullable = true)
	private java.util.Date appAddTime;

	/**
	 * app更新时间
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "app更新时间")
	@Column(name = "app_update_time", nullable = true)
	private java.util.Date appUpdateTime;

}
