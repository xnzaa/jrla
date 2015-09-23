package com.jrla.server;

import java.sql.Blob;
import java.sql.Date;

public class jrlaImage {
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	public Blob getB() {
		return b;
	}
	public void setB(Blob b) {
		this.b = b;
	}
	public Date getImage_time() {
		return image_time;
	}
	public void setImage_time(Date imageTime) {
		image_time = imageTime;
	}
	int id;
	int mid;
	Blob b;
	Date image_time;
	
	public jrlaImage(int id,int mid,Blob b,Date image_time){
		this.id = id;
		this.mid = mid;
		this.b = b;
		this.image_time = image_time;
	}
}
