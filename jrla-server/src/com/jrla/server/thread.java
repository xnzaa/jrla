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
	Socket sc;// ����Socket������
	main father;// ����ServerThread������
	DataInputStream din = null;// ������
	DataOutputStream dout = null;// �����
	// BufferedReader din;
	// PrintWriter dout;
	private boolean flag = true;// ѭ������

	public thread(Socket sc) {// ������
		this.sc = sc;// �õ�Socket
		try {
			din = new DataInputStream(sc.getInputStream());// ������
			dout = new DataOutputStream(sc.getOutputStream());// �������
			// din=new BufferedReader(new
			// InputStreamReader(sc.getInputStream()));//������
			// dout=new PrintWriter(sc.getOutputStream());//�������
		} catch (Exception e) {// �����쳣
			e.printStackTrace();// ��ӡ�쳣
		}
	}

	public void run() {
		while (flag) {// ѭ��
			try {
				// System.out.println("Client msg = 222");
				String msg = din.readUTF();// ����Ϣ read-UTF
				System.out.println("Client msg = " + msg);
				if (msg.startsWith("<#LOGIN#>")) {// ��¼����
					msg = msg.substring(9);// ��ȡ�Ӵ�
					String[] str = msg.split("\\|");// �ָ��ַ���
					String u_name = DBUtil.checkUser(str[0], str[1]);// ������ݿ����Ƿ��и��û�
					if (u_name == null) {// ��u_nameΪ��ʱ����¼ʧ��
						System.out.println("u_name == null");
						dout.writeUTF("<#LOGINERROR#>");// ֪ͨ�ֻ��ͻ��˵�¼ʧ��
					} else {// ��¼�ɹ�
						System.out.println(u_name + " <#LOGINOK#>");
						dout.writeUTF("<#LOGINOK#>" + u_name);// ��¼�ɹ������û�������
					}
					break;
				} else if (msg.startsWith("<#REGISTER#>")) {// ע�ᶯ��
					msg = msg.substring(12);// ��ȡ�Ӵ�
					String[] str = msg.split("\\|");// �ָ��ַ���
					int uid = DBUtil.insertUser(str[0], str[2], str[1], str[3],
							str[4]);
					dout.writeUTF("<#REGISTEROK#>" + uid + "|" + str[0]);// ��ͻ��˷����û�ID���û���
					break;
				} else if (msg.startsWith("<#ClientDown#>")) {// �ͻ�������
					try {

						String ipString = sc.getInetAddress().toString();
						int id = 0;
						String textString = msg.substring(14);// ��ȡ�Ӵ�
						String[] str = textString.split("\\|");// �ָ��ַ���
						id = Integer.parseInt(str[0]);
						DBUtil.update_ip_touser(ipString, id, 0);

						din.close();// �ر�������
						dout.close();// �ر������
						sc.close();// �ر�Socket
						flag = false;
					} catch (Exception e) {// �����쳣
						e.printStackTrace();// ��ӡ�쳣
					}
				} else if (msg.startsWith("<#chat#>")) {// �ͻ�������
					try {
						String textString = null;
						int id_from = 0;
						int id_to = 0;
						textString = msg.substring(8);// ��ȡ�Ӵ�
						String[] str = textString.split("\\|");// �ָ��ַ���
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
					} catch (Exception e) {// �����쳣
						e.printStackTrace();// ��ӡ�쳣
					}
				} else if (msg.startsWith("<#send_ip#>")) {// �ͻ�������
					try {
						String ipString = sc.getInetAddress().toString();
						ipString = ipString.substring(1);
						int id = 0;
						String textString = msg.substring(11);// ��ȡ�Ӵ�
						// String[] str = textString.split("\\|");//�ָ��ַ���
						// id=Integer.parseInt(str[0]);
						id = Integer.parseInt(textString);
						DBUtil.update_ip_touser(ipString, id, 1);
						// else {
						// // DBUtil.updateIntochat(id_from,id_to,textString,0);
						// dout.writeUTF("<#send_ip_error#>");
						// }
					} catch (Exception e) {// �����쳣
						e.printStackTrace();// ��ӡ�쳣
					}
				} else if (msg.startsWith("<#get_max#>")) {
					String return_back = null;
					msg = msg.substring(11);// ��ȡ�Ӵ�
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
					msg = msg.substring(12);// ��ȡ�Ӵ�
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
					 msg=msg.substring(15);//��ȡ�Ӵ�
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
					 msg=msg.substring(11);//��ȡ�Ӵ�
					 String[] str = msg.split("\\|");
					
					if (str[0].equalsIgnoreCase("jrla_activity_list"))
							return_back = DBUtil.add_activity_col(msg);
					if (str[0].equalsIgnoreCase("jrla_food_list"))
						return_back = DBUtil.add_food_col(msg);
					if (str[0].equalsIgnoreCase("jrla_gift_list"))
						return_back = DBUtil.add_gift_col(msg);
				} else if (msg.startsWith("<#del_col#>")) {
					 String return_back="";
					 msg=msg.substring(11);//��ȡ�Ӵ�
					 String[] str = msg.split("\\|");
					
					 if (str[0].equalsIgnoreCase("jrla_activity_list"))
						DBUtil.del_activity_col(msg);
					if (str[0].equalsIgnoreCase("jrla_food_list"))
						DBUtil.del_food_col(msg);
					if (str[0].equalsIgnoreCase("jrla_gift_list"))
						DBUtil.del_gift_col(msg);
				} else if (msg.startsWith("<#update_data#>")) {
					 String return_back="";
					 msg=msg.substring(15);//��ȡ�Ӵ�
					 String[] str = msg.split("\\|");
					
					if (str[0].equalsIgnoreCase("jrla_user"))
							return_back = DBUtil.update_jrla_user(msg);
//					 dout.writeUTF("<#detail_data#>"+return_back);
//					 System.out.println("<#detail_data#>"+return_back);
				} 
				else if (msg.startsWith("<#get_aid_max#>")) {
					String return_back = null;
					msg = msg.substring(15);// ��ȡ�Ӵ�
						return_back = DBUtil.get_aid_max();
					dout.writeUTF("<#get_aid_max#>" + return_back);
					System.out.println("<#get_aid_max#>" + return_back);
				} 
				else if (msg.startsWith("<#get_fid_max#>")) {
					String return_back = null;
					msg = msg.substring(15);// ��ȡ�Ӵ�
						return_back = DBUtil.get_fid_max();
					dout.writeUTF("<#get_fid_max#>" + return_back);
					System.out.println("<#get_fid_max#>" + return_back);
				} 
				else if (msg.startsWith("<#get_gid_max#>")) {
					String return_back = null;
					msg = msg.substring(15);// ��ȡ�Ӵ�
						return_back = DBUtil.get_gid_max();
					dout.writeUTF("<#get_gid_max#>" + return_back);
					System.out.println("<#get_gid_max#>" + return_back);
				} 
				else if (msg.startsWith("<#get_xid_data#>")) {
					String return_back = null;
					msg = msg.substring(16);// ��ȡ�Ӵ�
					String[] str = msg.split("\\|");
					return_back = DBUtil.get_xid_data(str[0],str[1],str[2],str[3]);
					dout.writeUTF("<#get_xid_data#>" + return_back);
					System.out.println("<#get_xid_data#>" + return_back);
				}
				else if (msg.startsWith("<#get_max_col#>")) {
					String return_back = null;
					msg = msg.substring(15);// ��ȡ�Ӵ�
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
					msg = msg.substring(16);// ��ȡ�Ӵ�
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
				else if (msg.startsWith("<#SEARCH#>")) {// ������ʳ��Ϣ����
					msg = msg.substring(10);// ��ȡ�Ӵ�
					String[] str = msg.split("\\|");// �ָ��ַ���infoValues��searchSort��startPrice��endPrice��span��currentPageNo

					int totleNumber = DBUtil.getjrlaInfoCountForPhone(str[0],
							Integer.parseInt(str[1]), str[2], str[3]);

					List<String[]> jrlaInfos = DBUtil.getjrlaInfoForPhone(
							str[0], Integer.parseInt(str[1]), str[2], str[3],
							Integer.parseInt(str[4]), Integer.parseInt(str[5]));
					dout.writeUTF("<#SEARCHINFO#>" + jrlaInfos.size() + "|"
							+ totleNumber);// ��ͻ��˷�������������
					for (String[] mi : jrlaInfos) {// ѭ����֯�ַ������͵��ͻ���
						dout.writeUTF(mi[0] + "|" + mi[1] + "|" + mi[2] + "|"
								+ mi[3] + "|" + mi[4] + "|" + mi[5] + "|"
								+ mi[6] + "|" + mi[7]);
					}
					int[] mids = new int[jrlaInfos.size()];// �������MID����
					for (int i = 0; i < mids.length; i++) {
						mids[i] = Integer
								.parseInt(((String[]) jrlaInfos.get(i))[7]);
					}

					ArrayList<Blob> blobs = DBUtil.getjrla_image(mids);
					for (Blob b : blobs) {
						int size = (int) b.length();
						byte[] bs = b.getBytes(1, (int) size);
						dout.writeInt(size);// д���ֽ�����ĳ���
						dout.write(bs);// ���ֽ����鷢�͵��ͻ���
						dout.flush();// ��ջ�����,��֤֮ǰ�����ݷ��ͳ�ȥ
					}
				} else if (msg.startsWith("<#FAVOURITE#>")) {// �����ҵ��ղ�
					msg = msg.substring(13);// ��ȡ�Ӵ�
					ArrayList<jrlaInfo> jrlaInfos = DBUtil.getFavourite(msg);
					dout.writeUTF("<#FAVOURITEINFO#>" + jrlaInfos.size());// ��ͻ��˷�������������
					for (jrlaInfo mi : jrlaInfos) {// ѭ����֯�ַ������͵��ͻ���
						dout.writeUTF(mi.getInfo_title() + "|"
								+ mi.getInfo_dis() + "|" + mi.getInfo_lon()
								+ "|" + mi.getInfo_lat() + "|"
								+ mi.getInfo_time() + "|" + mi.getUid() + "|"
								+ mi.getMid());
					}
					int[] mids = new int[jrlaInfos.size()];// �������MID����
					for (int i = 0; i < mids.length; i++) {
						mids[i] = Integer.parseInt(jrlaInfos.get(i).getMid());
					}

					ArrayList<Blob> blobs = DBUtil.getjrla_image(mids);// �õ���Ҫ��ͼƬ�б�
					for (Blob b : blobs) {
						int size = (int) b.length();
						byte[] bs = b.getBytes(1, (int) size);
						dout.writeInt(size);// д���ֽ�����ĳ���
						dout.write(bs);// ���ֽ����鷢�͵��ͻ���
						dout.flush();// ��ջ�����,��֤֮ǰ�����ݷ��ͳ�ȥ
					}
				} else if (msg.startsWith("<#RECOMMEND#>")) {// �����Ƽ�
					ArrayList<jrlaInfo> jrlaInfos = DBUtil.getjrla_recommend();
					dout.writeUTF("<#RECOMMENDINFO#>" + jrlaInfos.size());// ��ͻ��˷�������������
					for (jrlaInfo mi : jrlaInfos) {// ѭ����֯�ַ������͵��ͻ���
						dout.writeUTF(mi.getInfo_title() + "|"
								+ mi.getInfo_dis() + "|" + mi.getInfo_lon()
								+ "|" + mi.getInfo_lat() + "|"
								+ mi.getInfo_time() + "|" + mi.getUid() + "|"
								+ mi.getMid());
					}
					int[] mids = new int[jrlaInfos.size()];// �������MID����
					for (int i = 0; i < mids.length; i++) {
						mids[i] = Integer.parseInt(jrlaInfos.get(i).getMid());
					}

					ArrayList<Blob> blobs = DBUtil.getjrla_image(mids);
					for (Blob b : blobs) {
						int size = (int) b.length();// �õ�ͼƬ�ĳ���
						byte[] bs = b.getBytes(1, (int) size);// ��Blobת���ɳ��ֽ�����
						dout.writeInt(size);// д���ֽ�����ĳ���
						dout.write(bs);// ���ֽ����鷢�͵��ͻ���
						dout.flush();// ��ջ�����,��֤֮ǰ�����ݷ��ͳ�ȥ
					}
				} else if (msg.startsWith("<#DELETEjrlaCOL#>")) {// ɾ���ղ�
					msg = msg.substring(17);// ��ȡ�Ӵ�
					String[] str = msg.split("\\|");// �ָ��ַ���
					DBUtil.deletejrlaCol(str[0], str[1]);// ɾ�����ݿ��еĸ��ղ�
				} else if (msg.startsWith("<#INSERTjrlaCOL#>")) {// �ղض���
					msg = msg.substring(17);// ��ȡ�Ӵ�
					String[] str = msg.split("\\|");// �ָ��ַ���
					DBUtil.insertjrlaCol(str[0], str[1], "");// ����������Ϊ���ۣ��˴���û��ʵ��
				} else if (msg.startsWith("<#INSERTjrlaINFO#>")) {
					msg = msg.substring(18);// ��ȡ�Ӵ�
					String[] str = msg.split("\\|");// �ָ��ַ���
					int mid = DBUtil.insertjrla_info(str[0], str[1], str[2],
							str[3], str[4], str[5], Integer.parseInt(str[6]),
							2, str[7]);

					int size = din.readInt();// ��ȡͼƬ����ĳ���
					byte[] image = new byte[size];// ����ͼƬ����
					din.read(image);// ��ȡͼƬ����

					DBUtil.insertjrla_image(image, mid);// ���뵽���ݿ�
				} else if (msg.startsWith("<#jrlaSORT#>")) {// ��ȡ��ʳ����
					msg = msg.substring(12);// ��ȡ�Ӵ�
					List<String[]> sorts = DBUtil.getjrla_sort();
					dout.writeUTF("<#jrlaSORTINFO#>" + sorts.size() + "|" + msg);// ��ͻ��˷�������������
					for (String[] sort : sorts) {// ѭ����֯�ַ������͵��ͻ���
						dout.writeUTF(sort[0] + "|" + sort[1]);
					}
				}
			} catch (EOFException e) {// �����쳣
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
			} catch (Exception e) {// �����쳣
				e.printStackTrace();// ��ӡ�쳣
			}
		}
	}

	public void setFlag(boolean flag) {// ѭ����־λ�����÷���
		this.flag = flag;
	}
}
