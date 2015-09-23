package com.jrla.client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

public class food_item_proxy  {
	
	private int i = 0;
	private List<Map<String, Object>> list;
	private Object obj;

	private int data_start = 0;
	private int data_end = 0;
	private Boolean is_end = false;
	
	Context  context;
	
	public food_item_proxy(Context  con)
	{
		context=con;
	}
	
	public void sendRequest(final ServerListener listener) {
	
		final int max =get_jrla_food_list_max();
		data_end = data_start + 5;
		data_end = data_end > max ? max : data_end;
		final int num1 = data_start;
		final int num2 = data_end;
		Log.e("xbb-data1", data_start + "|" + data_end + "|" + max);
		if (!is_end) {

			Thread thread = new Thread() {
				public void run() {
					try {
						//this.sleep(1000);
					} catch (Exception e) {

					}

					list = createData(num1, num2);
					if (data_end==max) {
						listener.serverDataArrived(list, true);
					} else {
						listener.serverDataArrived(list, false);
					}

				}
			};
			thread.start();
			if (data_end == max)
				is_end = true;
			else
				data_start += 5;
		}

	}

	private List<Map<String, Object>> createData(int num3, int num4) {
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();

		String str = get_jrla_food_list_data(num3, num4); // getdate(num3,num4);
		if (str == "null") {
			return list;
		} else {
			String data1[] = str.split("\\~");
			for (int j = 0; j < data1.length; j++) {
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
//				map.put("url", data[6]);
				map.put("fid", data[7]);
				list.add(map);
			}
			return list;
		}
	}


	private int get_jrla_food_list_max() {
		return communicate.get_max_table(communicate.table_jrla_food_list,
				"");

	}

	private String get_jrla_food_list_data(int da_start, int da_end) {
		return communicate.get_data_table(communicate.table_jrla_food_list,
				da_start, da_end,activity_login.spf_read_user_id(context));

	}
}
