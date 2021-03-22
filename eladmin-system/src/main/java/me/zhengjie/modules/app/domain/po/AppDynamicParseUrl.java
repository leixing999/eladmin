package me.zhengjie.modules.app.domain.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import lombok.Data;

import java.io.Serializable;


/**
 * AppDynamicParseUrl
 *
 * @author xinglei
 * @date 2021-03-22 13:39:39
 */
@Data
@Entity
@Table(name = "app_dynamic_parse_url")
@ApiModel(value = "AppDynamicParseUrl")
public class AppDynamicParseUrl implements Serializable {

	/**
	 * 自增量主键
	 * nullable : false
	 * default  : null
	 */
	@Id
	@ApiModelProperty(value = "自增量主键")
	@Column(name = "id", nullable = true, length = 20)
	private Long id;

	/**
	 * 外键APP编号
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "外键APP编号")
	@Column(name = "app_id", nullable = true, length = 64)
	private String appId;

	/**
	 * url地址
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "url地址")
	@Column(name = "url", nullable = true, length = 256)
	private String url;

	/**
	 * 1：域名，2：html,3:图片资源
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "1：域名，2：html,3:图片资源")
	@Column(name = "type", nullable = true, length = 11)
	private Integer type;
}
