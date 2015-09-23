package com.jrla.client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

public class main_item_proxy {
	
	private int i = 0;
	private List<Map<String, Object>> list;
	private Object obj;

	private int data_start = 0;
	private int data_end = 0;
	private Boolean is_end = false;
	
	Context  context;
	
	public main_item_proxy(Context  con)
	{
		context=con;
	}
	
	String gid=null;
	String aid=null;
	String fid=null;
	
	public void sendRequest(final ServerListener listener) {

			Thread thread = new Thread() {
				public void run() {
					try {
						aid=get_aid_max();
						fid=get_fid_max();
						gid=get_gid_max();
					} catch (Exception e) {

					}
					list = createData(aid, fid,gid);
					listener.serverDataArrived(list, true);

				}
			};
			thread.start();
		}


	private List<Map<String, Object>> createData(String str_aid,String str_fid,String str_gid) {
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();

		String str = get_xid_data(str_aid, str_fid, str_gid); // getdate(num3,num4);
		if (str == "null") {
			return list;
		} else {
			String data1[] = str.split("\\~");
			for (int j = 0; j < 2; j++) {
				String data[] = data1[j].split("\\|");
				
				Map<String, Object> map = new HashMap<String, Object>();
				for(int i=0;i<8;i++)//主要修改这里
					if(data[i]=="")
						data[i]=" ";
				//id+"|"+name+"|"+price+"|"+addr+"|"+star+"|"+latitude+"|"+longitude+"|"+aid+"~";
				map.put("id", data[0]);
				map.put("name", data[1]);
				map.put("price", data[2]);
				map.put("addr", data[3]);
				map.put("star", data[4]);
				String  disString="...";
				if(get_location.latitude!="")
					disString=get_location.getDistance(data[6], data[5], get_location.latitude, get_location.longtitude);
				map.put("distance", disString);
				if(j==0)
				map.put("aid", data[7]);
				else if(j==1)
					map.put("fid", data[7]);
				list.add(map);
			}
			return list;
		}
	}


	private String get_fid_max() {
		return communicate.get_fid_max();

	}
	
	private String get_gid_max() {
		return communicate.get_gid_max();

	}
	
	private String get_aid_max() {
		return communicate.get_aid_max();

	}
	
	private String get_xid_data(String aid,String fid,String gid){
		return communicate.get_xid_data(aid,fid,gid,activity_login.spf_read_user_id(context));
	}
}
