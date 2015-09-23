package com.jrla.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.jrla.adapter.AreaAdapter;
import com.jrla.adapter.CateAdapter;
import com.jrla.adapter.SortAdapter;
import com.jrla.widget.PoiListItem;
import com.jrla.widget.explain_item;

public class activity_main_explain extends Activity implements ServerListener,
		OnClickListener {
	private List<Map<String, Object>> mData;
	private List<Map<String, Object>> filterData;
	private View loadingView;
	private ListView list;
	private boolean isEnd = false;
	private explain_item_proxy server;
	ListAdapter areaAdapter = null;
	ListAdapter resultAdapter = null;
	CateAdapter cateAdapter = null;
	SortAdapter sortAdapter = null;

	private boolean isLoadingRemoved = false;

	ImageView bn_back = null;

	Handler handler = new Handler() {
		public void handleMessage(Message paramMessage) {
			if (paramMessage.what == 1) {
				loadingView.setVisibility(View.GONE);
			} else if (paramMessage.what == 2) {
				list.removeFooterView(loadingView);
				isLoadingRemoved = true;
			} else if (paramMessage.what == 3) {
				list.addFooterView(loadingView);
				loadingView.setVisibility(View.VISIBLE);
				isLoadingRemoved = false;
			} else if (paramMessage.what == 4) {
				loadingView.setVisibility(View.VISIBLE);
			}
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_main_explain);

		bn_back = (ImageView) findViewById(R.id.map_bn_login_back);
		bn_back.setOnClickListener(this);

		// 新建 消息队列
		mData = PoiResultData.getData();
		filterData = mData;

		list = (ListView) findViewById(R.id.resultlist);
		// list.setOnItemClickListener(mOnClickListener);
		resultAdapter = new PoiResultAdapter(this);

		server = new explain_item_proxy(getApplicationContext());
		server.sendRequest(this);

		loadingView = LayoutInflater.from(this).inflate(R.layout.listfooter,
				null);

		list.addFooterView(loadingView);
		// loadingView.setVisibility(View.GONE);

		list.setAdapter(resultAdapter);
		list.setOnItemClickListener(mOnClickListener);
	}

	private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {

			Toast.makeText(activity_main_explain.this,
					filterData.get(position).get("name").toString(),
					Toast.LENGTH_SHORT).show();
			
			Intent intent = new Intent();
			Bundle bundle=new Bundle();
			bundle.putString("url", filterData.get(position).get("url").toString());
			bundle.putString("name", filterData.get(position).get("name").toString());
			intent.putExtras(bundle);
			intent.setClass(activity_main_explain.this,
					activity_explain_detail.class);

			startActivity(intent);
			// ResultActivity.this.finish();
		}
	};

	public class PoiResultAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public PoiResultAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return filterData.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				Log.v("is NULL", "DF2" + position);
			}

			Log.v("ListViewLog", "DF" + position);

			convertView = mInflater.inflate(R.layout.layout_explain_item, null);

			explain_item item = (explain_item) convertView;

			Map map = filterData.get(position);

			item.setPoiData(map.get("name") == null ? "" : map.get("name")
					.toString(),
					map.get("date_start") == null ? "" : map.get("date_start")
							.toString(), map.get("date_end") == null ? "" : map
							.get("date_end").toString(),
					map.get("othername") == null ? "" : map.get("othername")
							.toString(), map.get("others") == null ? "" : map
							.get("others").toString(),
					map.get("id") == null ? "" : map.get("id").toString(), map
							.get("did") == null ? "" : map.get("did")
							.toString());

			if (position == filterData.size() - 1 && !isEnd) {
				loadingView.setVisibility(View.VISIBLE);
				server.sendRequest(activity_main_explain.this);
			}

			return convertView;
		}

	}

	@Override
	public void serverDataArrived(List list, boolean isEnd) {
		this.isEnd = isEnd;
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			mData.add((Map<String, Object>) iter.next());
		}
		Message localMessage = new Message();
		if (!isEnd) {
			localMessage.what = 1;
		} else {
			localMessage.what = 2;
		}

		this.handler.sendMessage(localMessage);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.map_bn_login_back: {
			finish();
			break;
		}
		}

	}

}