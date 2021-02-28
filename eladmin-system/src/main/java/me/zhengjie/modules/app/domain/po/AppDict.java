package me.zhengjie.modules.app.domain.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import lombok.Data;

import java.io.Serializable;


/**
 * app对应的字典表
 *
 * @author xinglei
 * @date 2021-02-28 13:09:58
 */
@Data
@Entity
@Table(name = "app_dict")
@ApiModel(value = "app对应的字典表")
public class AppDict implements Serializable {

	/**
	 * 主键
	 * nullable : false
	 * default  : null
	 */
	@Id
	@ApiModelProperty(value = "主键")
	@Column(name = "id", nullable = true, length = 20)
	private Long id;

	/**
	 * 0：表示链接过滤词，1：应用名关键词
	 * nullable : false
	 * default  : 0
	 */
	@ApiModelProperty(value = "0：表示链接过滤词，1：应用名关键词")
	@Column(name = "type", nullable = true, length = 11)
	private Integer type;

	/**
	 * 对应字典值
	 * nullable : false
	 * default  : null
	 */
	@ApiModelProperty(value = "对应字典值")
	@Column(name = "dict_value", nullable = true, length = 128)
	private String dictValue;

	/**
	 * 0：禁用，1：启用
	 * nullable : false
	 * default  : 1
	 */
	@ApiModelProperty(value = "0：禁用，1：启用")
	@Column(name = "status", nullable = true, length = 11)
	private Integer status;

	/**
	 * 备注
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "备注")
	@Column(name = "remark", nullable = true, length = 128)
	private String remark;

	/**
	 * 针对type：1时候起作用
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "针对type：1时候起作用")
	@Column(name = "sub_type", nullable = true, length = 11)
	private Integer subType;
}
