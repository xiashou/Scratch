package com.tsingma.business.member.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Record {

	private String price;
	private String name;
	@Id
	private String time;
	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
