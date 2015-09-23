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
			
	Socket s = null;//����Socket������
	DataOutputStream dout = null;//�����
	DataInputStream din = null;//������	
	ProgressDialog myDialog = null;//���ȿ�
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
				Toast.makeText(activity_login.this, "�û������������", Toast.LENGTH_LONG).show();
			}
		}
	};
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
   	//���� ��� ��Ч
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
					
					if(name.trim().equals("")){//���û���Ϊ��ʱ 
						Toast.makeText(activity_login.this, "���������û�����", Toast.LENGTH_SHORT).show();
						return;
					}
					if(passwd.trim().equals("")){//������Ϊ��ʱ
						Toast.makeText(activity_login.this, "�����������룡", Toast.LENGTH_SHORT).show();
						return;
					}
//					myDialog = ProgressDialog.show(activity_login.this, "����", "���ڼ���...",true);
					final Toast toast = Toast.makeText(getApplicationContext(),"���ڼ���..." , Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP|Gravity.CENTER, -50, 100);  
					toast.show();
					new Thread(){//����ʱ�Ķ����ŵ��߳���
						public void run(){//��д��run����
							try {
								s = new Socket(communicate.server_ip, communicate.server_port);//���ӷ�����
								dout = new DataOutputStream(s.getOutputStream());
								din = new DataInputStream(s.getInputStream());
								 
								System.out.println("<#LOGIN#>"+name+"|"+passwd);
								dout.writeUTF("<#LOGIN#>"+name+"|"+passwd);
								String msg = din.readUTF();//���շ�������������Ϣ
								if(msg.startsWith("<#LOGINOK#>")){//��¼�ɹ�

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
								else if(msg.startsWith("<#LOGINERROR#>")){//��¼ʧ��
									myHandler.sendEmptyMessage(1);
								}
							} catch (Exception e) {//�����쳣
								e.printStackTrace();//��ӡ�쳣
							} finally{//ʹ��finally��֤֮������һ����ִ��
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
//                    finish();// finish��Activity
				case R.id.bn_register:
					

					name= login_et_user_name.getText().toString();
					passwd= login_et_user_passwd.getText().toString();
					if(name.trim().equals("")){//���û���Ϊ��ʱ
						Toast.makeText(activity_login.this,"�û�������Ϊ��!",Toast.LENGTH_SHORT).show();
						return;
					}
					else if(passwd.trim().equals("")){//���벻��Ϊ��
						Toast.makeText(activity_login.this,"���벻��Ϊ��!",Toast.LENGTH_SHORT).show();
						return;
					}
					
//					myDialog = ProgressDialog.show(activity_login.this, "����", "���ڼ���...",true);
					Toast.makeText(activity_login.this,"���ڼ���..." , Toast.LENGTH_LONG);
					new Thread(){
						public void run(){
							try{//�������粢����
						        s = new Socket(communicate.server_ip, communicate.server_port);
						        dout = new DataOutputStream(s.getOutputStream());
						        din = new DataInputStream(s.getInputStream());
							}catch(Exception e){//�����쳣
								e.printStackTrace();//��ӡ�쳣
							}
							String u_name = name;//�û���
							String u_qq	= "";//qq��
							String u_pwd1 = passwd;//����
							String u_Email = "";//�ʼ�
							String u_dis = "";//����
							String msg = "<#REGISTER#>"+u_name+"|"+u_pwd1+"|"+u_qq+"|"+u_Email+"|"+u_dis;
							try {
								dout.writeUTF(msg);//�����������ע����Ϣ
								String msg2 = din.readUTF();//���շ���������������Ϣ
								if(msg2.startsWith("<#REGISTEROK#>")){//ע��ɹ�
									msg2=msg2.substring(14);//��ȡ�ִ�
									String[] str = msg2.split("\\|");//�ָ��ַ���

									Intent intent = new Intent();
									intent.setClass(activity_login.this, activity_main.class);
									
									Bundle data = new Bundle();//��������
									data.putString("uid", str[0]);//��bundle���������
									data.putString("u_name", str[1]);//��bundle���������
									
									intent.putExtras(data);//��bundle��ŵ�Intent��
									startActivity(intent);//����MainActivity
									activity_login.this.finish();	//�ͷŵ�ǰ��Activity
								}
							} catch (IOException e) {//�����쳣
								e.printStackTrace();//��ӡ�쳣
							} finally{
								try{
									if(dout != null){
										dout.close();
										dout = null;
									}
								}
								catch(Exception e){//�����쳣
									e.printStackTrace();//��ӡ�쳣��Ϣ
								}
								try{
									if(din != null){
										din.close();
										din = null;
									}
								}
								catch(Exception e){//�����쳣
									e.printStackTrace();//��ӡ�쳣��Ϣ
								}
								try{
									if(s != null){
										s.close();
										s = null;
									}
								}
								catch(Exception e){//�����쳣
									e.printStackTrace();//��ӡ�쳣��Ϣ
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
//                    finish();// finish��Activity
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
		//����ͨ��Editor����洢key-value��ֵ�����ݡ�
//		Set<String> values=new HashSet<String>(); 		
		editor.putString("name", id);//�洢string����
		// �ġ�ͨ��commit()�����ύ���ݡ�
		editor.commit();
    }
    
    public static void spf_write_user(Context ct,String name)
    {
    	SharedPreferences sharedPreferences=ct.getSharedPreferences("login_info", MODE_PRIVATE);
        Editor editor=sharedPreferences.edit();
		//����ͨ��Editor����洢key-value��ֵ�����ݡ�
//		Set<String> values=new HashSet<String>(); 		
		editor.putString("name", name);//�洢string����
		// �ġ�ͨ��commit()�����ύ���ݡ�
		editor.commit();
    }
    
    public static String spf_read_user(Context ct)
    {
    	String name=null;
    	SharedPreferences sharedPreferences_read=ct.getSharedPreferences("login_info", MODE_PRIVATE);
		name=sharedPreferences_read.getString("name", null);//Ĭ��0
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
		name=sharedPreferences_read.getString("name", null);//Ĭ��0
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
//		name=sharedPreferences_read.getString("name", null);//Ĭ��0
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
    	String name=sharedPreferences.getString("name", null);//Ĭ��0
		if(name==null)
			return true;
		else {
			return false;
		}
    }
    
}