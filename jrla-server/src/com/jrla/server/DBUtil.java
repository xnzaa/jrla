package com.jrla.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBUtil {
	public static Connection getConnection() {// 得到数据库连接
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/jrla", "root", "");
			if (!conn.isClosed()) {
				System.out.println("Succeeded connecting to the Database!");
			} else {
				System.out.println("error connecting to the Database!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/* 数据源 */
		// try{
		// Context ctx = new InitialContext();
		// DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/mysql");
		// conn = ds.getConnection();
		// }
		// catch(Exception e){
		// e.printStackTrace();
		// }
		return conn;
	}

	public static int getjrlaInfoCount() {
		int result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			st = con.createStatement();
			rs = st.executeQuery("select count(mid) from jrla_info");
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static int getuser_status(int id) {
		int result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			st = con.createStatement();
			rs = st.executeQuery("select u_online from jrla_user where uid = "
					+ id);
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getuser_ip(int id) {
		int result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String ip_str = null;
		try {
			con = getConnection();
			st = con.createStatement();
			rs = st.executeQuery("select net_ip from jrla_user where uid = "
					+ id);
			if (rs.next()) {

				ip_str = new String(rs.getString("net_ip")
						.getBytes("ISO8859_1"), "GBK");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip_str;
	}

	public static int getuser_port(int id) {
		int result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			st = con.createStatement();
			rs = st.executeQuery("select net_port from jrla_user");
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void updateIntochat(Integer id_from, Integer id_to,
			String text, int status) {

		Connection con = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			// pstmt =
			// con.prepareStatement("select * from jrla_ads where gid=?");
			// pstmt.setInt(1, gid);
			// rs = pstmt.executeQuery();// 查询
			// if (!rs.next()) {
			// result = DBUtil.getMaxNumber("jrla_ads");
			// DBUtil.updateMaxNumber(5);// 将该字段值加1
			pstmt = con
					.prepareStatement("insert into jrla_chat(id_from,id_to,text,status) values(?,?,?,?)");
			pstmt.setInt(1, id_from);
			pstmt.setInt(2, id_to);
			pstmt.setString(3, text);
			pstmt.setInt(4, status);
			pstmt.executeUpdate();
			// }
		} catch (Exception e) {// 捕获异常
			e.printStackTrace();// 打印异常
		} finally {
			try {
				if (rs != null) {
					rs.close();// 关闭
					rs = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// return result;
	}

	public static void update_ip_touser(String ip, int id, int online) {

		Connection con = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			// pstmt =
			// con.prepareStatement("select * from jrla_ads where gid=?");
			// pstmt.setInt(1, gid);
			// rs = pstmt.executeQuery();// 查询
			// if (!rs.next()) {
			// result = DBUtil.getMaxNumber("jrla_ads");
			// DBUtil.updateMaxNumber(5);// 将该字段值加1
			pstmt = con
					.prepareStatement("update jrla_user set net_ip=?,u_online=? where uid=?");
			pstmt.setString(1, new String(ip.getBytes("GBK"), "ISO8859_1"));
			pstmt.setInt(2, online);
			pstmt.setInt(3, id);
			pstmt.executeUpdate();
			// }
		} catch (Exception e) {// 捕获异常
			e.printStackTrace();// 打印异常
		} finally {
			try {
				if (rs != null) {
					rs.close();// 关闭
					rs = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// return result;
	}

	public static ArrayList<Blob> getjrla_image(int[] mid) {// 得到美食图片
		ArrayList<Blob> result = new ArrayList<Blob>();
		for (int i = 0; i < mid.length; i++) {
			ArrayList<jrlaImage> temp = getjrlaImage(mid[i]);
			if (temp.size() > 0) {
				jrlaImage mi = temp.get(0);// 用于手机端显示，只取第一张图片即可
				Blob b = mi.getB();
				result.add(b);
			}
		}
		return result;
	}

	public static int getjrlaInfoCountForPhone(String infoValues,
			int searchSort, String startPrice, String endPrice) {// 返回满足搜索添加的总个数
		int result = 0;
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		String sql;
		if (searchSort == -1) {
			if (startPrice.trim().equals("")) {// 当起始价格为空时
				if (endPrice.trim().equals("")) {// 截止价格也为空时
					// LIMIT M,N M+1条记录开始（记录号从1开始） N要几条记录 例如要 5-8条记录 LIMIT 4,4
					sql = "select count(*) from "
							+ "jrla_info where info_title like '%" + infoValues
							+ "%';";
				} else {
					sql = "select count(*) from "
							+ "jrla_info where info_price<" + endPrice
							+ " and info_title like '%" + infoValues + "%';";
				}
			} else {// 起始价格不为空
				if (endPrice.trim().equals("")) {// 截止价格为空时
					// LIMIT M,N M+1条记录开始（记录号从1开始） N要几条记录 例如要 5-8条记录 LIMIT 4,4
					sql = "select count(*) from "
							+ "jrla_info where info_price>" + startPrice
							+ " and info_title like '%" + infoValues + "%';";
				} else {// 都不为空时
					sql = "select count(*) from "
							+ "jrla_info where info_price>" + startPrice
							+ " and info_price<" + endPrice
							+ " and info_title like '%" + infoValues + "%';";
				}
			}
		} else {
			if (startPrice.trim().equals("")) {// 当起始价格为空时
				if (endPrice.trim().equals("")) {// 截止价格也为空时
					// LIMIT M,N M+1条记录开始（记录号从1开始） N要几条记录 例如要 5-8条记录 LIMIT 4,4
					sql = "select count(*) from jrla_info "
							+ "where info_sort=" + searchSort
							+ " and info_title like '%" + infoValues + "%';";
				} else {// 起始为空，截止不为空
					sql = "select count(*) from jrla_info "
							+ "where info_sort=" + searchSort
							+ " and info_price>" + startPrice + " "
							+ "and info_title like '%" + infoValues + "%';";
				}
			} else {// 起始价格不为空
				if (endPrice.trim().equals("")) {// 截止价格为空时
					sql = "select count(*) from jrla_info where info_sort="
							+ searchSort + " and info_price>" + startPrice
							+ " " + "and info_title like '%" + infoValues
							+ "%';";
				} else {// 都不为空时
					sql = "select count(*) from jrla_info "
							+ "where info_price<" + endPrice
							+ " and info_sort=" + searchSort
							+ " and info_price>" + startPrice + " "
							+ "and info_title like '%" + infoValues + "%';";
				}
			}
		}
		try {
			con = getConnection();
			st = con.createStatement();

			// 执行检索
			rs = st.executeQuery(sql);

			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (st != null) {
					st.close();
					st = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static List<String[]> getjrlaInfoForPhone(String infoValues,
			int searchSort, String startPrice, String endPrice, int span,
			int currentPageNo) {
		List<String[]> result = new ArrayList<String[]>();
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		String sql;
		int start = span * (currentPageNo - 1);// 计算出起始记录编号
		if (searchSort == -1) {
			if (startPrice.trim().equals("")) {// 当起始价格为空时
				if (endPrice.trim().equals("")) {// 截止价格也为空时
					// LIMIT M,N M+1条记录开始（记录号从1开始） N要几条记录 例如要 5-8条记录 LIMIT 4,4
					sql = "select hotel_name,info_title,info_dis,info_lon,info_lat,info_time,uid,mid from "
							+ "jrla_info where info_title like '%"
							+ infoValues
							+ "%' limit " + start + "," + span + ";";
				} else {
					sql = "select hotel_name,info_title,info_dis,info_lon,info_lat,info_time,uid,mid from "
							+ "jrla_info where info_price<"
							+ endPrice
							+ " and info_title like '%"
							+ infoValues
							+ "%' limit " + start + "," + span + ";";
				}
			} else {// 起始价格不为空
				if (endPrice.trim().equals("")) {// 截止价格为空时
					// LIMIT M,N M+1条记录开始（记录号从1开始） N要几条记录 例如要 5-8条记录 LIMIT 4,4
					sql = "select hotel_name,info_title,info_dis,info_lon,info_lat,info_time,uid,mid from "
							+ "jrla_info where info_price>"
							+ startPrice
							+ " and info_title like '%"
							+ infoValues
							+ "%' limit " + start + "," + span + ";";
				} else {// 都不为空时
					sql = "select hotel_name,info_title,info_dis,info_lon,info_lat,info_time,uid,mid from "
							+ "jrla_info where info_price>"
							+ startPrice
							+ " and info_price<"
							+ endPrice
							+ " and info_title like '%"
							+ infoValues
							+ "%' limit " + start + "," + span + ";";
				}
			}
		} else {
			if (startPrice.trim().equals("")) {// 当起始价格为空时
				if (endPrice.trim().equals("")) {// 截止价格也为空时
					// LIMIT M,N M+1条记录开始（记录号从1开始） N要几条记录 例如要 5-8条记录 LIMIT 4,4
					sql = "select hotel_name,info_title,info_dis,info_lon,info_lat,info_time,uid,mid from jrla_info "
							+ "where info_sort="
							+ searchSort
							+ " and info_title like '%"
							+ infoValues
							+ "%' limit " + start + "," + span + ";";
				} else {// 起始为空，截止不为空
					// sql =
					// "select hotel_name,info_title,info_dis,info_lon,info_lat,info_time,uid,mid from "
					// +
					// "jrla_info where info_sort="+searchSort+" info_price<"+endPrice+" and "
					// +
					// "info_title like '%"+infoValues+"%' limit "+start+","+span+";";
					sql = "select hotel_name,info_title,info_dis,info_lon,info_lat,info_time,uid,mid from jrla_info "
							+ "where info_sort="
							+ searchSort
							+ " and info_price>"
							+ startPrice
							+ " "
							+ "and info_title like '%"
							+ infoValues
							+ "%' limit " + start + "," + span + ";";
				}
			} else {// 起始价格不为空
				if (endPrice.trim().equals("")) {// 截止价格为空时
					// LIMIT M,N M+1条记录开始（记录号从1开始） N要几条记录 例如要 5-8条记录 LIMIT 4,4
					// sql =
					// "select hotel_name,info_title,info_dis,info_lon,info_lat,info_time,uid,mid from "
					// +
					// "jrla_info where info_sort="+searchSort+" info_price>"+startPrice+" and info_title "
					// +
					// "like '%"+infoValues+"%' limit "+start+","+span+";";
					sql = "select hotel_name,info_title,info_dis,info_lon,info_lat,info_time,uid,mid from jrla_info "
							+ "where info_sort="
							+ searchSort
							+ " and info_price>"
							+ startPrice
							+ " "
							+ "and info_title like '%"
							+ infoValues
							+ "%' limit " + start + "," + span + ";";
				} else {// 都不为空时
					// sql =
					// "select hotel_name,info_title,info_dis,info_lon,info_lat,info_time,uid,mid from "
					// +
					// "jrla_info where info_sort="+searchSort+" info_price>"+startPrice+" and info_price<"+endPrice+" and info_title like '%"+infoValues+"%' limit "+start+","+span+";";
					sql = "select hotel_name,info_title,info_dis,info_lon,info_lat,info_time,uid,mid from jrla_info "
							+ "where info_price<"
							+ endPrice
							+ " and info_sort="
							+ searchSort
							+ " and info_price>"
							+ startPrice
							+ " "
							+ "and info_title like '%"
							+ infoValues
							+ "%' limit " + start + "," + span + ";";
				}
			}
		}
		try {
			con = getConnection();
			// 由于MySQL默认创建的是可滚动的结果集合，因此要在创建语句时指定结果集不可滚动，只读
			st = con.createStatement(ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_READ_ONLY);

			// 执行检索
			rs = st.executeQuery(sql);

			while (rs.next()) {
				String[] str = new String[8];
				str[0] = new String(rs.getString(1).getBytes("ISO8859_1"),
						"GBK");// hotel_name
				str[1] = new String(rs.getString(2).getBytes("ISO8859_1"),
						"GBK");// info_title
				str[2] = new String(rs.getString(3).getBytes("ISO8859_1"),
						"GBK");// info_dis
				str[3] = new String(rs.getString(4).getBytes("ISO8859_1"),
						"GBK");// info_lon
				str[4] = new String(rs.getString(5).getBytes("ISO8859_1"),
						"GBK");// info_lat
				str[5] = rs.getDate(6).toString();// info_time
				str[6] = new String(rs.getString(7).getBytes("ISO8859_1"),
						"GBK");// uid
				str[7] = new String(rs.getString(8).getBytes("ISO8859_1"),
						"GBK");// mid

				result.add(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (st != null) {
					st.close();
					st = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static ArrayList<jrlaInfo> getjrlaInfoByName(String name, int type,
			int span, int currentPageNo, int where) {// currentPageNo从1开始
		ArrayList<jrlaInfo> result = new ArrayList<jrlaInfo>();
		String info_name = name;
		if (where == 2) {
			try {
				info_name = new String(name.getBytes("GBK"), "ISO8859_1");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();// 打印异常
			}
		}

		String sql;
		if (currentPageNo == -1) {
			sql = "select info_title,info_dis,info_lon,info_lat,info_time,uid,mid from jrla_info;";
		} else {
			int start = span * (currentPageNo - 1);// 计算出起始记录编号
			if (name.trim().equals("")) {
				// LIMIT M,N M+1条记录开始（记录号从1开始） N要几条记录 例如要 5-8条记录 LIMIT 4,4
				sql = "select info_title,info_dis,info_lon,info_lat,info_time,uid,mid from jrla_info limit "
						+ start + "," + span + ";";
			} else {
				switch (type) {
				case 1:
					sql = "select info_title,info_dis,info_lon,info_lat,info_time,uid,mid from jrla_info where info_title like '%"
							+ info_name
							+ "%' limit "
							+ start
							+ ","
							+ span
							+ ";";
					break;
				case 2:// 按种类
					sql = "select info_title,info_dis,info_lon,info_lat,info_time,uid,mid from jrla_info where info_sort like '%"
							+ info_name
							+ "%' limit "
							+ start
							+ ","
							+ span
							+ ";";
					break;
				case 3:// 按价格
					sql = "select info_title,info_dis,info_lon,info_lat,info_time,uid,mid from jrla_info where info_price = "
							+ info_name + " limit " + start + "," + span + ";";
					break;
				default:
					sql = "select info_title,info_dis,info_lon,info_lat,info_time,uid,mid from jrla_info limit "
							+ start + "," + span + ";";
				}
			}
		}

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			// 由于MySQL默认创建的是可滚动的结果集合，因此要在创建语句时指定结果集不可滚动，只读
			st = con.createStatement(ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_READ_ONLY);

			// 执行检索
			rs = st.executeQuery(sql);

			while (rs.next()) {
				String info_title = new String(rs.getString("info_title")
						.getBytes("ISO8859_1"), "GBK");
				String info_dis = new String(rs.getString("info_dis").getBytes(
						"ISO8859_1"), "GBK");
				String info_lon = new String(rs.getString("info_lon").getBytes(
						"ISO8859_1"), "GBK");
				String info_lat = new String(rs.getString("info_lat").getBytes(
						"ISO8859_1"), "GBK");
				String uid = new String(rs.getString("uid").getBytes(
						"ISO8859_1"), "GBK");
				String mid = new String(rs.getString("mid").getBytes(
						"ISO8859_1"), "GBK");
				Date info_time = rs.getDate("info_time");

				jrlaInfo mi = new jrlaInfo(info_title, info_dis, info_lon,
						info_lat, info_time, uid, mid);
				result.add(mi);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (st != null) {
					st.close();
					st = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static boolean isAdmin(String uid) {// 检查是否为管理员
		boolean result = false;
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con
					.prepareStatement("select u_admin from jrla_user where uid=?");
			pstmt.setInt(1, Integer.parseInt(uid));

			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getBoolean(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void insertjrla_recommend(int mid) {// 插入每日推荐
		int id = DBUtil.getMaxNumber("jrla_recommend");
		DBUtil.updateMaxNumber(7);// 将该字段值加1
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con
					.prepareStatement("insert into jrla_recommend(id,mid) values(?,?)");
			pstmt.setInt(1, id);
			pstmt.setInt(2, mid);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void updateUserInfo(String uid, String u_name, String u_qq,
			String u_Email, String u_favourite, String u_dis) {
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con
					.prepareStatement("update jrla_user set u_name=?, u_qq=?, u_Email=?, u_hobby=?, u_dis=? where uid=?;");
			pstmt.setString(1, new String(u_name.getBytes("GBK"), "ISO8859_1"));
			pstmt.setString(2, new String(u_qq.getBytes("GBK"), "ISO8859_1"));
			pstmt.setString(3, new String(u_Email.getBytes("GBK"), "ISO8859_1"));
			pstmt.setString(4, new String(u_favourite.getBytes("GBK"),
					"ISO8859_1"));
			pstmt.setString(5, new String(u_dis.getBytes("GBK"), "ISO8859_1"));
			pstmt.setInt(6, Integer.parseInt(uid));
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void insertjrla_head(String tdis, File fileName, int uid) {
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		FileInputStream fis = null;
		int id = DBUtil.getMaxNumber("jrla_head");
		DBUtil.updateMaxNumber(1);// 将该字段值加1
		try {
			fis = new FileInputStream(fileName);
			pstmt = con
					.prepareStatement("insert into jrla_head(tid,tdis,tdata,uid) values(?,?,?,?)");
			pstmt.setInt(1, id);
			pstmt.setString(2, tdis);
			pstmt.setBinaryStream(3, fis, (int) fileName.length());
			pstmt.setInt(4, uid);
			pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
					fis = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void insertjrla_Ads(File fileName, int gid) {// 插入广告图片
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		FileInputStream fis = null;
		int id = DBUtil.getMaxNumber("jrla_ads_image");
		DBUtil.updateMaxNumber(6);// 将该字段值加1
		try {
			fis = new FileInputStream(fileName);
			pstmt = con
					.prepareStatement("insert into jrla_ads_image(gpid,gid,gdata) values(?,?,?)");
			pstmt.setInt(1, id);
			pstmt.setInt(2, gid);
			pstmt.setBinaryStream(3, fis, (int) fileName.length());
			pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
					fis = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void insertjrla_image(byte[] image, int mid) {
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		FileInputStream fis = null;
		int id = DBUtil.getMaxNumber("jrla_image");
		DBUtil.updateMaxNumber(4);// 将该字段值加1
		try {
			pstmt = con
					.prepareStatement("insert into jrla_image(id,mid,image_data) values(?,?,?)");
			pstmt.setInt(1, id);
			pstmt.setInt(2, mid);
			pstmt.setBytes(3, image);
			pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
					fis = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void insertjrla_image(File fileName, int mid) {// 插入美食图片
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		FileInputStream fis = null;
		int id = DBUtil.getMaxNumber("jrla_image");
		DBUtil.updateMaxNumber(4);// 将该字段值加1
		try {
			fis = new FileInputStream(fileName);
			pstmt = con
					.prepareStatement("insert into jrla_image(id,mid,image_data) values(?,?,?)");
			pstmt.setInt(1, id);
			pstmt.setInt(2, mid);
			pstmt.setBinaryStream(3, fis, (int) fileName.length());
			pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
					fis = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void insertjrlaCol(String mid, String uid, String comment) {
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con
					.prepareStatement("insert into jrla_col(mid,uid,comment) values(?,?,?)");
			pstmt.setString(1, mid);
			pstmt.setString(2, uid);
			pstmt.setString(3, comment);

			pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void deletejrlaCol(String mid, String uid) {
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con
					.prepareStatement("delete from jrla_col where mid=? and uid=?");
			pstmt.setString(1, mid);
			pstmt.setString(2, uid);

			pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static int insertjrla_info(String info_title, String info_dis,
			String info_lon, String info_lat, String uid, String info_sort,
			int info_price, int where, String mshotelName) {// 插入美食信息
		int mid = DBUtil.getMaxNumber("jrla_info");
		DBUtil.updateMaxNumber(3);// 将该字段值加1
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con
					.prepareStatement("insert into jrla_info(mid,info_title,info_dis,info_lon,info_lat,uid,info_sort,info_price,hotel_name) values(?,?,?,?,?,?,?,?,?)");
			if (where == 2) {// 手机来的
				pstmt.setInt(1, mid);
				pstmt.setString(2, new String(info_title.getBytes("GBK"),
						"ISO-8859-1"));
				pstmt.setString(3, new String(info_dis.getBytes("GBK"),
						"ISO-8859-1"));
				pstmt.setDouble(4, Double.parseDouble(info_lon));
				pstmt.setDouble(5, Double.parseDouble(info_lat));
				pstmt.setInt(6, Integer.parseInt(uid));
				pstmt.setInt(7, Integer.parseInt(info_sort));
				pstmt.setInt(8, info_price);
				pstmt.setString(9, new String(mshotelName.getBytes("GBK"),
						"ISO-8859-1"));
			} else {
				pstmt.setInt(1, mid);
				pstmt.setString(2, info_title);
				pstmt.setString(3, info_dis);
				pstmt.setDouble(4, Double.parseDouble(info_lon));
				pstmt.setDouble(5, Double.parseDouble(info_lat));
				pstmt.setInt(6, Integer.parseInt(uid));
				pstmt.setInt(7, Integer.parseInt(info_sort));
				pstmt.setInt(8, info_price);
				pstmt.setString(9, mshotelName);
			}

			pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mid;
	}

	public static ArrayList<jrlaImage> getjrlaImage(int mid) {// 得到美食图片
		ArrayList<jrlaImage> result = new ArrayList<jrlaImage>();
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con
					.prepareStatement("select * from jrla_image where mid=?");
			pstmt.setInt(1, mid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				Blob b = rs.getBlob("image_data");
				Date image_time = rs.getDate("image_time");
				jrlaImage mi = new jrlaImage(id, mid, b, image_time);
				result.add(mi);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static UserInfo getUserByUid(String uid) {// 根据用户ID得到用户所有信息
		UserInfo result = null;
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement("select * from jrla_user where uid=?");
			pstmt.setString(1, new String(uid.getBytes("GBK"), "ISO8859_1"));
			rs = pstmt.executeQuery();
			if (rs.next()) {
				String u_name = new String(rs.getString("u_name").getBytes(
						"ISO8859_1"), "GBK");
				String u_qq = new String(rs.getString("u_qq").getBytes(
						"ISO8859_1"), "GBK");
				String u_Email = new String(rs.getString("u_Email").getBytes(
						"ISO8859_1"), "GBK");
				String u_dis = new String(rs.getString("u_dis").getBytes(
						"ISO8859_1"), "GBK");
				int u_head = rs.getInt("u_head");
				boolean u_admin = rs.getBoolean("u_admin");
				String u_mood = new String((rs.getString("u_mood") == null ? ""
						: rs.getString("u_mood")).getBytes("ISO8859_1"), "GBK");
				int u_integral = rs.getInt("u_integral");// 积分
				String u_hobby = new String(
						(rs.getString("u_hobby") == null ? "" : rs
								.getString("u_hobby")).getBytes("ISO8859_1"),
						"GBK");
				int u_level = rs.getInt("u_level");
				int u_number = rs.getInt("u_number");// 访问次数
				result = new UserInfo(Integer.parseInt(uid), u_name, u_qq,
						u_Email, u_dis, u_head, u_admin, u_mood, u_integral,
						u_hobby, u_level, u_number);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static List<Integer> getjrlaAdsGpid(int ID) {// 根据编号得到所有广告图片
		ArrayList<Integer> result = new ArrayList<Integer>();
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con
					.prepareStatement("select gpid from jrla_ads_image where gid=?");
			pstmt.setInt(1, ID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int gid = rs.getInt(1);
				result.add(gid);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static List<Integer> getjrlaAdsID() {// 得到所有广告位置的编号
		List<Integer> result = new ArrayList<Integer>();
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement("select gid from jrla_ads;");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Integer i = rs.getInt(1);
				result.add(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static Blob getjrlaAdsImageByGpid(int gpid) {
		Blob result = null;
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con
					.prepareStatement("select gdata from jrla_ads_image where gpid = ?;");
			pstmt.setInt(1, gpid);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getBlob(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static void updateUserHead(int uid, int u_head) {// 更新用户头像
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con
					.prepareStatement("update jrla_user set u_head=? where uid=?");
			pstmt.setInt(1, u_head);
			pstmt.setInt(2, uid);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static ArrayList<Integer> getHeadNumber() {// 得到所有头像的id
		ArrayList<Integer> result = new ArrayList<Integer>();
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement("select tid from jrla_head");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Integer tid = rs.getInt("tid");
				result.add(tid);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static void insertjrla_sort(String sort) {// 插入新种类
		int sid = DBUtil.getMaxNumber("jrla_sort");
		DBUtil.updateMaxNumber(8);// 将该字段值加1
		Connection con = getConnection();
		PreparedStatement pstmt = null;

		try {
			pstmt = con
					.prepareStatement("insert into jrla_sort(sid,info_sort) values("
							+ sid
							+ ", '"
							+ new String(sort.getBytes("GBK"), "ISO-8859-1")
							+ "')");
			pstmt.executeUpdate();
		} catch (Exception e) {// 捕获异常
			e.printStackTrace();// 打印异常信息
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static List<String[]> getjrla_sort() {// 得到美食的全部种类
		List<String[]> result = new ArrayList<String[]>();
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con
					.prepareStatement("select sid,info_sort from jrla_sort;");
			rs = pstmt.executeQuery();// 查询
			while (rs.next()) {
				String sid = rs.getString(1);
				String info_sort = rs.getString(2);
				String[] strs = new String[2];
				strs[0] = sid;
				strs[1] = new String(info_sort.getBytes("ISO-8859-1"), "GBK");
				result.add(strs);
			}
		} catch (Exception e) {// 捕获异常
			e.printStackTrace();// 打印异常信息
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static ArrayList<jrlaHeadImage> getHeadImage(int tid) {// 得到指定id的头像图片
		ArrayList<jrlaHeadImage> result = new ArrayList<jrlaHeadImage>();
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement("select * from jrla_head where tid=?");
			pstmt.setInt(1, tid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String tdis = rs.getString("tdis");
				Blob b = rs.getBlob("tdata");
				int uid = rs.getInt("uid");
				jrlaHeadImage mi = new jrlaHeadImage(tid, tdis, b, uid);
				result.add(mi);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public static ArrayList<jrlaInfo> getjrla_recommend() {// 得到所有的每日推荐
		ArrayList<jrlaInfo> result = new ArrayList<jrlaInfo>();
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con
					.prepareStatement("select info_title,info_dis,info_lon,info_lat,a.info_time,a.uid,a.mid from jrla_info as a, jrla_recommend as b where a.mid = b.mid");
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String info_title = new String(rs.getString(1).getBytes(
						"ISO-8859-1"), "GBK");
				String info_dis = new String(rs.getString(2).getBytes(
						"ISO-8859-1"), "GBK");
				String info_lon = new String(rs.getString(3).getBytes(
						"ISO-8859-1"), "GBK");
				String info_lat = new String(rs.getString(4).getBytes(
						"ISO-8859-1"), "GBK");
				Date info_time = rs.getDate(5);
				String uid = new String(rs.getString(6).getBytes("ISO-8859-1"),
						"GBK");
				String mid = new String(rs.getString(7).getBytes("ISO-8859-1"),
						"GBK");
				jrlaInfo mi = new jrlaInfo(info_title, info_dis, info_lon,
						info_lat, info_time, uid, mid);
				result.add(mi);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (con != null) {
				con.close();
				con = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void deletejrla_recommend(int mid) {// 删除每日推荐
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con
					.prepareStatement("delete from jrla_recommend where mid=?");
			pstmt.setInt(1, mid);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static ArrayList<jrlaInfo> getFavourite(String uid) {// 得到我的收藏所有美食
		ArrayList<jrlaInfo> result = new ArrayList<jrlaInfo>();
		String sql = "select info_title,info_dis,info_lon,info_lat,info_time,a.uid,a.mid "
				+ "from jrla_info as a,jrla_col as b where a.mid=b.mid and b.uid = "
				+ uid + ";";
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = con.createStatement();// 创建语句
			rs = st.executeQuery(sql);
			while (rs.next()) {
				String info_title = new String(rs.getString("info_title")
						.getBytes("ISO8859_1"), "GBK");
				String info_dis = new String(rs.getString("info_dis").getBytes(
						"ISO8859_1"), "GBK");
				String info_lon = new String(rs.getString("info_lon").getBytes(
						"ISO8859_1"), "GBK");
				String info_lat = new String(rs.getString("info_lat").getBytes(
						"ISO8859_1"), "GBK");
				String mid = new String(rs.getString("mid").getBytes(
						"ISO8859_1"), "GBK");
				Date info_time = rs.getDate("info_time");

				jrlaInfo mi = new jrlaInfo(info_title, info_dis, info_lon,
						info_lat, info_time, uid, mid);
				result.add(mi);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (st != null) {
					st.close();
					st = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static String checkUser(String uid, String u_pwd)
			throws UnsupportedEncodingException {// 检查用户名是否存在
		String result = null;
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		try {

			st = con.createStatement();// 创建语句
			rs = st.executeQuery("select u_name from jrla_user where uid = '"
					+ uid + "' and u_pwd = '" + u_pwd + "';");
			if (rs.next()) {
				result = new String(rs.getString(1).getBytes("ISO8859_1"),
						"GBK");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// }
		// catch(Exception e){
		// e.printStackTrace();
		// }
		finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (st != null) {
					st.close();
					st = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static int insertUser(String u_name, String u_qq, String u_pwd,
			String u_Email, String u_dis) {// 添加用户
		int uid = DBUtil.getMaxNumber("jrla_user");
		DBUtil.updateMaxNumber(2);// 将该字段值加1
		Connection con = getConnection();
		Statement st = null;
		try {
			u_name = new String(u_name.getBytes("GBK"), "ISO8859_1");
			u_dis = new String(u_dis.getBytes("GBK"), "ISO8859_1");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			st = con.createStatement();
			String sql = "insert into jrla_user"
					+ "(u_admin,uid,u_name,u_qq,u_pwd,u_Email,u_dis) "
					+ "values(false," + uid + ",'" + u_name + "','"
					+ ((u_qq.equals("")) ? 0 : u_qq) + "','" + u_pwd + "','"
					+ u_Email + "','" + u_dis + "');";
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// }catch(Exception e){
		// e.printStackTrace();
		// }
		finally {
			try {
				if (st != null) {
					st.close();
					st = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return uid;
	}

	public static void updateMaxNumber(int what) {// 更改jrla_max中相应列的值
		Connection con = getConnection();// 得到数据库连接
		Statement st = null;
		String sql = "";
		try {
			st = con.createStatement();
			switch (what) {
			case 1:
				sql = "update jrla_max set jrla_head = jrla_head + 1 ";
				break;
			case 2:
				sql = "update jrla_max set jrla_user  = jrla_user  + 1 ";
				break;
			case 3:
				sql = "update jrla_max set jrla_info   = jrla_info   + 1 ";
				break;
			case 4:
				sql = "update jrla_max set jrla_image  = jrla_image  + 1 ";
				break;
			case 5:
				sql = "update jrla_max set jrla_ads   = jrla_ads   + 1 ";
				break;
			case 6:
				sql = "update jrla_max set jrla_ads_image   = jrla_ads_image   + 1 ";
				break;
			case 7:
				sql = "update jrla_max set jrla_recommend   = jrla_recommend   + 1 ";
				break;
			case 8:
				sql = "update jrla_max set jrla_sort  = jrla_sort   + 1 ";
				break;
			}
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// }catch(Exception e){
		// e.printStackTrace();
		// }
		finally {
			try {
				if (st != null) {
					st.close();
					st = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static int getMaxNumber(String what) {// 得到最大编号，即某个表的主键自加时的最大
		int result = 0;
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = con.createStatement();
			rs = st.executeQuery("select * from jrla_max");
			rs.next();
			result = rs.getInt(what);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// }catch(Exception e){
		// e.printStackTrace();
		// }
		finally {
			try {
				if (st != null) {
					st.close();
					st = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// public static void main(String[] args) {
	// try {
	// int i = DBUtil.getjrlaInfoCountForPhone("a", -1, "", "743");
	// System.out.println(i);
	//
	// } catch (NullPointerException e) {
	// System.out.println("发生异常的原因为 :" + e.getMessage());
	// }
	//
	// }

	public static String get_jrla_explain_list_max() {
		// TODO Auto-generated method stub
		int result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			st = con.createStatement();
			rs = st.executeQuery("select count(*) from jrla_explain_list");
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result + "";
	}

	public static String get_jrla_explain_list_data(String data_start,
			String data_end,String  id_col) {
		// TODO Auto-generated method stub
		int result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String writebuff = "";
		try {
			con = getConnection();
			st = con.createStatement();
			int step = Integer.parseInt(data_end)
					- Integer.parseInt(data_start);
			rs = st.executeQuery("select jrla_explain_list.id,name,data_start,data_end,other_name,others,url,(case when (select jrla_col.did from jrla_col where jrla_col.did=jrla_explain_list.id && jrla_col.uid="+id_col+" ) is not null then 1 else 0 end) as did from jrla_explain_list limit "
					+ data_start + "," + step);

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");// new
													// String(rs.getString("name").getBytes("utf-8"),
													// "utf-8");
				String d_start = rs.getDate("data_start").toString();
				String d_end = rs.getDate("data_end").toString();
				String other_name = rs.getString("other_name");// new
																// String(rs.getString("other_name").getBytes("utf-8"),
																// "utf-8");

				String others = "";
				if (rs.getString("others") != null)
					others = new String(rs.getString("others")
							.getBytes("utf-8"), "utf-8");

				String url = new String(rs.getString("url").getBytes("utf-8"),
						"utf-8");

				int did = rs.getInt("did");
				writebuff += id + "|" + name + "|" + d_start + "|" + d_end
						+ "|" + other_name + "|" + others + "|" + url + "|"
						+ did + "~";
				// System.out.println(id+"|"+name+"|"+d_start+"|"+d_end+"|"+other_name+"|"+others+"|"+url+"|"+did);
				// result.add(mi);~
			}
			System.out.println("11");
			String testbuff[] = writebuff.split("\\~");
			for (int i = 0; i < testbuff.length; i++)
				System.out.println(testbuff[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writebuff;
	}

	public static String get_jrla_activity_list_max() {
		// TODO Auto-generated method stub
		int result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			st = con.createStatement();
			rs = st.executeQuery("select count(*) from jrla_activity_list");
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result + "";
	}

	public static String get_jrla_activity_list_data(String data_start,
			String data_end,String  id_col) {
		// TODO Auto-generated method stub
		int result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String writebuff = "";
		try {
			con = getConnection();
			st = con.createStatement();
			int step = Integer.parseInt(data_end)
					- Integer.parseInt(data_start);
			// id,name,price,("addr")("star"))("tuan"))("card"))("promo"))("checkin")),laitude,longitude,did
			rs = st.executeQuery("select jrla_activity_list.id,name,price,addr,star,latitude,longitude,(case when (select jrla_col.aid from jrla_col where jrla_col.aid=jrla_activity_list.id && jrla_col.uid="+id_col+" ) is not null then 1 else 0 end) as aid from jrla_activity_list limit "
					+ data_start + "," + step);

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");// new
													// String(rs.getString("name").getBytes("utf-8"),
													// "utf-8");
				String price = rs.getString("price");
				String addr = rs.getString("addr").toString();
				Float star = rs.getFloat("star");// new
													// String(rs.getString("other_name").getBytes("utf-8"),
													// "utf-8");
				Double latitude = rs.getDouble("latitude");
				Double longitude = rs.getDouble("longitude");

				int aid = rs.getInt("aid");
				writebuff += id + "|" + name + "|" + price + "|" + addr + "|"
						+ star + "|" + latitude + "|" + longitude + "|" + aid
						+ "~";
				// System.out.println(id+"|"+name+"|"+d_start+"|"+d_end+"|"+other_name+"|"+others+"|"+url+"|"+did);
				// result.add(mi);~
			}
			System.out.println("11");
			String testbuff[] = writebuff.split("\\~");
			for (int i = 0; i < testbuff.length; i++)
				System.out.println(testbuff[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writebuff;
	}

	public static String get_jrla_food_list_max() {
		// TODO Auto-generated method stub
		int result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			st = con.createStatement();
			rs = st.executeQuery("select count(*) from jrla_food_list");
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result + "";
	}

	public static String get_jrla_food_list_data(String data_start,
			String data_end,String  id_col) {
		// TODO Auto-generated method stub
		int result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String writebuff = "";
		try {
			con = getConnection();
			st = con.createStatement();
			int step = Integer.parseInt(data_end)
					- Integer.parseInt(data_start);
			// id,name,price,("addr")("star"))("tuan"))("card"))("promo"))("checkin")),laitude,longitude,did
			rs = st.executeQuery("select jrla_food_list.id,name,price,addr,star,latitude,longitude,(case when (select jrla_col.fid from jrla_col where jrla_col.fid=jrla_food_list.id && jrla_col.uid="+id_col+" ) is not null then 1 else 0 end) as aid from jrla_food_list limit "
					+ data_start + "," + step);

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");// new
													// String(rs.getString("name").getBytes("utf-8"),
													// "utf-8");
				String price = rs.getString("price");
				String addr = rs.getString("addr").toString();
				Float star = rs.getFloat("star");// new
													// String(rs.getString("other_name").getBytes("utf-8"),
													// "utf-8");
				Double latitude = rs.getDouble("latitude");
				Double longitude = rs.getDouble("longitude");

				int aid = rs.getInt("aid");
				writebuff += id + "|" + name + "|" + price + "|" + addr + "|"
						+ star + "|" + latitude + "|" + longitude + "|" + aid
						+ "~";
				// System.out.println(id+"|"+name+"|"+d_start+"|"+d_end+"|"+other_name+"|"+others+"|"+url+"|"+did);
				// result.add(mi);~
			}
			System.out.println("11");
			String testbuff[] = writebuff.split("\\~");
			for (int i = 0; i < testbuff.length; i++)
				System.out.println(testbuff[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writebuff;
	}

	public static String get_jrla_gift_list_max() {
		// TODO Auto-generated method stub
		int result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			st = con.createStatement();
			rs = st.executeQuery("select count(*) from jrla_gift_list");
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result + "";
	}

	public static String get_jrla_gift_list_data(String data_start,
			String data_end,String  id_col) {
		// TODO Auto-generated method stub
		int result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String writebuff = "";
		try {
			con = getConnection();
			st = con.createStatement();
			int step = Integer.parseInt(data_end)
					- Integer.parseInt(data_start);
			// id,name,price,("addr")("star"))("tuan"))("card"))("promo"))("checkin")),laitude,longitude,did
			rs = st.executeQuery("select jrla_gift_list.id,name,price,addr,star,latitude,longitude,(case when (select jrla_col.gid from jrla_col where jrla_col.gid=jrla_gift_list.id && jrla_col.uid="+id_col+" ) is not null then 1 else 0 end) as aid from jrla_gift_list limit "
					+ data_start + "," + step);

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");// new
													// String(rs.getString("name").getBytes("utf-8"),
													// "utf-8");
				String price = rs.getString("price");
				String addr = rs.getString("addr").toString();
				Float star = rs.getFloat("star");// new
													// String(rs.getString("other_name").getBytes("utf-8"),
													// "utf-8");
				Double latitude = rs.getDouble("latitude");
				Double longitude = rs.getDouble("longitude");

				int aid = rs.getInt("aid");
				writebuff += id + "|" + name + "|" + price + "|" + addr + "|"
						+ star + "|" + latitude + "|" + longitude + "|" + aid
						+ "~";
				// System.out.println(id+"|"+name+"|"+d_start+"|"+d_end+"|"+other_name+"|"+others+"|"+url+"|"+did);
				// result.add(mi);~
			}
			System.out.println("11");
			String testbuff[] = writebuff.split("\\~");
			for (int i = 0; i < testbuff.length; i++)
				System.out.println(testbuff[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writebuff;
	}

	public static String get_jrla_activity_list_detail(String string) {
		// TODO Auto-generated method stub
		int result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String writebuff = "";
		try {
			con = getConnection();
			st = con.createStatement();
			// id,name,price,("addr")("star"))("tuan"))("card"))("promo"))("checkin")),laitude,longitude,did
			rs = st.executeQuery("select id,chara,phone_num,famous_activity,detail_explain,others,data_start,data_end from jrla_activity_list where id="
					+ string);

			while (rs.next()) {
				int id = rs.getInt("id");
				String chara = rs.getString("chara");// new
														// String(rs.getString("name").getBytes("utf-8"),
														// "utf-8");
				String phone_num = rs.getString("phone_num");
				String famous_activity = rs.getString("famous_activity")
						.toString();
				String detail_explain = rs.getString("detail_explain");// new
																		// String(rs.getString("other_name").getBytes("utf-8"),
																		// "utf-8");
				String others = rs.getString("others");
				String data_start = rs.getDate("data_start").toString();
				String data_end = rs.getDate("data_end").toString();

				writebuff = id + "|" + chara + "|" + phone_num + "|"
						+ famous_activity + "|" + detail_explain + "|" + others
						+ "|" + data_start + "|" + data_end;
			}
			System.out.println(writebuff);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writebuff;
	}

	public static String get_jrla_food_list_detail(String string) {
		// TODO Auto-generated method stub
		int result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String writebuff = "";
		try {
			con = getConnection();
			st = con.createStatement();
			// id,name,price,("addr")("star"))("tuan"))("card"))("promo"))("checkin")),laitude,longitude,did
			rs = st.executeQuery("select id,chara,phone_num,famous_food,detail_explain,others,custom_tailor from jrla_food_list where id="
					+ string);

			while (rs.next()) {
				int id = rs.getInt("id");
				String chara = rs.getString("chara");// new
														// String(rs.getString("name").getBytes("utf-8"),
														// "utf-8");
				String phone_num = rs.getString("phone_num");
				String famous_food = rs.getString("famous_food");
				String detail_explain = rs.getString("detail_explain");// new
																		// String(rs.getString("other_name").getBytes("utf-8"),
																		// "utf-8");
				String others = rs.getString("others");
				String custom_tailor = rs.getString("custom_tailor");

				writebuff = id + "|" + chara + "|" + phone_num + "|"
						+ famous_food + "|" + detail_explain + "|" + others
						+ "|" + custom_tailor;
			}
			System.out.println(writebuff);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writebuff;
	}

	public static String get_jrla_gift_list_detail(String string) {
		// TODO Auto-generated method stub
		int result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String writebuff = "";
		try {
			con = getConnection();
			st = con.createStatement();
			// id,name,price,("addr")("star"))("tuan"))("card"))("promo"))("checkin")),laitude,longitude,did
			rs = st.executeQuery("select id,chara,phone_num,famous_gift,detail_explain,others,custom_tailor from jrla_gift_list where id="
					+ string);

			while (rs.next()) {
				int id = rs.getInt("id");
				String chara = rs.getString("chara");// new
														// String(rs.getString("name").getBytes("utf-8"),
														// "utf-8");
				String phone_num = rs.getString("phone_num");
				String famous_gift = rs.getString("famous_gift");
				String detail_explain = rs.getString("detail_explain");// new
																		// String(rs.getString("other_name").getBytes("utf-8"),
																		// "utf-8");
				String others = rs.getString("others");
				String custom_tailor = rs.getString("custom_tailor");

				writebuff = id + "|" + chara + "|" + phone_num + "|"
						+ famous_gift + "|" + detail_explain + "|" + others
						+ "|" + custom_tailor;
			}
			System.out.println(writebuff);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writebuff;
	}

	public static String get_jrla_user_data(String id) {
		// TODO Auto-generated method stub
		int result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String writebuff = "";
		try {
			con = getConnection();
			st = con.createStatement();
			// id,name,price,("addr")("star"))("tuan"))("card"))("promo"))("checkin")),laitude,longitude,did
			rs = st.executeQuery("select uid,u_pwd,u_qq,u_email,u_custom,u_hobby from jrla_user where uid="
					+ id);

			while (rs.next()) {
				int uid = rs.getInt("uid");
				String u_pwd = rs.getString("u_pwd");// new
														// String(rs.getString("name").getBytes("utf-8"),
														// "utf-8");
				String u_qq = rs.getString("u_qq");
				String u_email = rs.getString("u_email") + "";
				String u_custom = rs.getInt("u_custom") + "";// new
																// String(rs.getString("other_name").getBytes("utf-8"),
																// "utf-8");
				String u_hobby = rs.getString("u_hobby");

				writebuff = uid + "|" + u_pwd + "|" + u_qq + "|" + u_email
						+ "|" + u_custom + "|" + u_hobby;
				// System.out.println(id+"|"+name+"|"+d_start+"|"+d_end+"|"+other_name+"|"+others+"|"+url+"|"+did);
				// result.add(mi);
			}
			System.out.println("11");
			System.out.println(writebuff);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writebuff;
	}

	public static String update_jrla_user(String msg) {
		// TODO Auto-generated method stub

		int result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		PreparedStatement pstmt = null;

		String writebuff = "";
		try {
			con = getConnection();
			st = con.createStatement();
			// id,name,price,("addr")("star"))("tuan"))("card"))("promo"))("checkin")),laitude,longitude,did
			String string[] = msg.split("\\|");

			pstmt = con
					.prepareStatement("update jrla_user set u_pwd=?,u_qq=?,u_email=?,u_custom=?,u_hobby=? where uid=?");
			pstmt.setString(1, string[2]);
			pstmt.setString(2, string[3]);
			pstmt.setString(3, string[4]);
			pstmt.setString(4, string[5]);
			pstmt.setString(5, string[6]);
			pstmt.setString(6, string[1]);
			pstmt.executeUpdate();

			System.out.println(pstmt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writebuff;
	}

	public static String add_activity_col(String msg) {
		// TODO Auto-generated method stub
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String  string[]=msg.split("\\|");
		try {
			con = getConnection();
			st = con.createStatement();
	
		String sql = "insert into jrla_col(aid,uid) values(" + string[1] + "," + string[2]+");";
		st.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String add_food_col(String msg) {
		// TODO Auto-generated method stub
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String  string[]=msg.split("\\|");
		try {
			con = getConnection();
			st = con.createStatement();
	
		String sql = "insert into jrla_col(fid,uid) values(" + string[1] + "," + string[2]+");";
		st.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String add_gift_col(String msg) {
		// TODO Auto-generated method stub
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String  string[]=msg.split("\\|");
		
		try {
			con = getConnection();
			st = con.createStatement();
	
		String sql = "insert into jrla_col(gid,uid) values(" + string[1] + "," + string[2]+");";
		st.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void del_activity_col(String msg) {
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		String  string[]=msg.split("\\|");
		try {
			pstmt = con.prepareStatement("delete from jrla_col where aid=? and uid=?");
			pstmt.setString(1, string[1]);
			pstmt.setString(2, string[2]);

			pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	public static void del_food_col(String msg) {
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		String  string[]=msg.split("\\|");
		try {
			pstmt = con.prepareStatement("delete from jrla_col where fid=? and uid=?");
			pstmt.setString(1, string[1]);
			pstmt.setString(2, string[2]);

			pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void del_gift_col(String msg) {
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		String  string[]=msg.split("\\|");
		try {
			pstmt = con.prepareStatement("delete from jrla_col where gid=? and uid=?");
			pstmt.setString(1, string[1]);
			pstmt.setString(2, string[2]);

			pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String get_jrla_activity_col_max(String string) {
		int result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			st = con.createStatement();
			rs = st.executeQuery("select count(aid) from jrla_col where uid="+string);
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result + "";
	}

	public static String get_jrla_activity_list_data_col(String data_start,
			String data_end,String  id_col) {
		// TODO Auto-generated method stub
		int result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String writebuff = "";
		try {
			con = getConnection();
			st = con.createStatement();
			int step = Integer.parseInt(data_end)
					- Integer.parseInt(data_start);
			// id,name,price,("addr")("star"))("tuan"))("card"))("promo"))("checkin")),laitude,longitude,did
			rs = st.executeQuery("select jrla_activity_list.id,name,price,addr,star,latitude,longitude,(case when (select jrla_col.aid from jrla_col where jrla_col.aid=jrla_activity_list.id && jrla_col.uid="+id_col+" ) is not null then 1 else 0 end) as aid from jrla_activity_list,jrla_col where jrla_col.aid=jrla_activity_list.id && jrla_col.uid= "+id_col+" limit "
					+ data_start + "," + step);

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");// new
													// String(rs.getString("name").getBytes("utf-8"),
													// "utf-8");
				String price = rs.getString("price");
				String addr = rs.getString("addr").toString();
				Float star = rs.getFloat("star");// new
													// String(rs.getString("other_name").getBytes("utf-8"),
													// "utf-8");
				Double latitude = rs.getDouble("latitude");
				Double longitude = rs.getDouble("longitude");

				int aid = rs.getInt("aid");
				writebuff += id + "|" + name + "|" + price + "|" + addr + "|"
						+ star + "|" + latitude + "|" + longitude + "|" + aid
						+ "~";
				// System.out.println(id+"|"+name+"|"+d_start+"|"+d_end+"|"+other_name+"|"+others+"|"+url+"|"+did);
				// result.add(mi);~
			}
			System.out.println("11");
			String testbuff[] = writebuff.split("\\~");
			for (int i = 0; i < testbuff.length; i++)
				System.out.println(testbuff[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writebuff;
	}

	public static String get_aid_max() {
		// TODO Auto-generated method stub
		int result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			st = con.createStatement();
			rs = st.executeQuery("select aid, count(aid) AS count from jrla_col group by aid order by count DESC  limit 1;");
			if (rs.next()) {
				result = rs.getInt("aid");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("aid="+result);
		return result + "";
	}
	
	
	public static String get_fid_max() {
		// TODO Auto-generated method stub
		int result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			st = con.createStatement();
			rs = st.executeQuery("select fid, count(fid) AS count from jrla_col group by fid order by count DESC  limit 1;");
			if (rs.next()) {
				result = rs.getInt("fid");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("fid="+result);
		return result + "";
	}
	
	
	public static String get_gid_max() {
		// TODO Auto-generated method stub
		int result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			st = con.createStatement();
			rs = st.executeQuery("select gid, count(gid) AS count from jrla_col group by gid order by count DESC  limit 1;");
			if (rs.next()) {
				result = rs.getInt("gid");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("gid="+result);
		return result + "";
	}

	public static String get_xid_data(String aid, String fid,
			String gid,String id_col) {
		
		int result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String writebuff = "";
		try {
			con = getConnection();
			st = con.createStatement();
			
			
			
			rs = st.executeQuery("select jrla_activity_list.id,name,price,addr,star,latitude,longitude,(case when (select jrla_col.gid from jrla_col where jrla_col.aid=jrla_activity_list.id && jrla_col.uid="+id_col+" ) is not null then 1 else 0 end) as aid from jrla_activity_list where jrla_activity_list.id="+gid);

			if (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");// new
													// String(rs.getString("name").getBytes("utf-8"),
													// "utf-8");
				String price = rs.getString("price");
				String addr = rs.getString("addr").toString();
				Float star = rs.getFloat("star");// new
													// String(rs.getString("other_name").getBytes("utf-8"),
													// "utf-8");
				Double latitude = rs.getDouble("latitude");
				Double longitude = rs.getDouble("longitude");

				int aid1 = rs.getInt("aid");
				writebuff += id + "|" + name + "|" + price + "|" + addr + "|"
						+ star + "|" + latitude + "|" + longitude + "|" + aid1
						+ "~";
			
			}
			
			
			rs = st.executeQuery("select jrla_food_list.id,name,price,addr,star,latitude,longitude,(case when (select jrla_col.fid from jrla_col where jrla_col.fid=jrla_food_list.id && jrla_col.uid="+id_col+" ) is not null then 1 else 0 end) as fid from jrla_food_list where jrla_food_list.id="+gid);

			if (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");// new
													// String(rs.getString("name").getBytes("utf-8"),
													// "utf-8");
				String price = rs.getString("price");
				String addr = rs.getString("addr").toString();
				Float star = rs.getFloat("star");// new
													// String(rs.getString("other_name").getBytes("utf-8"),
													// "utf-8");
				Double latitude = rs.getDouble("latitude");
				Double longitude = rs.getDouble("longitude");

				int aid1 = rs.getInt("fid");
				writebuff += id + "|" + name + "|" + price + "|" + addr + "|"
						+ star + "|" + latitude + "|" + longitude + "|" + aid1
						+ "~";
			
			}
			
			// id,name,price,("addr")("star"))("tuan"))("card"))("promo"))("checkin")),laitude,longitude,did
			rs = st.executeQuery("select jrla_gift_list.id,name,price,addr,star,latitude,longitude,(case when (select jrla_col.gid from jrla_col where jrla_col.gid=jrla_gift_list.id && jrla_col.uid="+id_col+" ) is not null then 1 else 0 end) as gid from jrla_gift_list where jrla_gift_list.id="+gid);

			if (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");// new
													// String(rs.getString("name").getBytes("utf-8"),
													// "utf-8");
				String price = rs.getString("price");
				String addr = rs.getString("addr").toString();
				Float star = rs.getFloat("star");// new
													// String(rs.getString("other_name").getBytes("utf-8"),
													// "utf-8");
				Double latitude = rs.getDouble("latitude");
				Double longitude = rs.getDouble("longitude");

				int aid1 = rs.getInt("gid");
				writebuff += id + "|" + name + "|" + price + "|" + addr + "|"
						+ star + "|" + latitude + "|" + longitude + "|" + aid1
						+ "~";
				// System.out.println(id+"|"+name+"|"+d_start+"|"+d_end+"|"+other_name+"|"+others+"|"+url+"|"+did);
				// result.add(mi);~
			}
			System.out.println("11");
			String testbuff[] = writebuff.split("\\~");
			for (int i = 0; i < testbuff.length; i++)
				System.out.println(testbuff[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writebuff;
	}
}