package me.zhengjie.modules.app.domain.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import lombok.Data;

import java.io.Serializable;


/**
 * 白名单权限表
 *
 * @author xinglei
 * @date 2021-02-27 11:05:58
 */
@Data
@Entity
@Table(name = "app_telecom_whitelist_permission")
@ApiModel(value = "白名单权限表")
public class AppTelecomWhitelistPermission implements Serializable {

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
	 * 外键，白名单库编号
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "外键，白名单库编号")
	@Column(name = "rel_id", nullable = true, length = 64)
	private String relId;

	/**
	 * 权限名称
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "权限名称")
	@Column(name = "permission", nullable = true, length = 160)
	private String permission;
}
