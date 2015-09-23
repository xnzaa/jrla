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

import com.jrla.adapter.AreaAdapter;
import com.jrla.adapter.CateAdapter;
import com.jrla.adapter.SortAdapter;
import com.jrla.widget.PoiListItem;

public class activity_main_activity extends Activity implements ServerListener,
		OnClickListener {
	private List<Map<String, Object>> mData;
	private List<Map<String, Object>> filterData;
	private View loadingView;
	private ListView list;
	private boolean isEnd = false;
	private activity_item_proxy server;
	ListAdapter areaAdapter = null;
	ListAdapter resultAdapter = null;
	CateAdapter cateAdapter = null;
	SortAdapter sortAdapter = null;

	private boolean isLoadingRemoved = false;

	ImageView bn_back = null;

	Button bn_activity_col = null;
	
	static Boolean is_collect=false;
	
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
		setContentView(R.layout.layout_main_activity);

		bn_activity_col = (Button) findViewById(R.id.bn_activity_col);
		bn_activity_col.setOnClickListener(this);

		bn_back = (ImageView) findViewById(R.id.map_bn_login_back);
		bn_back.setOnClickListener(this);

		mData = PoiResultData.getData();
		filterData = mData;

		list = (ListView) findViewById(R.id.resultlist);
		// list.setOnItemClickListener(mOnClickListener);
		resultAdapter = new PoiResultAdapter(this);

		View btnArea = findViewById(R.id.id_area);
		btnArea.setOnClickListener(this);

		View btnType = findViewById(R.id.id_type);
		btnType.setOnClickListener(this);

		View btnSort = findViewById(R.id.id_sort);
		btnSort.setOnClickListener(this);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null
				&& bundle.getString("activity").equalsIgnoreCase("collect")) {
			
			bn_activity_col.setVisibility(View.INVISIBLE);
			is_collect=true;
			server = new activity_item_proxy(getApplicationContext());
			server.sendcolRequest(this);
			
		} else {
			is_collect=false;
			server = new activity_item_proxy(getApplicationContext());
			server.sendRequest(this);
		}

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

			Bundle bundle = new Bundle();

			bundle.putString("id", filterData.get(position).get("id")
					.toString());
			bundle.putString("xid", filterData.get(position).get("aid")
					.toString());
			bundle.putString("name", filterData.get(position).get("name")
					.toString());
			bundle.putString("price", filterData.get(position).get("price")
					.toString());
			bundle.putString("addr", filterData.get(position).get("addr")
					.toString());
			bundle.putString("table", communicate.table_jrla_activity_list);

			Intent intent = new Intent();
			intent.putExtras(bundle);
			intent.setClass(activity_main_activity.this, activity_detail.class);

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

			convertView = mInflater.inflate(R.layout.resultitem, null);

			PoiListItem item = (PoiListItem) convertView;

			Map map = filterData.get(position);
			// id+"|"+name+"|"+price+"|"+addr+"|"+star+"|"+latitude+"|"+longitude+"|"+aid+"~";

			item.setPoiData(map.get("name").toString(), map.get("price")
					.toString(), map.get("addr").toString(), ((int) Float
					.parseFloat(map.get("star").toString())), false, false,
					false, false);

			item.setDistanceText(map.get("distance").toString());

			if (position == filterData.size() - 1 && !isEnd) {
				loadingView.setVisibility(View.VISIBLE);
				if(activity_main_activity.is_collect==false){
					server.sendRequest(activity_main_activity.this);
					}
					else {
						server.sendcolRequest(activity_main_activity.this);
					}
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
		case R.id.id_area: {
			showDialogPopup(R.id.id_area);

			break;
		}
		case R.id.id_type: {
			showDialogPopup(R.id.id_type);
			break;
		}
		case R.id.id_sort: {
			showDialogPopup(R.id.id_sort);
			break;
		}
		case R.id.map_bn_login_back: {
			finish();
			break;
		}
		case R.id.bn_activity_col: {

//			Intent intent = new Intent(activity_main_activity.this,
//					activity_main_activity.class);
//			Bundle bundle = new Bundle();
//			bundle.putString("activity", "collect");
//			intent.putExtras(bundle);
//			startActivity(intent);
//			this.finish();

		}
		}

	}

	protected void showDialogPopup(int viewId) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);

		switch (viewId) {
		case R.id.id_area: {

			if (areaAdapter == null) {
				areaAdapter = new AreaAdapter(this);
			}

			localBuilder.setAdapter(areaAdapter, new areaPopupListener(
					areaAdapter));
			break;
		}

		case R.id.id_type: {
			if (cateAdapter == null) {
				cateAdapter = new CateAdapter(this);
			}
			localBuilder.setAdapter(cateAdapter,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			break;
		}

		case R.id.id_sort: {
			if (sortAdapter == null) {
				sortAdapter = new SortAdapter(this);
			}
			localBuilder.setAdapter(sortAdapter,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			break;
		}

		}

		AlertDialog localAlertDialog = localBuilder.create();
		localAlertDialog.show();
	}

	class areaPopupListener implements DialogInterface.OnClickListener {
		AreaAdapter mAdapter;

		public areaPopupListener(ListAdapter adapter) {
			mAdapter = (AreaAdapter) adapter;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (mAdapter.isTopLevel()) {
				((AreaAdapter) mAdapter).setTypeIndex(which);
				final String strSelect = ((AreaAdapter) mAdapter).getSelect();

				filterData = new ArrayList<Map<String, Object>>();
				((PoiResultAdapter) resultAdapter).notifyDataSetChanged();

				if (isLoadingRemoved) {
					list.addFooterView(loadingView);
					loadingView.setVisibility(View.VISIBLE);
					isLoadingRemoved = false;
				} else {
					loadingView.setVisibility(View.VISIBLE);
				}

				new Thread() {
					public void run() {

						try {
							Thread.sleep(1000);
						} catch (Exception e) {

						}

						Iterator iter = mData.iterator();

						while (iter.hasNext()) {

							Map<String, Object> map = (Map<String, Object>) iter
									.next();
							String strArea = map.get("area").toString();
							if (strArea.equalsIgnoreCase(strSelect)) {
								filterData.add((Map<String, Object>) iter
										.next());
							}
						}

						Message msg = new Message();
						msg.what = 2;
						handler.sendMessage(msg);

					}
				}.start();

			} else {
				if (which == 0) {
					((AreaAdapter) mAdapter).setTypeIndex(which);
					filterData.clear();
					mData.clear();
					filterData = mData;
					if (isLoadingRemoved) {
						list.addFooterView(loadingView);
						loadingView.setVisibility(View.VISIBLE);
						isLoadingRemoved = false;
					} else {
						loadingView.setVisibility(View.VISIBLE);
					}
					if(activity_main_activity.is_collect==false){
					server = new activity_item_proxy(getApplicationContext());
					server.sendRequest(activity_main_activity.this);
					}
					else {
						server = new activity_item_proxy(getApplicationContext());
						server.sendcolRequest(activity_main_activity.this);
					}
				}
			}

		}

	}

}