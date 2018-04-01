package com.tsingma.business.member.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "b_member")
public class Member {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String unionId;
	private String appid;
	private String openId;
	private String nickName;
	private String gender;
	private String language;
	private String city;
	private String province;
	private String country;
	private String avatarUrl;
	private String timestamp;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUnionId() {
		return unionId;
	}
	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public Member() {
		super();
	}
	public Member(Integer id, String unionId, String appid, String openId, String nickName, String gender,
			String language, String city, String province, String country, String avatarUrl, String timestamp) {
		super();
		this.id = id;
		this.unionId = unionId;
		this.appid = appid;
		this.openId = openId;
		this.nickName = nickName;
		this.gender = gender;
		this.language = language;
		this.city = city;
		this.province = province;
		this.country = country;
		this.avatarUrl = avatarUrl;
		this.timestamp = timestamp;
	}
	public Member(String unionId, String appid, String openId, String nickName, String gender, String language,
			String city, String province, String country, String avatarUrl, String timestamp) {
		super();
		this.unionId = unionId;
		this.appid = appid;
		this.openId = openId;
		this.nickName = nickName;
		this.gender = gender;
		this.language = language;
		this.city = city;
		this.province = province;
		this.country = country;
		this.avatarUrl = avatarUrl;
		this.timestamp = timestamp;
	}
	

}
