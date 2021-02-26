package me.zhengjie.modules.app.domain.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import lombok.Data;

import java.io.Serializable;


/**
 * app对应权限列表
 *
 * @author xinglei
 * @date 2021-02-26 16:11:46
 */
@Data
@Entity
@Table(name = "app_permission")
@ApiModel(value = "app对应权限列表")
public class AppPermission implements Serializable {

	/**
	 * 自增量
	 * nullable : false
	 * default  : null
	 */
	@Id
	@ApiModelProperty(value = "自增量")
	@Column(name = "id", nullable = true, length = 64)
	private String id;

	/**
	 * 关联app_telecom_link表
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "关联app_telecom_link表")
	@Column(name = "app_link_id", nullable = true, length = 64)
	private String appLinkId;

	/**
	 * app对应的权限名称
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "app对应的权限名称")
	@Column(name = "app_permission_name", nullable = true, length = 200)
	private String appPermissionName;
}
