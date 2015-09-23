package com.jrla.client;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
//import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jrla.client.Message;

public class activity_chat extends Activity implements OnClickListener{
	EditText et_tony_message;
	Button but_tony_send;
	
	EditText et_id_message;
	EditText et_hill_message;
	Button but_hill_send;
	
	ImageView but_chat_back;
	
	ListView lv_message;
	List<Message> list;
	MessageAdapter adapter;
	Handler handler=new Handler();
	
	int way_message=3;
	
	private MyHandler handler_mian = null;  
//	static Handler handler_mian = null; 
	private static myapp mAPP = null;  
	    
	public void chat_init_handle(Context con) {
		// TODO Auto-generated constructor stub
		mAPP = (myapp)con.getApplicationContext(); 
    	handler_mian = new MyHandler();  
        mAPP.setHandler(handler_mian);
//		handler_mian = handler1;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_chat);
		init();
		
		
		mAPP = (myapp)getApplicationContext(); 
    	handler_mian = new MyHandler();  
        mAPP.setHandler(handler_mian);
//		handler_mian = handler1;
		mAPP.setHandler(handler_mian);
		

		
    	SharedPreferences sharedPreferences=getSharedPreferences("chat_info", MODE_PRIVATE);
    	String messageString=null;
    	messageString=sharedPreferences.getString("name", null);//默认0
    	Toast.makeText(activity_chat.this, messageString, Toast.LENGTH_LONG).show();
    	
    	show_receive_message(messageString);

		
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null&&bundle.getString("first_activity").equalsIgnoreCase("activity_welcome"))
		{
			Intent intent = new Intent();
	        intent.setClass(activity_chat.this, activity_main.class); 
	        startActivity(intent);
	        finish();
		}
		if(bundle!=null&&bundle.getString("first_activity").equalsIgnoreCase("activity_baidumap"))
		{
			
			et_id_message=(EditText)findViewById(R.id.et_id_message);
			et_id_message.setText(bundle.getString("to_user"));
		}
	}

	private void init() {
//		et_tony_message=(EditText) findViewById(R.id.et_tony_message);
//		but_tony_send=(Button) findViewById(R.id.but_tony_send);
//		but_tony_send.setOnClickListener(this);
		
		et_hill_message=(EditText) findViewById(R.id.et_hill_message);
		but_hill_send=(Button) findViewById(R.id.but_hill_send);
		but_hill_send.setOnClickListener(this);
		
		but_chat_back=(ImageView) findViewById(R.id.bn_chat_back);
		but_chat_back.setOnClickListener(this);
		
		list=new ArrayList<Message>();
		lv_message=(ListView) findViewById(R.id.lv_message);
		adapter=new MessageAdapter();
		lv_message.setAdapter(adapter);
		if(activity_login.spf_read_user(activity_chat.this)==null)
		{
			Intent intent = new Intent();
			intent.setClass(activity_chat.this, activity_login.class);
			startActivity(intent);
			activity_chat.this.finish();
		}
//		communicate.socket_send(communicate.server_ip,communicate.server_port,"2",communicate.function_receive_chat);
		
	}


	@Override
	public void onClick(View v) {
		
//		if(v==but_tony_send){
//			/****/
//			if(et_tony_message.getText()==null||et_tony_message.getText().toString().equals("")){
//				Toast.makeText(this, "请输入内容", 0).show();
//				return ;
//			}
//			Message m=new Message();
//			m.setFrom_username("Tony");
//			m.setCreate_time(System.currentTimeMillis());
//			m.setText(et_tony_message.getText().toString());
//			showMessage(m);
//			et_tony_message.setText("");
//		}
		if(v==but_hill_send){
			if(et_hill_message.getText()==null||et_hill_message.getText().toString().equals("")){
				Toast.makeText(this, "请输入内容", 0).show();
				return ;
			}
			Message m=new Message();
			m.setFrom_username(activity_login.spf_read_user_id(activity_chat.this));
			m.setCreate_time(System.currentTimeMillis());
			m.setText(et_hill_message.getText().toString());
			m.setdir(0);
			//测试发送
			sendMessage(activity_login.spf_read_user(activity_chat.this),activity_login.spf_read_user(activity_chat.this),m.getText());
		
			//真实发送
//			sendMessage(m.getFrom_username(),et_id_message.getText().toString(),m.getText());

			showMessage(m);
			et_hill_message.setText("");
		}
		if(v.getId()==R.id.bn_chat_back){
			activity_chat.this.finish();
		}
	}
	
	private void show_myself_message(String str)
	{
		
	}
	
	private void show_receive_message(String messageString)
	{
		Message m =new Message();
    	
    	messageString=messageString.substring(8);//截取子串
		String[] str = messageString.split("\\|");//分割字符串
		
    	m.setFrom_username(str[0]);
    	m.setCreate_time(Long.parseLong(str[2]));
    	m.setText(str[3]);
    	m.setdir(1);
    	showMessage(m);
	}
	private void showMessage(Message m) {
		list.add(m);
		adapter.notifyDataSetChanged();
		lv_message.setSelection(list.size()+1);
	}
	
	private void sendMessage(String from_id,String to_id,String Message) {
//		list.add(m);
//		adapter.notifyDataSetChanged();
//		lv_message.setSelection(list.size()+1);
		String time_now=System.currentTimeMillis()+"";
		String text=Message;
		String from=from_id;
		String to=to_id;
		String send_buff=from_id+"|"+to_id+"|"+time_now+"|"+text;
		communicate.socket_send(communicate.server_ip,communicate.server_port,send_buff,communicate.function_chat);
	}
	
	private void receiveMessage(String message) {
		
		Message mess=new Message();
		
		String[] str = message.split("\\|");//分割字符串
		mess.setFrom_username(str[0]);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmm");
//		long millionSeconds = sdf.parse(str).getTime();//毫秒
		
		mess.setCreate_time(Long.parseLong(str[2]));
		mess.setText(et_hill_message.getText().toString());
		
		list.add(mess);
		adapter.notifyDataSetChanged();
		lv_message.setSelection(list.size()+1);
	}
	
	class MessageAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Message message=list.get(position);
			ViewHolder viewHolder=null;
//		if(convertView==null){
			if(message.getdir()==1){
				convertView=parent.inflate(activity_chat.this, R.layout.list_message_item_left, null);
			}else{
				convertView=parent.inflate(activity_chat.this, R.layout.list_message_item_right, null);
			}
			viewHolder=new ViewHolder();
			viewHolder.iv_userhead=(ImageView) convertView.findViewById(R.id.iv_userhead);
			viewHolder.tv_chatcontent=(TextView) convertView.findViewById(R.id.tv_chatcontent);
			viewHolder.tv_sendtime=(TextView) convertView.findViewById(R.id.tv_sendtime);
			viewHolder.tv_username=(TextView) convertView.findViewById(R.id.tv_username);
//			convertView.setTag(viewHolder);
//		}else{
//			viewHolder=(ViewHolder) convertView.getTag();
//		}
			
			viewHolder.tv_chatcontent.setText(message.getText());
			viewHolder.tv_sendtime.setText(new SimpleDateFormat("HH:mm:ss").format(new Date(message.getCreate_time())));
			viewHolder.tv_username.setText(message.getFrom_username());
			return convertView;
		}
		class ViewHolder{
			public ImageView iv_userhead;
			public TextView tv_username;
			public TextView tv_chatcontent;
			public TextView tv_sendtime;
		}
	}
	
	
	
	 final class MyHandler extends Handler {  
		
	        public void handleMessage(android.os.Message msg) {  
	            super.handleMessage(msg);  
	            if(msg.what == 3) { // 更新UI 
	            	Toast.makeText(activity_chat.this, "null", Toast.LENGTH_LONG).show();
//	            	Toast.makeText(activity_chat.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
//	            	Log.d("111", "null");
	            	
	            	show_receive_message(msg.obj.toString());
	            }  
	        }  
	    }  
}
