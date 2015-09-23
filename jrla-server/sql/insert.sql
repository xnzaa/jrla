insert into jrla_head(tid,tname,tdis,uid) 
	values(0,"默认头像","这是个默认的头像，并没有数据",0);/*插入头像*/
insert into jrla_user(uid,u_name,u_qq,u_Email,u_dis,u_head,
		u_pwd,u_admin,u_mood,u_integral,u_hobby,u_level,u_number) 
	values(0,"管理员账号","250178767","250178767@qq.com",
		"这是个测试管理用户",0,111,true,"快乐",376,"电脑",3,23);/*插入用户*/
insert into jrla_max values();/*插入一条最大编号记录*/
update jrla_max set jrla_head = 10;
update jrla_max set jrla_user = 10;
commit;