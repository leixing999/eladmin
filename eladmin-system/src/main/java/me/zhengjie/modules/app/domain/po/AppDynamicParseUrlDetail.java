package me.zhengjie.modules.app.domain.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import lombok.Data;

import java.io.Serializable;


/**
 * AppDynamicParseUrlDetail
 *
 * @author xinglei
 * @date 2021-03-22 13:39:31
 */
@Data
@Entity
@Table(name = "app_dynamic_parse_url_detail")
@ApiModel(value = "AppDynamicParseUrlDetail")
public class AppDynamicParseUrlDetail implements Serializable {

	/**
	 * 主键
	 * nullable : false
	 * default  : null
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@ApiModelProperty(value = "主键")
	@Column(name = "id", nullable = true, length = 20)
	private Long id;

	/**
	 * url表主键
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "url表主键")
	@Column(name = "url_id", nullable = true, length = 20)
	private Long urlId;

	/**
	 * 国家
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "国家")
	@Column(name = "country", nullable = true, length = 256)
	private String country;

	/**
	 * 城市
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "城市")
	@Column(name = "city", nullable = true, length = 256)
	private String city;

	/**
	 * ip
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "ip")
	@Column(name = "ip", nullable = true, length = 32)
	private String ip;

	/**
	 * 域名所属的人的电话号码
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "域名所属的人的电话号码")
	@Column(name = "phone", nullable = true, length = 30)
	private String phone;

	/**
	 * 域名所属的人的邮箱
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "域名所属的人的邮箱")
	@Column(name = "email", nullable = true, length = 256)
	private String email;

	/**
	 * 用户姓名
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "用户姓名")
	@Column(name = "username", nullable = true, length = 256)
	private String username;

	/**
	 * 注册开发商
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "注册开发商")
	@Column(name = "company", nullable = true, length = 512)
	private String company;

	/**
	 * 注册日期
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "注册日期")
	@Column(name = "register_time", nullable = true, length = 30)
	private String registerTime;

	/**
	 * 过期日期
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "过期日期")
	@Column(name = "expire_time", nullable = true, length = 30)
	private String expireTime;

	/**
	 * app_id主键
	 * nullable : true
	 * default  : null
	 */
	@ApiModelProperty(value = "app_id主键")
	@Column(name = "app_id", nullable = true, length = 64)
	private String appId;
}
