package com.tsingma.business.scratch.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "b_activity")
public class Activity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String appid;
	private String name;
	private Double price;
	private Integer actNumber;
	private Integer virNumber;
	private Integer broNumber;
	private String imageUrl;
	private Boolean enable;
	private String createdTime;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getActNumber() {
		return actNumber;
	}
	public void setActNumber(Integer actNumber) {
		this.actNumber = actNumber;
	}
	public Integer getVirNumber() {
		return virNumber;
	}
	public void setVirNumber(Integer virNumber) {
		this.virNumber = virNumber;
	}
	public Integer getBroNumber() {
		return broNumber;
	}
	public void setBroNumber(Integer broNumber) {
		this.broNumber = broNumber;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Boolean getEnable() {
		return enable;
	}
	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	
	
}
