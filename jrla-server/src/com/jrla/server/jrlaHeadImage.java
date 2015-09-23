package com.jrla.server;

import java.sql.Blob;

public class jrlaHeadImage {
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public String getTdis() {
		return tdis;
	}
	public void setTdis(String tdis) {
		this.tdis = tdis;
	}
	public Blob getB() {
		return b;
	}
	public void setB(Blob b) {
		this.b = b;
	}
	public jrlaHeadImage(int tid, String tdis, Blob b, int uid) {
		super();
		this.tid = tid;
		this.tdis = tdis;
		this.b = b;
		this.uid = uid;
	}
	int tid;
	String tdis;
	Blob b;
	int uid;
}
