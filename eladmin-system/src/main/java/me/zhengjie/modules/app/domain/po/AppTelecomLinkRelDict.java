package me.zhengjie.modules.app.domain.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;


/**
 * AppTelecomLinkRelDict
 *
 * @author xinglei
 * @date 2021-04-06 15:12:32
 */
@Data
@Entity
@Table(name = "app_telecom_link_rel_dict")
@ApiModel(value = "AppTelecomLinkRelDict")
public class AppTelecomLinkRelDict implements Serializable {

	/**
	 * id
	 * nullable : false
	 * default  : null
	 */
	@Id
	@ApiModelProperty(value = "id")
	@Column(name = "id", nullable = true)
	private String id;

	/**
	 * app编号
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "app编号")
	@Column(name = "app_id", nullable = true, length = 64)
	private String appId;

	/**
	 * 字典编号
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "字典编号")
	@Column(name = "dict_id", nullable = true)
	private Long dictId;
}
