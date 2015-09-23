package com.jrla.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class activity_user_center extends Activity implements OnClickListener {

	ImageView bn_back = null;
	EditText et_id = null;
	EditText et_passwd = null;
	EditText et_qq = null;
	EditText et_email = null;
	EditText et_others = null;
	EditText et_hobby = null;

	Button bn_edit = null;
	Button bn_cancel = null;

	
	Handler handler = new Handler() 
	{
		public void handleMessage(Message paramMessage) 
		{
			if(paramMessage.what == 1)
			{

				String detail_message=paramMessage.obj.toString();
				String data[]=detail_message.split("\\|");
//				uid+"|"+u_pwd+"|"+u_qq+"|"+u_email+"|"+u_custom+"|"+u_hobby;
				et_id.setText(data[0]);
				et_passwd.setText(data[1]);
				et_qq.setText(data[2]);
				et_email.setText(data[3]);
				et_others.setText(data[4]);
				et_hobby.setText(data[5]);
				Toast.makeText(activity_user_center.this, "111", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	static String data_detailString="";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_user_center);
		
		
		bn_back = (ImageView) findViewById(R.id.bn_back);
		bn_back.setOnClickListener(this);

		et_id = (EditText) findViewById(R.id.et_user_id);
		et_passwd = (EditText) findViewById(R.id.et_user_passwd);
		et_email = (EditText) findViewById(R.id.et_user_email);
		et_others = (EditText) findViewById(R.id.et_user_others);
		et_hobby = (EditText) findViewById(R.id.et_user_hobby);
		et_qq = (EditText) findViewById(R.id.et_user_qq);

		bn_edit = (Button) findViewById(R.id.bn_user_edit);
		bn_edit.setOnClickListener(this);
		bn_cancel = (Button) findViewById(R.id.bn_user_cancel);
		bn_cancel.setOnClickListener(this);
		
		et_id.setEnabled(false);
		et_id.setLongClickable(false);
		et_passwd.setEnabled(false);
		et_passwd.setLongClickable(false);
		et_email.setEnabled(false);
		et_email.setLongClickable(false);
		et_others.setEnabled(false);
		et_others.setLongClickable(false);
		et_hobby.setEnabled(false);
		et_hobby.setLongClickable(false);
		et_qq.setEnabled(false);
		et_qq.setLongClickable(false);
		
		 new Thread()
	        {
	        	public void run()
	        	{
	        		data_detailString="";
	        		data_detailString=get_user_info(activity_login.spf_read_user(activity_user_center.this));

	        		handler.obtainMessage(1, data_detailString).sendToTarget();
	        	}
	        }.start();
	}

	// et_search.setInputType(0x00000081);

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bn_back:
			finish();
			break;
		case R.id.bn_user_edit:

			if (bn_edit.getText().toString().equalsIgnoreCase("修改信息")) {
				
				et_passwd.setEnabled(true);
				et_passwd.setLongClickable(true);
				et_qq.setEnabled(true);
				et_qq.setLongClickable(true);
				et_email.setEnabled(true);
				et_email.setLongClickable(true);
				et_others.setEnabled(true);
				et_others.setLongClickable(true);
				et_hobby.setEnabled(true);
				et_hobby.setLongClickable(true);

				bn_edit.setText("提交修改");
			} else if (bn_edit.getText().toString().equalsIgnoreCase("提交修改")) {
				
				String infostr=et_id.getText().toString()+"|"+et_passwd.getText().toString()
						+"|"+et_qq.getText().toString()
						+"|"+et_email.getText().toString()+"|"+et_others.getText().toString()
						+"|"+et_hobby.getText().toString();
				communicate.socket_send(communicate.server_ip, communicate.server_port, infostr, communicate.function_update_user_info);
				
				
				
				et_id.setEnabled(false);
				et_id.setLongClickable(false);
				et_passwd.setEnabled(false);
				et_passwd.setLongClickable(false);
				et_email.setEnabled(false);
				et_email.setLongClickable(false);
				et_others.setEnabled(false);
				et_others.setLongClickable(false);
				et_hobby.setEnabled(false);
				et_hobby.setLongClickable(false);
				et_qq.setEnabled(false);
				et_qq.setLongClickable(false);
				bn_edit.setText("修改信息");
				
			}
			break;

		case R.id.bn_user_cancel:
			et_id.setEnabled(false);
			et_id.setLongClickable(false);
			et_passwd.setEnabled(false);
			et_passwd.setLongClickable(false);
			et_email.setEnabled(false);
			et_email.setLongClickable(false);
			et_others.setEnabled(false);
			et_others.setLongClickable(false);
			et_hobby.setEnabled(false);
			et_hobby.setLongClickable(false);
			et_qq.setEnabled(false);
			et_qq.setLongClickable(false);
			 new Thread()
		        {
		        	public void run()
		        	{
		        		data_detailString="";
		        		data_detailString=get_user_info(activity_login.spf_read_user(activity_user_center.this));

		        		handler.obtainMessage(1, data_detailString).sendToTarget();
		        	}
		        }.start();
		        
			bn_edit.setText("修改信息");
			break;

		}
	}
	
	
	

	static String get_data_user_return = null;
	static Boolean received=false;
	
	private String get_user_info(final String id) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		received = false;
		new Thread() {// 将耗时的动作放到线程中
			Socket s=null;
			DataOutputStream dout=null;
			DataInputStream din=null;
			public void run() {// 重写的run方法
				try {
					s = new Socket(communicate.server_ip, communicate.server_port);
					dout = new DataOutputStream(s.getOutputStream());
					din = new DataInputStream(s.getInputStream());
					dout.writeUTF("<#get_data#>jrla_user" + "|"+id);// 通知服务器客户端退出
						String msg = din.readUTF();// 接收服务器发来的消息
						Log.e("sbs", msg);
						if (msg.startsWith("<#get_data#>")) {// 得到数据
							msg = msg.substring(12);

							get_data_user_return = msg;
						} else if (msg.startsWith("<#get_data_error#>")) {// 空数据数据

							get_data_user_return = null;
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
		while (!received);
		if (get_data_user_return == null)
			return "null";
		else
			return get_data_user_return;
	
	}
}
