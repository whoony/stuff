package com.stuff.bean;

import java.io.Serializable;
import java.util.Date;

public class DailyReport implements Serializable{

	private Long id;
	private Long principalId;
	private String uploadedName;
	private String generatedName;
	private String savedPath;
	private String mimeType;
	private Date createTime;
	private Date chooseTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPrincipalId() {
		return principalId;
	}
	public void setPrincipalId(Long principalId) {
		this.principalId = principalId;
	}
	public String getUploadedName() {
		return uploadedName;
	}
	public void setUploadedName(String uploadedName) {
		this.uploadedName = uploadedName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getSavedPath() {
		return savedPath;
	}
	public void setSavedPath(String savedPath) {
		this.savedPath = savedPath;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getGeneratedName() {
		return generatedName;
	}
	public void setGeneratedName(String generatedName) {
		this.generatedName = generatedName;
	}
	public Date getChooseTime() {
		return chooseTime;
	}
	public void setChooseTime(Date chooseTime) {
		this.chooseTime = chooseTime;
	}
}
