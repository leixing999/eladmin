package me.zhengjie.modules.app.domain.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import lombok.Data;

import java.io.Serializable;


/**
 * 电信app链接包
 *
 * @author xinglei
 * @date 2021-02-24 18:20:01
 */
@Data
@Entity
@Table(name = "app_telecom_link_package")
@ApiModel(value = "电信app链接包")
public class AppTelecomLinkPackage implements Serializable {

	/**
	 * 自增量
	 * nullable : true
	 * default  : null
	 */
	@Id
	@ApiModelProperty(value = "自增量")
	@Column(name = "id", nullable = true, length = 20)
	private Long id;

	/**
	 * 包含app链接的文件名
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "包含app链接的文件名")
	@Column(name = "link_package_name", nullable = true, length = 200)
	private String linkPackageName;

	/**
	 * 上传者IP地址
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "上传者IP地址")
	@Column(name = "ip", nullable = true, length = 30)
	private String ip;

	/**
	 * 包含app 链接的包大小
	 * nullable : true
	 * default  : 0
	 */
	@ApiModelProperty(value = "包含app 链接的包大小")
	@Column(name = "link_package_size", nullable = true, length = 20)
	private Long linkPackageSize;

	/**
	 * 包状态(0:待处理，1：处理中，2：处理完成）
	 * nullable : true
	 * default  : 0
	 */
	@ApiModelProperty(value = "包状态(0:待处理，1：处理中，2：处理完成）")
	@Column(name = "link_package_status", nullable = true, length = 11)
	private Integer linkPackageStatus;

	/**
	 * 包总行数
	 * nullable : true
	 * default  : 0
	 */
	@ApiModelProperty(value = "包总行数")
	@Column(name = "link_package_lines", nullable = true, length = 20)
	private Long linkPackageLines;

	/**
	 * 目前分析所在行
	 * nullable : true
	 * default  : 0
	 */
	@ApiModelProperty(value = "目前分析所在行")
	@Column(name = "link_package_parse_line", nullable = true, length = 20)
	private Long linkPackageParseLine;

	/**
	 * 添加时间
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "添加时间")
	@Column(name = "link_package_add_time", nullable = true)
	private java.util.Date linkPackageAddTime;

	/**
	 * 包分析开始时间
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "包分析开始时间")
	@Column(name = "link_package_parse_begin_time", nullable = true)
	private java.util.Date linkPackageParseBeginTime;

	/**
	 * 包分析结束时间
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "包分析结束时间")
	@Column(name = "link_package_parse_end_time", nullable = true)
	private java.util.Date linkPackageParseEndTime;
}
