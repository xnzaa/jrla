package com.jrla.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class explain_item_proxy {
	private int i = 0;
	private List<Map<String, Object>> list;
	private Object obj;

	private int data_start = 0;
	private int data_end = 0;
	private Boolean is_end = false;

	Context  context;
	
	public explain_item_proxy(Context  con)
	{
		context=con;
	}
	
	public void sendRequest(final ServerListener listener) {
	
		final int max = get_jrla_explain_list_max();
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
					Log.e("xbb-data", data_start + "|" + data_end);
					if (data_end>=max) {
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

		String str = get_jrla_explain_list_data(num3, num4); // getdate(num3,num4);
		if (str == "null") {
			return list;
		} else {
			String data1[] = str.split("\\~");
			for (int j = 0; j < data1.length; j++) {
				String data[] = data1[j].split("\\|");
				
				Map<String, Object> map = new HashMap<String, Object>();
				for(int i=0;i<8;i++)
					if(data[i]=="")
						data[i]=" ";
				map.put("id", data[0]);
				map.put("name", data[1]);
				map.put("data_start", data[2]);
				map.put("data_end", data[3]);
				map.put("othername", data[4]);
				map.put("others", data[5]);
				map.put("url", data[6]);
				map.put("did", data[7]);
				list.add(map);
			}
			return list;
		}
	}

	// @Override
	// public void onReceive(Context context, Intent intent) {
	// // TODO Auto-generated method stub
	// Toast.makeText(
	// context,
	// "Action£º" + intent.getAction() + "\nÄÚÈÝÊÇ£º"
	// + intent.getStringExtra("msg"),
	// Toast.LENGTH_LONG).show();
	// }

	private int get_jrla_explain_list_max() {
		return communicate.get_max_table(communicate.table_jrla_explain_list,
				"");

	}

	private String get_jrla_explain_list_data(int da_start, int da_end) {
		return communicate.get_data_table(communicate.table_jrla_explain_list,
				da_start, da_end,activity_login.spf_read_user_id(context));

	}
}