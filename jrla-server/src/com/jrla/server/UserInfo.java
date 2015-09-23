package com.jrla.server;

public class UserInfo {
	int uid;
	String u_name;
	String u_qq;
	String u_Email;
	String u_dis;
	int u_head;
	boolean u_admin;
	String u_mood;
	int u_integral;//积分
	String u_hobby;
	int u_level;
	int u_number;//访问次数
	public UserInfo(int uid, String uName, String uQq, String uEmail,
			String uDis, int uHead, boolean uAdmin, String uMood,
			int uIntegral, String uHobby, int uLevel, int uNumber) {
		super();
		this.uid = uid;
		u_name = uName;
		u_qq = uQq;
		u_Email = uEmail;
		u_dis = uDis;
		u_head = uHead;
		u_admin = uAdmin;
		u_mood = uMood;
		u_integral = uIntegral;
		u_hobby = uHobby;
		u_level = uLevel;
		u_number = uNumber;
	}

	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getU_name() {
		return u_name;
	}
	public void setU_name(String uName) {
		u_name = uName;
	}
	public String getU_qq() {
		return u_qq;
	}
	public void setU_qq(String uQq) {
		u_qq = uQq;
	}
	public String getU_Email() {
		return u_Email;
	}
	public void setU_Email(String uEmail) {
		u_Email = uEmail;
	}
	public String getU_dis() {
		return u_dis;
	}
	public void setU_dis(String uDis) {
		u_dis = uDis;
	}
	public int getU_head() {
		return u_head;
	}
	public void setU_head(int uHead) {
		u_head = uHead;
	}
	public boolean isU_admin() {
		return u_admin;
	}
	public void setU_admin(boolean uAdmin) {
		u_admin = uAdmin;
	}
	public String getU_mood() {
		return u_mood;
	}
	public void setU_mood(String uMood) {
		u_mood = uMood;
	}
	public int getU_integral() {
		return u_integral;
	}
	public void setU_integral(int uIntegral) {
		u_integral = uIntegral;
	}
	public String getU_hobby() {
		return u_hobby;
	}
	public void setU_hobby(String uHobby) {
		u_hobby = uHobby;
	}
	public int getU_level() {
		return u_level;
	}
	public void setU_level(int uLevel) {
		u_level = uLevel;
	}
	public int getU_number() {
		return u_number;
	}
	public void setU_number(int uNumber) {
		u_number = uNumber;
	}

}
