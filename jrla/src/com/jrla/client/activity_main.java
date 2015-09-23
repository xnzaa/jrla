package com.jrla.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jrla.client.activity_main_food.PoiResultAdapter;
import com.jrla.widget.PoiListItem;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class activity_main extends Activity implements OnClickListener,ServerListener {

	private SlideMenu slideMenu;
	private TextView tv_menu_id;
	private TextView tv_menu_center;
	private TextView tv_menu_favorite;
	private TextView tv_menu_about_us;
	private TextView tv_menu_setting;
	private TextView tv_menu_exit;
	private RelativeLayout rl_menu_id;
	GridView gv_main = null;
	private Button bn_together_voice;
	private Button bn_together_choice;
	private Button bn_together_map;
	private Button bn_search;
	private EditText et_search;
	TextView search_tv_location;

	private final static int REQUEST_CODE = 0;
	private final static int RESULT_LOGIN = 1;
	private final static int RESULT_REGISTER = 2;
	private static String str_login_user = null;
	OnItemClickListener ocl_gridview = null;
	// ����һ������������ʶ�Ƿ��˳�
	private static boolean isExit = false;
	public static String location = null;

	private Thread thread;

//	private MyHandler handler_mian = null;  
	private Handler handler_mian = null;  
	private myapp mAPP = null; 
	
	
	private main_item_proxy main_item_proxy=null;
	
	
	
	private List<Map<String, Object>> mData;
	private List<Map<String, Object>> filterData;
	private View loadingView;
	private ListView list;
	ListAdapter resultAdapter = null;
	private boolean isEnd = false;
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);

		slideMenu = (SlideMenu) findViewById(R.id.slide_menu);
		ImageView menuImg = (ImageView) findViewById(R.id.title_bar_menu_btn);
		menuImg.setOnClickListener(this);

		tv_menu_id = (TextView) findViewById(R.id.tv_menu_id);
		tv_menu_id.setOnClickListener(activity_main.this);

		tv_menu_center = (TextView) findViewById(R.id.tv_menu_center);
		tv_menu_center.setOnClickListener(activity_main.this);

		tv_menu_about_us = (TextView) findViewById(R.id.tv_menu_about_us);
		tv_menu_about_us.setOnClickListener(activity_main.this);

		tv_menu_setting = (TextView) findViewById(R.id.tv_menu_setting);
		tv_menu_setting.setOnClickListener(activity_main.this);

		tv_menu_exit = (TextView) findViewById(R.id.tv_menu_exit);
		tv_menu_exit.setOnClickListener(activity_main.this);

		rl_menu_id = (RelativeLayout) findViewById(R.id.rl_menu_id);
		rl_menu_id.setOnClickListener(activity_main.this);

		bn_together_voice = (Button) findViewById(R.id.main_together_voice);
		bn_together_voice.setOnClickListener(activity_main.this);
		bn_together_choice = (Button) findViewById(R.id.main_together_choice);
		bn_together_choice.setOnClickListener(activity_main.this);
		bn_together_map = (Button) findViewById(R.id.main_together_map);
		bn_together_map.setOnClickListener(activity_main.this);
		bn_search= (Button) findViewById(R.id.search_bn_search);
		bn_search.setOnClickListener(activity_main.this);
		et_search=(EditText)findViewById(R.id.et_search);
		
		search_tv_location = (TextView) findViewById(R.id.search_tv_location);

		
		 mData = PoiResultData.getData();
	        filterData = mData;
	        
	        list = (ListView)findViewById(R.id.resultlist);
	        //list.setOnItemClickListener(mOnClickListener);
	        resultAdapter = new PoiResultAdapter(this);
	        list.setAdapter(resultAdapter);
	        list.setOnItemClickListener(mOnClickListener);
		
		 main_item_proxy=new main_item_proxy(getApplicationContext());
		 main_item_proxy.sendRequest(this);
		// bn_together_voice.bringToFront();
		// bn_together_voice.setVisibility(View.GONE);
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		boolean GPS_status = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);// ����ֻ��ǲ���������GPS����״̬true��gps������false��GPSδ����
		if (!GPS_status) {
			Intent gps_dialog = new Intent(this, activity_gps_dialog.class);
			startActivity(gps_dialog);
		}
		// boolean NETWORK_status =
		// locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);//
		// ��һ��Gpsprovider��Google��·��ͼ��

		gv_main = (GridView) findViewById(R.id.gv_main);
		gv_main.setAdapter(new ImageAdapter(this));

		gv_main.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Intent aim_activity = null;
				Bundle bundle = null;
				switch (position) {
				case 0:
					Toast.makeText(activity_main.this, "0", Toast.LENGTH_SHORT)
							.show();
					// aim_activity = new Intent(activity_main.this,
					// activity_main_food.class);
					aim_activity = new Intent(activity_main.this,
							activity_main_food.class);
					bundle = new Bundle();
					bundle.putString("val", "1->1");
					aim_activity.putExtras(bundle);
					startActivityForResult(aim_activity, REQUEST_CODE);
					break;
				case 1:
					
					ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
					ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
					Toast.makeText(activity_main.this,cn.getClassName(),Toast.LENGTH_SHORT)					.show();
						
					Toast.makeText(activity_main.this, "1", Toast.LENGTH_SHORT)
							.show();
					aim_activity = new Intent(activity_main.this,
							activity_main_activity.class);
//					bundle = new Bundle();
//					bundle.putString("val", "1->1");
//					aim_activity.putExtras(bundle);
					startActivityForResult(aim_activity, REQUEST_CODE);
					break;
				case 2:
					Toast.makeText(activity_main.this, "2", Toast.LENGTH_SHORT).show();
					aim_activity = new Intent(activity_main.this,
							activity_main_gift.class);
//					bundle = new Bundle();
//					bundle.putString("val", "1->1");
//					aim_activity.putExtras(bundle);
					startActivityForResult(aim_activity, REQUEST_CODE);
					break;
				case 3:
					Toast.makeText(activity_main.this, "3", Toast.LENGTH_SHORT)
							.show();
					aim_activity = new Intent(activity_main.this,
							activity_main_explain.class);
//					aim_activity = new Intent(activity_main.this,
//							activity_explain_detail.class);
//					bundle = new Bundle();
//					bundle.putString("val", "1->1");
//					aim_activity.putExtras(bundle);
					startActivityForResult(aim_activity, REQUEST_CODE);
					break;
				}
			}
		});
		gv_main.setSelector(new ColorDrawable(Color.TRANSPARENT));

		new get_location(activity_main.this, "activity_main");
		thread = new Thread(runnable);
		thread.start();
		
		communicate.socket_send(communicate.server_ip, communicate.server_port, activity_login.spf_read_user_id(activity_main.this), communicate.function_send_ip);
		 DataReceiver myReceiver = new DataReceiver();  
         IntentFilter filter = new IntentFilter();  
         filter.addAction("com.jrla.client");   
         registerReceiver(myReceiver, filter); 
	}

	private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position,long id) {
			Bundle bundle=new Bundle();
			
			bundle.putString("id", filterData.get(position).get("id").toString());
			bundle.putString("name", filterData.get(position).get("name").toString());
			bundle.putString("price", filterData.get(position).get("price").toString());
			bundle.putString("addr", filterData.get(position).get("addr").toString());
			if(position==1){
			bundle.putString("xid", filterData.get(position).get("fid").toString());
			bundle.putString("table", "jrla_food_list");
			}
			else if(position==0)
			{
				bundle.putString("xid", filterData.get(position).get("aid").toString());
				bundle.putString("table", "jrla_activity_list");
			}
			else if(position==2)
			{
				bundle.putString("xid", filterData.get(position).get("gid").toString());
				bundle.putString("table", "jrla_gift_list");
			}
			
			Intent intent = new Intent();
			intent.putExtras(bundle);
			intent.setClass(activity_main.this, activity_detail.class);
			startActivity(intent);
//			activity_main_food.this.finish();
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

		if(convertView == null)
		{
			Log.v("is NULL","DF2"+position);
		}
		
		Log.v("ListViewLog","DF"+position);
		
		convertView = mInflater.inflate(R.layout.resultitem, null);
		
		PoiListItem item = (PoiListItem) convertView;

		Map map = filterData.get(position);

		item.setPoiData(map.get("name").toString(), map.get("price")
				.toString(), map.get("addr").toString(), ((int)Float.parseFloat(map
				.get("star").toString())), false, false, false,false);

		item.setDistanceText(map.get("distance").toString());
		
		if(position == filterData.size() -1 && !isEnd )
		{
			loadingView.setVisibility(View.VISIBLE);
			main_item_proxy.sendRequest(activity_main.this);
		}

		return convertView;
	}

	}
	@Override
	public void onClick(View v) {
		Intent aim_activity = null;
		Bundle bundle = null;
		switch (v.getId()) {
		case R.id.title_bar_menu_btn:
			if (slideMenu.isMainScreenShowing()) {
				slideMenu.openMenu();

			} else {
				slideMenu.closeMenu();
			}
			break;
		case R.id.tv_menu_id:
			if (activity_login.spf_read_user(activity_main.this) != null) {

			} else {
				aim_activity = new Intent(this, activity_login.class);
				startActivity(aim_activity);
			}
			break;
		case R.id.rl_menu_id:
			if (activity_login.spf_read_user(activity_main.this) != null) {

			} else {
				aim_activity = new Intent(this, activity_login.class);
				startActivity(aim_activity);
			}
			break;
		case R.id.tv_menu_center:
			if (activity_login.spf_read_user(activity_main.this) != null) {
				aim_activity = new Intent(activity_main.this,
						activity_user_center.class);
				bundle = new Bundle();
				bundle.putString("val", "1->1");
				aim_activity.putExtras(bundle);
				startActivityForResult(aim_activity, REQUEST_CODE);
			} else {
				aim_activity = new Intent(activity_main.this,
						activity_login.class);
				bundle = new Bundle();
				bundle.putString("val", "1->1");
				aim_activity.putExtras(bundle);
				startActivityForResult(aim_activity, REQUEST_CODE);
			}

			break;
		case R.id.tv_menu_setting:
			aim_activity = new Intent(activity_main.this,
					activity_setting.class);
			startActivity(aim_activity);
			break;
		case R.id.tv_menu_about_us:
				aim_activity = new Intent(this, activity_about_us.class);
				startActivity(aim_activity);
			break;
		case R.id.tv_menu_exit:

			activity_login.spf_del_user(activity_main.this);

			if (activity_login.spf_read_user(activity_main.this) == null) {
				Toast.makeText(getApplicationContext(), "���Ѿ��ɹ��˳�",
						Toast.LENGTH_SHORT).show();
			}
			tv_menu_id.setText("���¼");

			break;
		case R.id.main_together_choice:
			if (bn_together_voice.getText() != "") {
				bn_together_choice.setBackgroundResource(R.drawable.home_addv);
				bn_together_voice
						.setBackgroundResource(R.drawable.home_edittext);
				bn_together_voice.setText("");
				
			} else {
				bn_together_choice.setBackgroundResource(R.drawable.home_add);
				bn_together_voice.setBackgroundResource(R.drawable.home_voice);
				bn_together_voice.setText("�Ҹ��������ѹ���");
			}
			
//			 Intent intent = new Intent();  
//             intent.setAction("com.jrla.CLIENT");  
//             intent.putExtra("msg", "���ǹ㲥���͵���Ϣ"); 
// 			 intent.setPackage("com.jrla.client"); 
//             sendBroadcast(intent); 
			
			break;
		case R.id.main_together_voice:

			aim_activity = new Intent(activity_main.this, activity_chat.class);
			startActivity(aim_activity);
			break;
		case R.id.main_together_map:

			aim_activity = new Intent(this, activity_baidumap.class);
			startActivity(aim_activity);
			break;
		case R.id.search_bn_search:
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri uri = Uri.parse("http://www.baidu.com/s?wd="+ Uri.encode(et_search.getText().toString()));
			intent.setData(uri);
			startActivity(intent);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE) {
			if (resultCode == RESULT_LOGIN)
				Toast.makeText(activity_main.this, "���Ǵ�cancel���������Ӧ",
						Toast.LENGTH_LONG).show();
			else if (resultCode == RESULT_REGISTER) {
				String temp = null;
				Bundle bundle = data.getExtras();
				if (bundle != null)
					temp = bundle.getString("val");
				Toast.makeText(activity_main.this, "�� ��ok���������Ӧ:" + temp,
						Toast.LENGTH_LONG).show();
			}
		}
	}

	public static void set_user(String str) {
		str_login_user = str;
	}

	public static String get_user() {
		return str_login_user;
	}

	@Override
	protected void onDestroy() {// Actvity�ͷ�ʱ������
		super.onDestroy();
		communicate.socket_send(communicate.server_ip,
					communicate.server_port, activity_login.spf_read_user_id(activity_main.this), communicate.function_client_down);

	}
	protected void onStart() {
		super.onStart();
		if (activity_login.spf_read_user(activity_main.this) != null) {
			SharedPreferences sharedPreferences_read = getSharedPreferences(
					"login_info", MODE_PRIVATE);
			String name = sharedPreferences_read.getString("name", null);// Ĭ��0
			tv_menu_id.setText(name);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����",
					Toast.LENGTH_LONG).show();
			// ����handler�ӳٷ��͸���״̬��Ϣ
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			finish();
			//System.exit(0);
		}
	}

	// imageview ����
	public class ImageAdapter extends BaseAdapter {
		private Context mContext;

		public ImageAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mThumbIds.length;
		}

		public Object getItem(int position) {
			return mThumbIds[position];
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			View view = View.inflate(mContext, R.layout.layout_gvitem_main,
					null);
			RelativeLayout rl = (RelativeLayout) view
					.findViewById(R.id.rl_gvitem_main);

			ImageView image = (ImageView) rl.findViewById(R.id.iv_gvitem_main);
			TextView text = (TextView) rl.findViewById(R.id.tv_gvitem_main);
			image.setImageResource(mThumbIds[position]);
			text.setText(mTextValues[position]);

			return rl;
		}

		// references to our images
		private Integer[] mThumbIds = { R.drawable.main_food_2,
				R.drawable.main_activity_2, R.drawable.main_gift_2,
				R.drawable.main_explain_2, };
		private String[] mTextValues = { "������ʳ", "���׻", "��ɫ����", "�Ļ�����" };
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			System.out.println("runnable");
			switch (msg.what) {
			case 1:
				search_tv_location.setText(msg.obj.toString());
				// Toast.makeText(activity_main.this, "111",
				// Toast.LENGTH_SHORT).show();
				break;
			case 2:

				SharedPreferences sharedPreferences = getSharedPreferences(
						"chat_info", MODE_PRIVATE);
				// name=sharedPreferences.getString("name", null);//Ĭ��0
				Editor editor = sharedPreferences.edit();
				editor.putString("name", msg.obj.toString());// �洢string����
				editor.commit();
				if (!isTopActivity()) {
					Toast.makeText(activity_main.this,
							"1:" + msg.obj.toString(), Toast.LENGTH_SHORT)
							.show();
					NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					// ����һ��֪ͨ����(��Ҫ���ݵĲ���������,�ֱ���ͼ��,����� ʱ��)
					Notification notification = new Notification(
							R.drawable.ic_launcher, "֪ͨ",
							System.currentTimeMillis());
					Intent intent = new Intent(activity_main.this,
							activity_chat.class);
					PendingIntent pendingIntent = android.app.PendingIntent
							.getActivity(activity_main.this, 0, intent, 0);
					notification.setLatestEventInfo(getApplicationContext(),
							"λ���罻", "������������Ϣ����ע��鿴", pendingIntent);
					notification.flags = Notification.FLAG_AUTO_CANCEL;// ������Զ���ʧ
					notification.defaults = Notification.DEFAULT_SOUND;// ����Ĭ��
					notification.ledARGB = Color.BLUE;
					notification.ledOnMS = 5000; // ����ʱ�䣬����
					manager.notify(1, notification);// ����֪ͨ,id���Լ�ָ����ÿһ��Notification��Ӧ��Ψһ��־
				}
				else {
					 mAPP = (myapp)getApplicationContext();
				     // ��øù������ʵ��  
//				     activity_chat.chat_init_handle(activity_main.this); 
					 handler_mian = mAPP.getHandler();  
//					 handler_mian = new MyHandler();  
//				     mAPP.setHandler(handler_mian);
					 handler_mian.obtainMessage(3, msg.obj.toString()).sendToTarget();
				}
			}
		}
	};

	private boolean isTopActivity() {
		boolean isTop = false;
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		if (cn.getClassName().contains("activity_chat")) {
			isTop = true;
		}
		return isTop;
	}

	Runnable runnable = new Runnable() {
		public void run() {
			while (true) {
				if (location != null) {
					handler.obtainMessage(1, location).sendToTarget();
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				new Thread() {
					public void run() {

						ServerSocket ss = null;// ServerSocket������
						Socket sc = null;
						// Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);
						while (true) {// ������ѭ��
							try {
								ss = new ServerSocket(9998);
								sc = ss.accept();
								// ����ʽ�������ȴ��û�����//System.out.println("==========ServerThread============");

								thread sa = new thread(sc);// ���������߳�
								sa.start();// ���������߳�
							} catch (Exception e) {// �����쳣
								e.printStackTrace();// ��ӡ�쳣
							}
							try {
								if (ss != null) {
									ss.close();
									ss = null;
								}
							} catch (Exception el) {
								el.printStackTrace();
							}
						}
					}
				}.start();
			}
		}
	};

	public class thread extends Thread {
		Socket sc;// ����Socket������
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
					if (msg.startsWith("<#chat#>")) {// ��¼����
					//
						handler.obtainMessage(2, msg).sendToTarget();
						// Thread.sleep(1000);
					}
					if (msg.startsWith("<#jrla_explain_list_max#>")) {// ��¼����
						//
						 Intent intent = new Intent();  
			             intent.setAction("com.jrla.client");  
			             intent.putExtra("msg", "���ǹ㲥���͵���Ϣ");  
			             sendBroadcast(intent); 
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
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
			}
		}
	}
	private  class DataReceiver extends BroadcastReceiver {// �̳���BroadcastReceiver������

		public void onReceive(Context context, Intent intent) {// ��дonReceive����
			
				// Bundle bundle = intent.getExtras();
				// //�����л����ڱ����ؽ�����
				// Serializable data = bundle.getSerializable("weibodata");
				// if (data != null) {
				//
				// Toast.makeText(context, "broad", Toast.LENGTH_LONG).show();
				Toast.makeText(
						context,
						"���յ���Intent��Actionλ��" + intent.getAction() + "\n��Ϣ�����ǣ�"
								+ intent.getStringExtra("msg"),
						Toast.LENGTH_LONG).show();
				// } else {
				// return;
				// }
		}
}
	@Override
	public void serverDataArrived(List list, boolean isEnd) {
		this.isEnd = isEnd;
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			mData.add((Map<String, Object>) iter.next());
		}
	}

}
