package com.jrla.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

public class thread extends Thread {
	Socket sc;// 声明Socket的引用
	main father;// 声明ServerThread的引用
	DataInputStream din = null;// 输入流
	DataOutputStream dout = null;// 输出流
	// BufferedReader din;
	// PrintWriter dout;
	private boolean flag = true;// 循环变量

	public thread(Socket sc) {// 构造器
		this.sc = sc;// 得到Socket
		try {
			din = new DataInputStream(sc.getInputStream());// 输入流
			dout = new DataOutputStream(sc.getOutputStream());// 输入出流
			// din=new BufferedReader(new
			// InputStreamReader(sc.getInputStream()));//输入流
			// dout=new PrintWriter(sc.getOutputStream());//输入出流
		} catch (Exception e) {// 捕获异常
			e.printStackTrace();// 打印异常
		}
	}

	public void run() {
		while (flag) {// 循环
			try {
				// System.out.println("Client msg = 222");
				String msg = din.readUTF();// 收消息 read-UTF
				System.out.println("Client msg = " + msg);
				if (msg.startsWith("<#LOGIN#>")) {// 登录动作
					msg = msg.substring(9);// 截取子串
					String[] str = msg.split("\\|");// 分割字符串
					String u_name = DBUtil.checkUser(str[0], str[1]);// 检查数据库中是否含有该用户
					if (u_name == null) {// 当u_name为空时，登录失败
						System.out.println("u_name == null");
						dout.writeUTF("<#LOGINERROR#>");// 通知手机客户端登录失败
					} else {// 登录成功
						System.out.println(u_name + " <#LOGINOK#>");
						dout.writeUTF("<#LOGINOK#>" + u_name);// 登录成功，将用户名返回
					}
					break;
				} else if (msg.startsWith("<#REGISTER#>")) {// 注册动作
					msg = msg.substring(12);// 截取子串
					String[] str = msg.split("\\|");// 分割字符串
					int uid = DBUtil.insertUser(str[0], str[2], str[1], str[3],
							str[4]);
					dout.writeUTF("<#REGISTEROK#>" + uid + "|" + str[0]);// 向客户端发送用户ID和用户名
					break;
				} else if (msg.startsWith("<#ClientDown#>")) {// 客户端下线
					try {

						String ipString = sc.getInetAddress().toString();
						int id = 0;
						String textString = msg.substring(14);// 截取子串
						String[] str = textString.split("\\|");// 分割字符串
						id = Integer.parseInt(str[0]);
						DBUtil.update_ip_touser(ipString, id, 0);

						din.close();// 关闭输入流
						dout.close();// 关闭输出流
						sc.close();// 关闭Socket
						flag = false;
					} catch (Exception e) {// 捕获异常
						e.printStackTrace();// 打印异常
					}
				} else if (msg.startsWith("<#chat#>")) {// 客户端下线
					try {
						String textString = null;
						int id_from = 0;
						int id_to = 0;
						textString = msg.substring(8);// 截取子串
						String[] str = textString.split("\\|");// 分割字符串
						id_from = Integer.parseInt(str[0]);
						id_to = Integer.parseInt(str[1]);

						if (DBUtil.getuser_status(id_to) == 1) {
							// String send_buff=add_protocol(textString);
							String send_buff = textString;
							System.out.println("ip msg = "
									+ DBUtil.getuser_ip(id_to));
							communicate.socket_send(DBUtil.getuser_ip(id_to),
									communicate.server_port, send_buff,
									communicate.function_chat);

						} else {
							// DBUtil.updateIntochat(id_from,id_to,textString,0);
							dout.writeUTF("<#chat_error#>");
						}
					} catch (Exception e) {// 捕获异常
						e.printStackTrace();// 打印异常
					}
				} else if (msg.startsWith("<#send_ip#>")) {// 客户端下线
					try {
						String ipString = sc.getInetAddress().toString();
						ipString = ipString.substring(1);
						int id = 0;
						String textString = msg.substring(11);// 截取子串
						// String[] str = textString.split("\\|");//分割字符串
						// id=Integer.parseInt(str[0]);
						id = Integer.parseInt(textString);
						DBUtil.update_ip_touser(ipString, id, 1);
						// else {
						// // DBUtil.updateIntochat(id_from,id_to,textString,0);
						// dout.writeUTF("<#send_ip_error#>");
						// }
					} catch (Exception e) {// 捕获异常
						e.printStackTrace();// 打印异常
					}
				} else if (msg.startsWith("<#get_max#>")) {
					String return_back = null;
					msg = msg.substring(11);// 截取子串
					String[] str = msg.split("\\|");
					if (str[0].equalsIgnoreCase("jrla_explain_list"))
						return_back = DBUtil.get_jrla_explain_list_max();
					if (str[0].equalsIgnoreCase("jrla_activity_list"))
						return_back = DBUtil.get_jrla_activity_list_max();
					if (str[0].equalsIgnoreCase("jrla_food_list"))
						return_back = DBUtil.get_jrla_food_list_max();
					if (str[0].equalsIgnoreCase("jrla_gift_list"))
						return_back = DBUtil.get_jrla_gift_list_max();
					dout.writeUTF("<#get_max#>" + return_back);
					System.out.println("<#get_max#>" + return_back);
				} else if (msg.startsWith("<#get_data#>")) {
					String return_back = "";
					msg = msg.substring(12);// 截取子串
					String[] str = msg.split("\\|");

					if (str[0].equalsIgnoreCase("jrla_explain_list"))
						return_back = DBUtil.get_jrla_explain_list_data(str[1],
								str[2],str[3]);
					if (str[0].equalsIgnoreCase("jrla_activity_list"))
						return_back = DBUtil.get_jrla_activity_list_data(
								str[1], str[2],str[3]);
					if (str[0].equalsIgnoreCase("jrla_food_list"))
						return_back = DBUtil.get_jrla_food_list_data(str[1],
								str[2],str[3]);
					if (str[0].equalsIgnoreCase("jrla_gift_list"))
						return_back = DBUtil.get_jrla_gift_list_data(str[1],
								str[2],str[3]);
					if (str[0].equalsIgnoreCase("jrla_user"))
						return_back = DBUtil.get_jrla_user_data(str[1]);
					dout.writeUTF("<#get_data#>" + return_back);
					System.out.println("<#get_data#>" + return_back);
				} else if (msg.startsWith("<#detail_data#>")) {
					 String return_back="";
					 msg=msg.substring(15);//截取子串
					 String[] str = msg.split("\\|");
					
					if (str[0].equalsIgnoreCase("jrla_activity_list"))
							return_back = DBUtil.get_jrla_activity_list_detail(str[1]);
					if (str[0].equalsIgnoreCase("jrla_food_list"))
							return_back = DBUtil.get_jrla_food_list_detail(str[1]);
					if (str[0].equalsIgnoreCase("jrla_gift_list"))
							return_back = DBUtil.get_jrla_gift_list_detail(str[1]);
					 dout.writeUTF("<#detail_data#>"+return_back);
					 System.out.println("<#detail_data#>"+return_back);
				}else if (msg.startsWith("<#add_col#>")) {
					 String return_back="";
					 msg=msg.substring(11);//截取子串
					 String[] str = msg.split("\\|");
					
					if (str[0].equalsIgnoreCase("jrla_activity_list"))
							return_back = DBUtil.add_activity_col(msg);
					if (str[0].equalsIgnoreCase("jrla_food_list"))
						return_back = DBUtil.add_food_col(msg);
					if (str[0].equalsIgnoreCase("jrla_gift_list"))
						return_back = DBUtil.add_gift_col(msg);
				} else if (msg.startsWith("<#del_col#>")) {
					 String return_back="";
					 msg=msg.substring(11);//截取子串
					 String[] str = msg.split("\\|");
					
					 if (str[0].equalsIgnoreCase("jrla_activity_list"))
						DBUtil.del_activity_col(msg);
					if (str[0].equalsIgnoreCase("jrla_food_list"))
						DBUtil.del_food_col(msg);
					if (str[0].equalsIgnoreCase("jrla_gift_list"))
						DBUtil.del_gift_col(msg);
				} else if (msg.startsWith("<#update_data#>")) {
					 String return_back="";
					 msg=msg.substring(15);//截取子串
					 String[] str = msg.split("\\|");
					
					if (str[0].equalsIgnoreCase("jrla_user"))
							return_back = DBUtil.update_jrla_user(msg);
//					 dout.writeUTF("<#detail_data#>"+return_back);
//					 System.out.println("<#detail_data#>"+return_back);
				} 
				else if (msg.startsWith("<#get_aid_max#>")) {
					String return_back = null;
					msg = msg.substring(15);// 截取子串
						return_back = DBUtil.get_aid_max();
					dout.writeUTF("<#get_aid_max#>" + return_back);
					System.out.println("<#get_aid_max#>" + return_back);
				} 
				else if (msg.startsWith("<#get_fid_max#>")) {
					String return_back = null;
					msg = msg.substring(15);// 截取子串
						return_back = DBUtil.get_fid_max();
					dout.writeUTF("<#get_fid_max#>" + return_back);
					System.out.println("<#get_fid_max#>" + return_back);
				} 
				else if (msg.startsWith("<#get_gid_max#>")) {
					String return_back = null;
					msg = msg.substring(15);// 截取子串
						return_back = DBUtil.get_gid_max();
					dout.writeUTF("<#get_gid_max#>" + return_back);
					System.out.println("<#get_gid_max#>" + return_back);
				} 
				else if (msg.startsWith("<#get_xid_data#>")) {
					String return_back = null;
					msg = msg.substring(16);// 截取子串
					String[] str = msg.split("\\|");
					return_back = DBUtil.get_xid_data(str[0],str[1],str[2],str[3]);
					dout.writeUTF("<#get_xid_data#>" + return_back);
					System.out.println("<#get_xid_data#>" + return_back);
				}
				else if (msg.startsWith("<#get_max_col#>")) {
					String return_back = null;
					msg = msg.substring(15);// 截取子串
					String[] str = msg.split("\\|");
					if (str[0].equalsIgnoreCase("jrla_activity_list"))
						return_back = DBUtil.get_jrla_activity_col_max(str[1]);
//					if (str[0].equalsIgnoreCase("jrla_activity_list"))
//						return_back = DBUtil.get_jrla_activity_list_max();
//					if (str[0].equalsIgnoreCase("jrla_food_list"))
//						return_back = DBUtil.get_jrla_food_list_max();
//					if (str[0].equalsIgnoreCase("jrla_gift_list"))
//						return_back = DBUtil.get_jrla_gift_list_max();
					dout.writeUTF("<#get_max#>" + return_back);
					System.out.println("<#get_max#>" + return_back);
				} else if (msg.startsWith("<#get_data_col#>")) {
					String return_back = "";
					msg = msg.substring(16);// 截取子串
					String[] str = msg.split("\\|");
					if (str[0].equalsIgnoreCase("jrla_activity_list"))
						return_back = DBUtil.get_jrla_activity_list_data_col(
								str[1], str[2],str[3]);
//					if (str[0].equalsIgnoreCase("jrla_food_list"))
//						return_back = DBUtil.get_jrla_food_list_data(str[1],
//								str[2],str[3]);
//					if (str[0].equalsIgnoreCase("jrla_gift_list"))
//						return_back = DBUtil.get_jrla_gift_list_data(str[1],
//								str[2],str[3]);
					dout.writeUTF("<#get_data#>" + return_back);
					System.out.println("<#get_data#>" + return_back);
				} 
				else if (msg.startsWith("<#SEARCH#>")) {// 搜索美食信息动作
					msg = msg.substring(10);// 截取子串
					String[] str = msg.split("\\|");// 分割字符串infoValues、searchSort、startPrice、endPrice、span、currentPageNo

					int totleNumber = DBUtil.getjrlaInfoCountForPhone(str[0],
							Integer.parseInt(str[1]), str[2], str[3]);

					List<String[]> jrlaInfos = DBUtil.getjrlaInfoForPhone(
							str[0], Integer.parseInt(str[1]), str[2], str[3],
							Integer.parseInt(str[4]), Integer.parseInt(str[5]));
					dout.writeUTF("<#SEARCHINFO#>" + jrlaInfos.size() + "|"
							+ totleNumber);// 向客户端发送搜索的数据
					for (String[] mi : jrlaInfos) {// 循环组织字符串发送到客户端
						dout.writeUTF(mi[0] + "|" + mi[1] + "|" + mi[2] + "|"
								+ mi[3] + "|" + mi[4] + "|" + mi[5] + "|"
								+ mi[6] + "|" + mi[7]);
					}
					int[] mids = new int[jrlaInfos.size()];// 创建存放MID数组
					for (int i = 0; i < mids.length; i++) {
						mids[i] = Integer
								.parseInt(((String[]) jrlaInfos.get(i))[7]);
					}

					ArrayList<Blob> blobs = DBUtil.getjrla_image(mids);
					for (Blob b : blobs) {
						int size = (int) b.length();
						byte[] bs = b.getBytes(1, (int) size);
						dout.writeInt(size);// 写入字节数组的长度
						dout.write(bs);// 将字节数组发送到客户端
						dout.flush();// 清空缓冲区,保证之前的数据发送出去
					}
				} else if (msg.startsWith("<#FAVOURITE#>")) {// 搜索我的收藏
					msg = msg.substring(13);// 截取子串
					ArrayList<jrlaInfo> jrlaInfos = DBUtil.getFavourite(msg);
					dout.writeUTF("<#FAVOURITEINFO#>" + jrlaInfos.size());// 向客户端发送搜索的数据
					for (jrlaInfo mi : jrlaInfos) {// 循环组织字符串发送到客户端
						dout.writeUTF(mi.getInfo_title() + "|"
								+ mi.getInfo_dis() + "|" + mi.getInfo_lon()
								+ "|" + mi.getInfo_lat() + "|"
								+ mi.getInfo_time() + "|" + mi.getUid() + "|"
								+ mi.getMid());
					}
					int[] mids = new int[jrlaInfos.size()];// 创建存放MID数组
					for (int i = 0; i < mids.length; i++) {
						mids[i] = Integer.parseInt(jrlaInfos.get(i).getMid());
					}

					ArrayList<Blob> blobs = DBUtil.getjrla_image(mids);// 得到需要的图片列表
					for (Blob b : blobs) {
						int size = (int) b.length();
						byte[] bs = b.getBytes(1, (int) size);
						dout.writeInt(size);// 写入字节数组的长度
						dout.write(bs);// 将字节数组发送到客户端
						dout.flush();// 清空缓冲区,保证之前的数据发送出去
					}
				} else if (msg.startsWith("<#RECOMMEND#>")) {// 搜索推荐
					ArrayList<jrlaInfo> jrlaInfos = DBUtil.getjrla_recommend();
					dout.writeUTF("<#RECOMMENDINFO#>" + jrlaInfos.size());// 向客户端发送搜索的数据
					for (jrlaInfo mi : jrlaInfos) {// 循环组织字符串发送到客户端
						dout.writeUTF(mi.getInfo_title() + "|"
								+ mi.getInfo_dis() + "|" + mi.getInfo_lon()
								+ "|" + mi.getInfo_lat() + "|"
								+ mi.getInfo_time() + "|" + mi.getUid() + "|"
								+ mi.getMid());
					}
					int[] mids = new int[jrlaInfos.size()];// 创建存放MID数组
					for (int i = 0; i < mids.length; i++) {
						mids[i] = Integer.parseInt(jrlaInfos.get(i).getMid());
					}

					ArrayList<Blob> blobs = DBUtil.getjrla_image(mids);
					for (Blob b : blobs) {
						int size = (int) b.length();// 得到图片的长度
						byte[] bs = b.getBytes(1, (int) size);// 将Blob转换成成字节数组
						dout.writeInt(size);// 写入字节数组的长度
						dout.write(bs);// 将字节数组发送到客户端
						dout.flush();// 清空缓冲区,保证之前的数据发送出去
					}
				} else if (msg.startsWith("<#DELETEjrlaCOL#>")) {// 删除收藏
					msg = msg.substring(17);// 截取子串
					String[] str = msg.split("\\|");// 分割字符串
					DBUtil.deletejrlaCol(str[0], str[1]);// 删除数据库中的该收藏
				} else if (msg.startsWith("<#INSERTjrlaCOL#>")) {// 收藏动作
					msg = msg.substring(17);// 截取子串
					String[] str = msg.split("\\|");// 分割字符串
					DBUtil.insertjrlaCol(str[0], str[1], "");// 第三个参数为评论，此处并没有实现
				} else if (msg.startsWith("<#INSERTjrlaINFO#>")) {
					msg = msg.substring(18);// 截取子串
					String[] str = msg.split("\\|");// 分割字符串
					int mid = DBUtil.insertjrla_info(str[0], str[1], str[2],
							str[3], str[4], str[5], Integer.parseInt(str[6]),
							2, str[7]);

					int size = din.readInt();// 读取图片数组的长度
					byte[] image = new byte[size];// 创建图片数组
					din.read(image);// 读取图片数组

					DBUtil.insertjrla_image(image, mid);// 插入到数据库
				} else if (msg.startsWith("<#jrlaSORT#>")) {// 获取美食种类
					msg = msg.substring(12);// 截取子串
					List<String[]> sorts = DBUtil.getjrla_sort();
					dout.writeUTF("<#jrlaSORTINFO#>" + sorts.size() + "|" + msg);// 向客户端发送搜索的数据
					for (String[] sort : sorts) {// 循环组织字符串发送到客户端
						dout.writeUTF(sort[0] + "|" + sort[1]);
					}
				}
			} catch (EOFException e) {// 捕获异常
				System.out.println("Client Down");
				flag = false;
				try {
					if (din != null) {
						din.close();
						din = null;
					}
				} catch (Exception el) {
					el.printStackTrace();
				}
				try {
					if (dout != null) {
						dout.close();
						dout = null;
					}
				} catch (Exception el) {
					el.printStackTrace();
				}
				try {
					if (sc != null) {
						sc.close();
						sc = null;
					}
				} catch (Exception el) {
					el.printStackTrace();
				}
			} catch (Exception e) {// 捕获异常
				e.printStackTrace();// 打印异常
			}
		}
	}

	public void setFlag(boolean flag) {// 循环标志位的设置方法
		this.flag = flag;
	}
}
