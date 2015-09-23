package com.jrla.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.R.integer;
import android.R.string;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.util.Log;

public class communicate {

	static Socket s = null;// 声明Socket的引用

	static DataOutputStream dout = null;// 输出流
	static DataInputStream din = null;// 输入流

	static String server_ip = "192.168.191.1";
	static int server_port = 9999;

	static int function_chat = 1;
	static int function_receive_chat = 2;
	static int function_send_ip = 3;
	static int function_client_down = 4;
	static int function_get_jrla_explain_list_max = 5;
	static int function_get_user_info = 6;
	static int function_update_user_info = 7;
	static int function_add_col = 8;
	static int function_del_col = 9;
	
	
	static String table_jrla_explain_list = "jrla_explain_list";

	static String table_jrla_activity_list = "jrla_activity_list";

	static String table_jrla_food_list = "jrla_food_list";

	static String table_jrla_gift_list = "jrla_gift_list";

	// byte 1字节 16位
	// char 2 字节
	// short 2 字节
	// int 4 字节
	// float 4 字节
	// long 8 字节
	// double 8 字节

	private String socket_receive(String aim_ip, String aim_port, String message) {
		try {

			s = new Socket(aim_ip, Integer.parseInt(aim_port));
			din = new DataInputStream(s.getInputStream());
			String msg = din.readUTF();// 接收服务器发来的消息
			return msg;

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 连接服务器
		String receive_buff = null;
		return receive_buff;
	}

	private String add_protocol(String message) {
		StringBuffer sb = new StringBuffer(message);
		sb.insert(0, 0x8080);
		sb.insert(1, 0x0000);
		sb.insert(2, (char) message.length());
		sb.insert(3, (char) message.length());
		byte[] buffer = sb.toString().getBytes();
		char crc = crcTable(buffer);
		sb.insert(sb.length(), crc);
		String send_buff = sb.toString();
		return send_buff;
	}

	private String remove_protocol(String message) {
		String receive_buff = null;
		return receive_buff;
	}

	// string username = "chenxizhang";
	// byte[] buffer = System.Text.Encoding.UTF8.GetBytes(username);
	// //这是把字符串转成字节数组
	// Console.WriteLine(System.Text.Encoding.UTF8.GetString(buffer));
	// //这是把字节数组再转回到字符串

	public static char crcTable(byte[] bytes) {
		char[] table = { 0x0000, 0xC0C1, 0xC181, 0x0140, 0xC301, 0x03C0,
				0x0280, 0xC241, 0xC601, 0x06C0, 0x0780, 0xC741, 0x0500, 0xC5C1,
				0xC481, 0x0440, 0xCC01, 0x0CC0, 0x0D80, 0xCD41, 0x0F00, 0xCFC1,
				0xCE81, 0x0E40, 0x0A00, 0xCAC1, 0xCB81, 0x0B40, 0xC901, 0x09C0,
				0x0880, 0xC841, 0xD801, 0x18C0, 0x1980, 0xD941, 0x1B00, 0xDBC1,
				0xDA81, 0x1A40, 0x1E00, 0xDEC1, 0xDF81, 0x1F40, 0xDD01, 0x1DC0,
				0x1C80, 0xDC41, 0x1400, 0xD4C1, 0xD581, 0x1540, 0xD701, 0x17C0,
				0x1680, 0xD641, 0xD201, 0x12C0, 0x1380, 0xD341, 0x1100, 0xD1C1,
				0xD081, 0x1040, 0xF001, 0x30C0, 0x3180, 0xF141, 0x3300, 0xF3C1,
				0xF281, 0x3240, 0x3600, 0xF6C1, 0xF781, 0x3740, 0xF501, 0x35C0,
				0x3480, 0xF441, 0x3C00, 0xFCC1, 0xFD81, 0x3D40, 0xFF01, 0x3FC0,
				0x3E80, 0xFE41, 0xFA01, 0x3AC0, 0x3B80, 0xFB41, 0x3900, 0xF9C1,
				0xF881, 0x3840, 0x2800, 0xE8C1, 0xE981, 0x2940, 0xEB01, 0x2BC0,
				0x2A80, 0xEA41, 0xEE01, 0x2EC0, 0x2F80, 0xEF41, 0x2D00, 0xEDC1,
				0xEC81, 0x2C40, 0xE401, 0x24C0, 0x2580, 0xE541, 0x2700, 0xE7C1,
				0xE681, 0x2640, 0x2200, 0xE2C1, 0xE381, 0x2340, 0xE101, 0x21C0,
				0x2080, 0xE041, 0xA001, 0x60C0, 0x6180, 0xA141, 0x6300, 0xA3C1,
				0xA281, 0x6240, 0x6600, 0xA6C1, 0xA781, 0x6740, 0xA501, 0x65C0,
				0x6480, 0xA441, 0x6C00, 0xACC1, 0xAD81, 0x6D40, 0xAF01, 0x6FC0,
				0x6E80, 0xAE41, 0xAA01, 0x6AC0, 0x6B80, 0xAB41, 0x6900, 0xA9C1,
				0xA881, 0x6840, 0x7800, 0xB8C1, 0xB981, 0x7940, 0xBB01, 0x7BC0,
				0x7A80, 0xBA41, 0xBE01, 0x7EC0, 0x7F80, 0xBF41, 0x7D00, 0xBDC1,
				0xBC81, 0x7C40, 0xB401, 0x74C0, 0x7580, 0xB541, 0x7700, 0xB7C1,
				0xB681, 0x7640, 0x7200, 0xB2C1, 0xB381, 0x7340, 0xB101, 0x71C0,
				0x7080, 0xB041, 0x5000, 0x90C1, 0x9181, 0x5140, 0x9301, 0x53C0,
				0x5280, 0x9241, 0x9601, 0x56C0, 0x5780, 0x9741, 0x5500, 0x95C1,
				0x9481, 0x5440, 0x9C01, 0x5CC0, 0x5D80, 0x9D41, 0x5F00, 0x9FC1,
				0x9E81, 0x5E40, 0x5A00, 0x9AC1, 0x9B81, 0x5B40, 0x9901, 0x59C0,
				0x5880, 0x9841, 0x8801, 0x48C0, 0x4980, 0x8941, 0x4B00, 0x8BC1,
				0x8A81, 0x4A40, 0x4E00, 0x8EC1, 0x8F81, 0x4F40, 0x8D01, 0x4DC0,
				0x4C80, 0x8C41, 0x4400, 0x84C1, 0x8581, 0x4540, 0x8701, 0x47C0,
				0x4680, 0x8641, 0x8201, 0x42C0, 0x4380, 0x8341, 0x4100, 0x81C1,
				0x8081, 0x4040, };

		char crc = 0x0000;

		for (byte b : bytes) {
			crc = (char) ((crc >>> 8) ^ table[(crc ^ b) & 0xff]);
		}
		return crc;
		// return Integer.toHexString(crc);
	}

	static void socket_send(final String aim_ip, final int aim_port,
			final String message, final int function) {
		new Thread() {// 将耗时的动作放到线程中
			public void run() {// 重写的run方法
				try {
					s = new Socket(aim_ip, aim_port);
					dout = new DataOutputStream(s.getOutputStream());
					if (function == function_chat)
						dout.writeUTF("<#chat#>" + message);
					// if(function == function_chat)
					// dout.writeUTF("<#chat_receive#>"+message);
					if (function == function_send_ip)
						dout.writeUTF("<#send_ip#>" + message);
					if (function == function_client_down)
						dout.writeUTF("<#ClientDown#>" + message);// 通知服务器客户端退出

					if (function == function_get_jrla_explain_list_max)
						dout.writeUTF("<#ClientDown#>" + message);// 通知服务器客户端退出
					if (function == function_update_user_info)
						dout.writeUTF("<#update_data#>" +"jrla_user"+"|"+message);//更新数据
					if (function == function_add_col)
						dout.writeUTF("<#add_col#>"+message);// 添加收藏
					if (function == function_del_col)
						dout.writeUTF("<#del_col#>"+message);// 删除收藏

				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 连接服务器
				finally {// 使用finally保证之后的语句一定被执行
					try {
						if (din != null) {
							din.close();
							din = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (dout != null) {
							dout.close();
							dout = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (s != null) {
							s.close();
							s = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	static int return_back = 0;
	static Boolean received = false;

	static int get_max_table(final String table, final String string) {
		received = false;
		new Thread() {// 将耗时的动作放到线程中
			public void run() {// 重写的run方法
				try {
					s = new Socket(server_ip, server_port);
					dout = new DataOutputStream(s.getOutputStream());
					din = new DataInputStream(s.getInputStream());
					if (table == table_jrla_explain_list) {
						dout.writeUTF("<#get_max#>jrla_explain_list" + string);// 通知服务器客户端退出

						String msg = din.readUTF();// 接收服务器发来的消息
						if (msg.startsWith("<#get_max#>")) {//
							msg = msg.substring(11);
							String str[] = msg.split("\\|");
							Log.e("xbb", msg);
							return_back = Integer.parseInt(str[0]);
							Log.e("xbb", return_back + "");
						} else if (msg.startsWith("<#get_max_error#>")) {//

							return_back = -1;
						}
					} else if (table == table_jrla_activity_list) {
						dout.writeUTF("<#get_max#>jrla_activity_list" + string);// 通知服务器客户端退出

						String msg = din.readUTF();// 接收服务器发来的消息
						if (msg.startsWith("<#get_max#>")) {//
							msg = msg.substring(11);
							String str[] = msg.split("\\|");
							Log.e("xbb", msg);
							return_back = Integer.parseInt(str[0]);
							Log.e("xbb", return_back + "");
						} else if (msg.startsWith("<#get_max_error#>")) {//

							return_back = -1;
						}
					}
					else if (table == table_jrla_food_list) {
						dout.writeUTF("<#get_max#>jrla_food_list" + string);// 通知服务器客户端退出

						String msg = din.readUTF();// 接收服务器发来的消息
						if (msg.startsWith("<#get_max#>")) {//
							msg = msg.substring(11);
							String str[] = msg.split("\\|");
							Log.e("xbb", msg);
							return_back = Integer.parseInt(str[0]);
							Log.e("xbb", return_back + "");
						} else if (msg.startsWith("<#get_max_error#>")) {//

							return_back = -1;
						}
					}
					else if (table == table_jrla_gift_list) {
						dout.writeUTF("<#get_max#>jrla_gift_list" + string);// 通知服务器客户端退出

						String msg = din.readUTF();// 接收服务器发来的消息
						if (msg.startsWith("<#get_max#>")) {//
							msg = msg.substring(11);
							String str[] = msg.split("\\|");
							Log.e("xbb", msg);
							return_back = Integer.parseInt(str[0]);
							Log.e("xbb", return_back + "");
						} else if (msg.startsWith("<#get_max_error#>")) {//

							return_back = -1;
						}
					}
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 连接服务器
				finally {// 使用finally保证之后的语句一定被执行
					try {
						if (din != null) {
							din.close();
							din = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (dout != null) {
							dout.close();
							dout = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (s != null) {
							s.close();
							s = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					received = true;
				}
			}
		}.start();
		while (!received)
			;
		return return_back;
	}

	static String get_data_table_return = null;

	public static String get_data_table(final String table,
			final int data_start, final int data_end, final String id_col) {
		// TODO Auto-generated method stub
		received = false;
		new Thread() {// 将耗时的动作放到线程中
			public void run() {// 重写的run方法
				try {
					s = new Socket(server_ip, server_port);
					dout = new DataOutputStream(s.getOutputStream());
					din = new DataInputStream(s.getInputStream());
					if (table == table_jrla_explain_list) {
						dout.writeUTF("<#get_data#>jrla_explain_list" + "|"
								+ data_start + "|" + data_end+"|"+id_col);// 通知服务器客户端退出
						String msg = din.readUTF();// 接收服务器发来的消息
						if (msg.startsWith("<#get_data#>")) {// 得到数据
							msg = msg.substring(12);

							get_data_table_return = msg;
						} else if (msg.startsWith("<#get_data#>")) {// 空数据数据

							get_data_table_return = null;
						}
					} else if (table == table_jrla_activity_list) {
						dout.writeUTF("<#get_data#>jrla_activity_list" + "|"
								+ data_start + "|" + data_end+"|"+id_col);// 通知服务器客户端退出
						String msg = din.readUTF();// 接收服务器发来的消息
						if (msg.startsWith("<#get_data#>")) {// 得到数据
							msg = msg.substring(12);

							get_data_table_return = msg;
						} else if (msg.startsWith("<#get_data#>")) {// 空数据数据

							get_data_table_return = null;
						}
					} else if (table == table_jrla_food_list) {
						dout.writeUTF("<#get_data#>jrla_food_list" + "|"
								+ data_start + "|" + data_end+"|"+id_col);// 通知服务器客户端退出
						String msg = din.readUTF();// 接收服务器发来的消息
						if (msg.startsWith("<#get_data#>")) {// 得到数据
							msg = msg.substring(12);

							get_data_table_return = msg;
						} else if (msg.startsWith("<#get_data#>")) {// 空数据数据

							get_data_table_return = null;
						}
					} else if (table == table_jrla_gift_list) {
						dout.writeUTF("<#get_data#>jrla_gift_list" + "|"
								+ data_start + "|" + data_end+"|"+id_col);// 通知服务器客户端退出
						String msg = din.readUTF();// 接收服务器发来的消息
						if (msg.startsWith("<#get_data#>")) {// 得到数据
							msg = msg.substring(12);

							get_data_table_return = msg;
						} else if (msg.startsWith("<#get_data#>")) {// 空数据数据

							get_data_table_return = null;
						}
					}
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 连接服务器
				finally {// 使用finally保证之后的语句一定被执行
					try {
						if (din != null) {
							din.close();
							din = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (dout != null) {
							dout.close();
							dout = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (s != null) {
							s.close();
							s = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					received = true;
				}
			}
		}.start();
		while (!received)
			;
		if (get_data_table_return == null)
			return "null";
		else
			return get_data_table_return;
	}

	static String get_data_detail_return = null;

	public static String get_data_detail(final String table,
			final String id) {
		// TODO Auto-generated method stub
		received = false;
		new Thread() {// 将耗时的动作放到线程中
			public void run() {// 重写的run方法
				try {
					s = new Socket(server_ip, server_port);
					dout = new DataOutputStream(s.getOutputStream());
					din = new DataInputStream(s.getInputStream());
					if (table.equalsIgnoreCase(table_jrla_activity_list))
						dout.writeUTF("<#detail_data#>jrla_activity_list" + "|"+id);// 通知服务器客户端退出
				
					else if(table.equalsIgnoreCase(table_jrla_food_list))
						dout.writeUTF("<#detail_data#>jrla_food_list" + "|"+id);
					else if(table.equalsIgnoreCase(table_jrla_gift_list))
						dout.writeUTF("<#detail_data#>jrla_gift_list" + "|"+id);
					String msg = din.readUTF();// 接收服务器发来的消息
					if (msg.startsWith("<#detail_data#>")) {// 得到数据
							msg = msg.substring(15);
							Log.e("xbx", msg);
							get_data_detail_return = msg;
					} else if (msg.startsWith("<#detail_data_error#>")) {// 空数据数据

							get_data_detail_return = null;
					}
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 连接服务器
				finally {// 使用finally保证之后的语句一定被执行
					try {
						if (din != null) {
							din.close();
							din = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (dout != null) {
							dout.close();
							dout = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (s != null) {
							s.close();
							s = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					received = true;
				}
			}
		}.start();
		while (!received)
			;
		if (get_data_detail_return == null)
			return "null";
		else
			return get_data_detail_return;
	}
	
	
	
	
	static int get_max_col(final String table, final String uid_col) {
		received = false;
		new Thread() {// 将耗时的动作放到线程中
			public void run() {// 重写的run方法
				try {
					s = new Socket(server_ip, server_port);
					dout = new DataOutputStream(s.getOutputStream());
					din = new DataInputStream(s.getInputStream());
					if (table == table_jrla_activity_list) {
						dout.writeUTF("<#get_max_col#>jrla_activity_list" +"|" +uid_col);// 通知服务器客户端退出

						String msg = din.readUTF();// 接收服务器发来的消息
						if (msg.startsWith("<#get_max_col#>")) {//
							msg = msg.substring(15);
							String str[] = msg.split("\\|");
							Log.e("xbb", msg);
							return_back = Integer.parseInt(str[0]);
							Log.e("xbb", return_back + "");
						} else if (msg.startsWith("<#get_max_col_error#>")) {//

							return_back = -1;
						}
					} 
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 连接服务器
				finally {// 使用finally保证之后的语句一定被执行
					try {
						if (din != null) {
							din.close();
							din = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (dout != null) {
							dout.close();
							dout = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (s != null) {
							s.close();
							s = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					received = true;
				}
			}
		}.start();
		while (!received)
			;
		return return_back;
	}

	public static String get_data_col(final String table,
			final int data_start, final int data_end, final String id_col) {
		// TODO Auto-generated method stub
		received = false;
		new Thread() {// 将耗时的动作放到线程中
			public void run() {// 重写的run方法
				try {
					s = new Socket(server_ip, server_port);
					dout = new DataOutputStream(s.getOutputStream());
					din = new DataInputStream(s.getInputStream());
					if (table == table_jrla_activity_list) {
						dout.writeUTF("<#get_data_col#>jrla_activity_list" + "|"
								+ data_start + "|" + data_end+"|"+id_col);// 通知服务器客户端退出
						String msg = din.readUTF();// 接收服务器发来的消息
						if (msg.startsWith("<#get_data_col#>")) {// 得到数据
							msg = msg.substring(16);

							get_data_table_return = msg;
						} else if (msg.startsWith("<#get_data_col_error#>")) {// 空数据数据

							get_data_table_return = null;
						}
					} 
//					else if (table == table_jrla_food_list) {
//						dout.writeUTF("<#get_data#>jrla_food_list" + "|"
//								+ data_start + "|" + data_end+"|"+id_col);// 通知服务器客户端退出
//						String msg = din.readUTF();// 接收服务器发来的消息
//						if (msg.startsWith("<#get_data#>")) {// 得到数据
//							msg = msg.substring(12);
//
//							get_data_table_return = msg;
//						} else if (msg.startsWith("<#get_data#>")) {// 空数据数据
//
//							get_data_table_return = null;
//						}
//					} else if (table == table_jrla_gift_list) {
//						dout.writeUTF("<#get_data#>jrla_gift_list" + "|"
//								+ data_start + "|" + data_end+"|"+id_col);// 通知服务器客户端退出
//						String msg = din.readUTF();// 接收服务器发来的消息
//						if (msg.startsWith("<#get_data#>")) {// 得到数据
//							msg = msg.substring(12);
//
//							get_data_table_return = msg;
//						} else if (msg.startsWith("<#get_data#>")) {// 空数据数据
//
//							get_data_table_return = null;
//						}
//					}
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 连接服务器
				finally {// 使用finally保证之后的语句一定被执行
					try {
						if (din != null) {
							din.close();
							din = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (dout != null) {
							dout.close();
							dout = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (s != null) {
							s.close();
							s = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					received = true;
				}
			}
		}.start();
		while (!received)
			;
		if (get_data_table_return == null)
			return "null";
		else
			return get_data_table_return;
	}

	public static String get_fid_max() {
		received = false;
		new Thread() {// 将耗时的动作放到线程中
			public void run() {// 重写的run方法
				try {
					s = new Socket(server_ip, server_port);
					dout = new DataOutputStream(s.getOutputStream());
					din = new DataInputStream(s.getInputStream());
						dout.writeUTF("<#get_fid_max#>");// 通知服务器客户端退出

						String msg = din.readUTF();// 接收服务器发来的消息
						if (msg.startsWith("<#get_fid_max#>")) {//
							msg = msg.substring(15);
							String str[] = msg.split("\\|");
							Log.e("xbb", msg);
							return_back = Integer.parseInt(str[0]);
							Log.e("xbb", return_back + "");
						} else if (msg.startsWith("<#get_max_error#>")) {//

							return_back = -1;
						}
					}catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 连接服务器
				finally {// 使用finally保证之后的语句一定被执行
					try {
						if (din != null) {
							din.close();
							din = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (dout != null) {
							dout.close();
							dout = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (s != null) {
							s.close();
							s = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					received = true;
				}
			}
		}.start();
		while (!received)
			;
			return return_back+"";
	}
	
	
	public static String get_aid_max() {
		received = false;
		new Thread() {// 将耗时的动作放到线程中
			public void run() {// 重写的run方法
				try {
					s = new Socket(server_ip, server_port);
					dout = new DataOutputStream(s.getOutputStream());
					din = new DataInputStream(s.getInputStream());
						dout.writeUTF("<#get_aid_max#>");// 通知服务器客户端退出

						String msg = din.readUTF();// 接收服务器发来的消息
						if (msg.startsWith("<#get_aid_max#>")) {//
							msg = msg.substring(15);
							String str[] = msg.split("\\|");
							Log.e("xbb", msg);
							return_back = Integer.parseInt(str[0]);
							Log.e("xbb", return_back + "");
						} else if (msg.startsWith("<#get_max_error#>")) {//

							return_back = -1;
						}
					}catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 连接服务器
				finally {// 使用finally保证之后的语句一定被执行
					try {
						if (din != null) {
							din.close();
							din = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (dout != null) {
							dout.close();
							dout = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (s != null) {
							s.close();
							s = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					received = true;
				}
			}
		}.start();
		while (!received)
			;
			return return_back+"";
	}
	
	
	public static String get_gid_max() {
		received = false;
		new Thread() {// 将耗时的动作放到线程中
			public void run() {// 重写的run方法
				try {
					s = new Socket(server_ip, server_port);
					dout = new DataOutputStream(s.getOutputStream());
					din = new DataInputStream(s.getInputStream());
						dout.writeUTF("<#get_gid_max#>");// 通知服务器客户端退出

						String msg = din.readUTF();// 接收服务器发来的消息
						if (msg.startsWith("<#get_gid_max#>")) {//
							msg = msg.substring(15);
							String str[] = msg.split("\\|");
							Log.e("xbb", msg);
							return_back = Integer.parseInt(str[0]);
							Log.e("xbb", return_back + "");
						} else if (msg.startsWith("<#get_max_error#>")) {//

							return_back = -1;
						}
					}catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 连接服务器
				finally {// 使用finally保证之后的语句一定被执行
					try {
						if (din != null) {
							din.close();
							din = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (dout != null) {
							dout.close();
							dout = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (s != null) {
							s.close();
							s = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					received = true;
				}
			}
		}.start();
		while (!received)
			;
			return return_back+"";
	}
	
	static String str_xid_data=null;
	public static String get_xid_data(final String aid, final String fid, final String gid, final String id_col) {
		received = false;
		new Thread() {// 将耗时的动作放到线程中
			public void run() {// 重写的run方法
				try {
					s = new Socket(server_ip, server_port);
					dout = new DataOutputStream(s.getOutputStream());
					din = new DataInputStream(s.getInputStream());
						dout.writeUTF("<#get_xid_data#>"+aid+"|"+fid+"|"+gid+"|"+id_col);// 通知服务器客户端退出

						String msg = din.readUTF();// 接收服务器发来的消息
						if (msg.startsWith("<#get_xid_data#>")) {//
							msg = msg.substring(16);
							String str[] = msg.split("\\|");
							str_xid_data = msg;
							Log.e("xbb", return_back + "");
						} else if (msg.startsWith("<#get_xid_data_error#>")) {//

							str_xid_data = null;
						}
					}catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 连接服务器
				finally {// 使用finally保证之后的语句一定被执行
					try {
						if (din != null) {
							din.close();
							din = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (dout != null) {
							dout.close();
							dout = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (s != null) {
							s.close();
							s = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					received = true;
				}
			}
		}.start();
		while (!received)
			;
			return str_xid_data;
	}

}
