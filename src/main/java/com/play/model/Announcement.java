package com.play.model;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
/**
 * 站点公告 
 *
 * @author admin
 *
 */
@Table(name = "announcement")
public class Announcement {
	public static final int TYPE_ALL_STATION = 1;// 群发
	public static final int TYPE_SOME_STATION = 2;// 个别站点
	
	@Column(name = "id", primarykey = true)
	private Long id;
	/**
	 * 类别，1=群发，2=个别站点
	 */
	@Column(name = "type")
	private Integer type;
	/**
	 * 内容
	 */
	@Column(name = "content")
	private String content;
	/**
	 * 创建时间
	 */
	@Column(name = "create_datetime")
	private Date createDatetime;
	/**
	 * 开始时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Column(name = "start_datetime")
	private Date startDatetime;
	/**
	 * 结束时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Column(name = "end_datetime")
	private Date endDatetime;
	/**
	 * 接收类型 1：真人 2:电子 3：体育 4：棋牌 5:彩票
	 */
	@Column(name = "accept_type")
	private String acceptType;
	/**
	 * 是否弹出1是2否
	 */
	@Column(name = "dialog_flag")
	private Integer dialogFlag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public Date getStartDatetime() {
		return startDatetime;
	}

	public void setStartDatetime(Date startDatetime) {
		this.startDatetime = startDatetime;
	}

	public Date getEndDatetime() {
		return endDatetime;
	}

	public void setEndDatetime(Date endDatetime) {
		this.endDatetime = endDatetime;
	}

	public String getAcceptType() {
		return acceptType;
	}

	public void setAcceptType(String acceptType) {
		this.acceptType = acceptType;
	}

	public Integer getDialogFlag() {
		return dialogFlag;
	}

	public void setDialogFlag(Integer dialogFlag) {
		this.dialogFlag = dialogFlag;
	}

}
