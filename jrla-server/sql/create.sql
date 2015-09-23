create database jrla;

use jrla;


CREATE TABLE jrla_head(/*头像表*/
	tid	int		NOT NULL,
	tname	varchar(50),
	tdis	char(255),
	tdata	blob,
	uid	int,
	ttime	TIMESTAMP,
	PRIMARY KEY(tid)
);


CREATE TABLE jrla_user(/*用户表*/
	uid		int		NOT NULL,
	u_name		char(50)	NOT NULL,
	u_qq		varchar(15),
	u_pwd		varchar(50),
	u_Email		varchar(50),
	u_dis		char(255),
	location  	char(255),
	info_lon	float(17,14),
	info_lat	float(17,14),
	u_head		int,
	u_admin		boolean NOT NULL,
	u_mood		varchar(50),
	u_integral	int,
	u_hobby		varchar(50),
	u_level		int,
	u_number	int,
	PRIMARY KEY(uid),
	FOREIGN KEY(u_head) REFERENCES jrla_head(tid)
);


CREATE TABLE jrla_sort(/*节日种类表*/
	sid		int  NOT NULL,
	info_sort	char(50)  NOT NULL,
	PRIMARY KEY(sid)
);
	

CREATE TABLE jrla_info(/*节日信息表*/
	mid			int NOT NULL,	
	info_title		char(50)	NOT NULL,
	info_dis		text,
	info_sort		int,
	solar-start		TIMESTAMP
	solar-end		TIMESTAMP
	lunar-start		TIMESTAMP
	lunar-end		TIMESTAMP
	info_title		char(255),
	info_info		MEDIUMTEXT,
	uid			int,
	hotel_name		char(50),
	PRIMARY KEY(mid),
	FOREIGN KEY(uid) REFERENCES jrla_user(uid),
	FOREIGN KEY(info_sort) REFERENCES jrla_sort(sid)
);


CREATE TABLE jrla_image(/*节日图片表*/
	id  		int  		NOT NULL,
	mid  		int  		NOT NULL,
	image_data  	blob,
	image_time  	TIMESTAMP,
	PRIMARY KEY(id),
	FOREIGN KEY(mid) REFERENCES jrla_info(mid)
);


CREATE TABLE jrla_recommend(/*最近节日推荐表*/
	id  		int  		NOT NULL,
	mid  		int  		NOT NULL,
	recommend_time  TIMESTAMP,
	PRIMARY KEY(id),
	FOREIGN KEY(mid) REFERENCES jrla_info(mid)
);



CREATE TABLE jrla_activity(/*节日活动表*/
	id  		int  		NOT NULL,
	mid  		int  		NOT NULL,
	data		TIMESTAMP,
	location  	char(255),
	info_lon	float(17,14),
	info_lat	float(17,14),
	title		char(255),
	info		MEDIUMTEXT,
	PRIMARY KEY(id),
	FOREIGN KEY(mid) REFERENCES jrla_info(mid)
);





CREATE TABLE jrla_info(/*节日礼物信息表*/
	mid			int NOT NULL,	
	info_title		char(50) NOT NULL,
	info_dis		text,
	info_lon		float(17,14),
	info_lat		float(17,14),
	info_sort		int,
	info_price		double,
	info_time		TIMESTAMP,
	uid			int,
	hotel_name		char(50),
	PRIMARY KEY(mid),
	FOREIGN KEY(uid) REFERENCES jrla_user(uid),
	FOREIGN KEY(info_sort) REFERENCES jrla_sort(sid)
);


CREATE TABLE jrla_info(/*节日食品信息表*/
	mid			int NOT NULL,	
	info_title		char(50)	NOT NULL,
	info_dis		text,
	info_lon		float(17,14),
	info_lat		float(17,14),
	info_sort		int,
	info_price		double,
	info_time		TIMESTAMP,
	uid			int,
	hotel_name		char(50),
	PRIMARY KEY(mid),
	FOREIGN KEY(uid) REFERENCES jrla_user(uid),
	FOREIGN KEY(info_sort) REFERENCES jrla_sort(sid)
);


CREATE TABLE jrla_col(/*我的收藏表*/
	mid	int		NOT NULL,
	uid	int		NOT NULL,
	comment	char(255),
	FOREIGN KEY(mid) REFERENCES jrla_info(mid),
	FOREIGN KEY(uid) REFERENCES jrla_user(uid)
);




CREATE TABLE jrla_ads(/*广告表*/
	gid	int		NOT NULL,
	gdis	char(255),
	PRIMARY KEY(gid)
);


CREATE TABLE jrla_ads_image(/*广告图片表*/
	gpid	int		NOT NULL,
	gid	int		NOT NULL,
	gdata	blob,
	PRIMARY KEY(gpid),
	FOREIGN KEY(gid) REFERENCES jrla_ads(gid)
);


CREATE TABLE jrla_max(/*最大编号表*/
	jrla_head	int	default 0,
	jrla_user	int	default 0,
	jrla_info	int	default 0,
	jrla_image	int	default 0,
	jrla_ads	int	default 0,
	jrla_ads_image	int	default 0,
	jrla_recommend	int	default 0,
	jrla_sort	int default 0
);

commit;
show tables;
