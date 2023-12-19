package com.play.model;


import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

import java.util.Date;

@Table(name = "sys_app_launcher")
public class SysAppLauncher {

	@Column(name = "id",primarykey = true)
	private Long id;
	/**
	 * 系统上的图片名
	 */
	@Column(name = "img_real_name")
	private String imgRealName;
	/**
	 * 图片在系统上的路径
	 */
	@Column(name = "img_path")
	private String imgPath;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "create_datetime")
	private Date createDatetime;

	@Column(name = "img_ext")
	private String imgExt;
	/**
	 * 文件大小单位MB，KB，B，GB
	 */
	@Column(name = "img_size")
	private String imgSize;

	@Column(name = "size")
	private Long size;

	@Column(name = "status")
	private Integer status;

	@Column(name = "source")
	private Integer source;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImgRealName() {
		return imgRealName;
	}

	public void setImgRealName(String imgRealName) {
		this.imgRealName = imgRealName;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public String getImgExt() {
		return imgExt;
	}

	public void setImgExt(String imgExt) {
		this.imgExt = imgExt;
	}

	public String getImgSize() {
		return imgSize;
	}

	public void setImgSize(String imgSize) {
		this.imgSize = imgSize;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}
}
