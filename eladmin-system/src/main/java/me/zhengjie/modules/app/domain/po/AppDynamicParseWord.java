package me.zhengjie.modules.app.domain.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import lombok.Data;

import java.io.Serializable;


/**
 * AppDynamicParseWord
 *
 * @author xinglei
 * @date 2021-03-22 13:39:22
 */
@Data
@Entity
@Table(name = "app_dynamic_parse_word")
@ApiModel(value = "AppDynamicParseWord")
public class AppDynamicParseWord implements Serializable {

	/**
	 * 主键
	 * nullable : true
	 * default  : null
	 */
	@Id
	@ApiModelProperty(value = "主键")
	@Column(name = "id", nullable = true, length = 20)
	private String id;

	/**
	 * app 主键编号
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "app 主键编号")
	@Column(name = "app_id", nullable = true, length = 64)
	private String appId;

	/**
	 * 内容
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "内容")
	@Column(name = "content", nullable = true)
	private String content;

	/**
	 * 来源渠道
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "渠道0:动态分析，1：静态分析")
	@Column(name = "channel", nullable = true)
	private Integer channel;

}
