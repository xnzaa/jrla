package com.jrla.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class activity_login extends Activity {
	
	
	EditText login_et_user_name=null;
	EditText login_et_user_passwd=null;
	ImageView bn_back=null;
	Button bn_login=null;
	Button bn_register=null;
			
	Socket s = null;//声明Socket的引用
	DataOutputStream dout = null;//输出流
	DataInputStream din = null;//输入流	
	ProgressDialog myDialog = null;//进度框
	String name;
	String passwd;
	private final static int RESULT_LOGIN = 1;
	private final static int RESULT_REGISTER = 2;
	public activity_login() {
		// TODO Auto-generated constructor stub
	}
	
	
	Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 1){
				Toast.makeText(activity_login.this, "用户名或密码错误！", Toast.LENGTH_LONG).show();
			}
		}
	};
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
   	//积极 灵活 高效
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_login);  

		bn_back=(ImageView)findViewById(R.id.bn_login_back);
		bn_login=(Button)findViewById(R.id.bn_login);
		bn_register=(Button)findViewById(R.id.bn_register);
		login_et_user_name=(EditText)findViewById(R.id.login_et_user_name);
		login_et_user_passwd=(EditText)findViewById(R.id.login_et_user_passwd);
		OnClickListener listener=new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch(v.getId()){
				case R.id.bn_login_back:
					finish();
					break;
				case R.id.bn_login:
					name= login_et_user_name.getText().toString();
					passwd= login_et_user_passwd.getText().toString();
					
					if(name.trim().equals("")){//当用户名为空时 
						Toast.makeText(activity_login.this, "请您输入用户名！", Toast.LENGTH_SHORT).show();
						return;
					}
					if(passwd.trim().equals("")){//当密码为空时
						Toast.makeText(activity_login.this, "请您输入密码！", Toast.LENGTH_SHORT).show();
						return;
					}
//					myDialog = ProgressDialog.show(activity_login.this, "进度", "正在加载...",true);
					final Toast toast = Toast.makeText(getApplicationContext(),"正在加载..." , Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP|Gravity.CENTER, -50, 100);  
					toast.show();
					new Thread(){//将耗时的动作放到线程中
						public void run(){//重写的run方法
							try {
								s = new Socket(communicate.server_ip, communicate.server_port);//连接服务器
								dout = new DataOutputStream(s.getOutputStream());
								din = new DataInputStream(s.getInputStream());
								 
								System.out.println("<#LOGIN#>"+name+"|"+passwd);
								dout.writeUTF("<#LOGIN#>"+name+"|"+passwd);
								String msg = din.readUTF();//接收服务器发来的消息
								if(msg.startsWith("<#LOGINOK#>")){//登录成功

									spf_write_user(getApplicationContext(),name);
									activity_main.set_user("111");
									msg=msg.substring(11);
									Intent intent = new Intent();
									intent.setClass(activity_login.this, activity_main.class);
									Bundle bundle = new Bundle();
									bundle.putString("name", name);
									intent.putExtras(bundle);
									setResult(RESULT_LOGIN, intent);
									activity_login.this.finish();
								}
								else if(msg.startsWith("<#LOGINERROR#>")){//登录失败
									myHandler.sendEmptyMessage(1);
								}
							} catch (Exception e) {//捕获异常
								e.printStackTrace();//打印异常
							} finally{//使用finally保证之后的语句一定被执行
								try{
									if(din != null){
										din.close();
										din = null;
									}
								}
								catch(Exception e){
									e.printStackTrace();
								}
								try{
									if(dout != null){
										dout.close();
										dout = null;
									}							
								}
								catch(Exception e){
									e.printStackTrace();
								}
								try{
									if(s != null){
										s.close();
										s = null;
									}							
								}
								catch(Exception e){
									e.printStackTrace();
								}	
								toast.cancel();
//								myDialog.dismiss();
//								finish();
							}
						}
					}.start();
//					if(cb.isChecked()){
//						rememberMe(uid.getText().toString().trim(),pwd.getText().toString().trim());
//					}
					break;
//					Intent intent=getIntent();
//                    Bundle bd=new Bundle();
//                    bd.putString("val", "This is from ShowMsg!");
//                    intent.putExtras(bd);
//                    setResult(RESULT_LOGIN, intent);
//                    finish();// finish本Activity
				case R.id.bn_register:
					

					name= login_et_user_name.getText().toString();
					passwd= login_et_user_passwd.getText().toString();
					if(name.trim().equals("")){//当用户名为空时
						Toast.makeText(activity_login.this,"用户名不能为空!",Toast.LENGTH_SHORT).show();
						return;
					}
					else if(passwd.trim().equals("")){//密码不能为空
						Toast.makeText(activity_login.this,"密码不能为空!",Toast.LENGTH_SHORT).show();
						return;
					}
					
//					myDialog = ProgressDialog.show(activity_login.this, "进度", "正在加载...",true);
					Toast.makeText(activity_login.this,"正在加载..." , Toast.LENGTH_LONG);
					new Thread(){
						public void run(){
							try{//连接网络并打开流
						        s = new Socket(communicate.server_ip, communicate.server_port);
						        dout = new DataOutputStream(s.getOutputStream());
						        din = new DataInputStream(s.getInputStream());
							}catch(Exception e){//捕获异常
								e.printStackTrace();//打印异常
							}
							String u_name = name;//用户名
							String u_qq	= "";//qq号
							String u_pwd1 = passwd;//密码
							String u_Email = "";//邮件
							String u_dis = "";//描述
							String msg = "<#REGISTER#>"+u_name+"|"+u_pwd1+"|"+u_qq+"|"+u_Email+"|"+u_dis;
							try {
								dout.writeUTF(msg);//向服务器发送注册消息
								String msg2 = din.readUTF();//接收服务器发送来的消息
								if(msg2.startsWith("<#REGISTEROK#>")){//注册成功
									msg2=msg2.substring(14);//截取字串
									String[] str = msg2.split("\\|");//分割字符串

									Intent intent = new Intent();
									intent.setClass(activity_login.this, activity_main.class);
									
									Bundle data = new Bundle();//创建数据
									data.putString("uid", str[0]);//向bundle中添加数据
									data.putString("u_name", str[1]);//向bundle中添加数据
									
									intent.putExtras(data);//将bundle存放到Intent中
									startActivity(intent);//启动MainActivity
									activity_login.this.finish();	//释放当前的Activity
								}
							} catch (IOException e) {//捕获异常
								e.printStackTrace();//打印异常
							} finally{
								try{
									if(dout != null){
										dout.close();
										dout = null;
									}
								}
								catch(Exception e){//捕获异常
									e.printStackTrace();//打印异常信息
								}
								try{
									if(din != null){
										din.close();
										din = null;
									}
								}
								catch(Exception e){//捕获异常
									e.printStackTrace();//打印异常信息
								}
								try{
									if(s != null){
										s.close();
										s = null;
									}
								}
								catch(Exception e){//捕获异常
									e.printStackTrace();//打印异常信息
								}
//								myDialog.dismiss();
							}				
						}
					}.start();

					break;
				}
//					Toast.makeText(activity_login.this, activity_main.get_user(), Toast.LENGTH_SHORT).show() ;
//					Intent intent_r=getIntent();
//                    Bundle bd_r=new Bundle();
//                    bd_r.putString("val", "This is from ShowMsg!");
//                    intent_r.putExtras(bd_r);
//                    setResult(RESULT_REGISTER, intent_r);
//                    finish();// finish本Activity
				}
			};
		
		bn_back.setOnClickListener(listener);
		bn_login.setOnClickListener(listener);
		bn_register.setOnClickListener(listener);
    };
    
    public static void spf_write_user_id(Context ct,String id)
    {
    	SharedPreferences sharedPreferences=ct.getSharedPreferences("login_info", MODE_PRIVATE);
        Editor editor=sharedPreferences.edit();
		//三、通过Editor对象存储key-value键值对数据。
//		Set<String> values=new HashSet<String>(); 		
		editor.putString("name", id);//存储string类型
		// 四、通过commit()方法提交数据。
		editor.commit();
    }
    
    public static void spf_write_user(Context ct,String name)
    {
    	SharedPreferences sharedPreferences=ct.getSharedPreferences("login_info", MODE_PRIVATE);
        Editor editor=sharedPreferences.edit();
		//三、通过Editor对象存储key-value键值对数据。
//		Set<String> values=new HashSet<String>(); 		
		editor.putString("name", name);//存储string类型
		// 四、通过commit()方法提交数据。
		editor.commit();
    }
    
    public static String spf_read_user(Context ct)
    {
    	String name=null;
    	SharedPreferences sharedPreferences_read=ct.getSharedPreferences("login_info", MODE_PRIVATE);
		name=sharedPreferences_read.getString("name", null);//默认0
//		if(name==null)
//			return false;
//		else {
//			return true;
//		}
		return name;
    }
    
    public static String spf_read_user_id(Context ct)
    {
    	String name=null;
    	SharedPreferences sharedPreferences_read=ct.getSharedPreferences("login_info", MODE_PRIVATE);
		name=sharedPreferences_read.getString("name", null);//默认0
//		if(name==null)
//			return false;
//		else {
//			return true;
//		}
		return name;
    }
    
    
//    public static String spf_read_user_id()
//    {
//    	String name=null;
//    	SharedPreferences sharedPreferences_read=activity_login.this.getSharedPreferences("login_info", MODE_PRIVATE);
//		name=sharedPreferences_read.getString("name", null);//默认0
////		if(name==null)
////			return false;
////		else {
////			return true;
////		}
//		return name;
//    }
    
    public static boolean spf_del_user(Context ct)
    {
    	SharedPreferences sharedPreferences=ct.getSharedPreferences("login_info", MODE_PRIVATE);
    	Editor editor=sharedPreferences.edit();
    	editor.clear();
    	editor.commit();
    	String name=sharedPreferences.getString("name", null);//默认0
		if(name==null)
			return true;
		else {
			return false;
		}
    }
    
}