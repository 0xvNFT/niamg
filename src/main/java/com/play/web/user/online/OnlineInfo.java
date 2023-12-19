package com.play.web.user.online;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class OnlineInfo {
	private Long id;
	private String ip;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date time;
	private Integer status;// 下线=1，上线=2

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
