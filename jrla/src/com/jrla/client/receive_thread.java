package com.jrla.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.Socket;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;


public class receive_thread  extends Thread{
	Socket sc;//声明Socket的引用
//	main father;//声明ServerThread的引用
	DataInputStream din = null;//输入流
	DataOutputStream dout = null;//输出流
//	BufferedReader din;
//	PrintWriter dout;
	private boolean flag=true;//循环变量 
	
	Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 1){
//				 NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//	             //构建一个通知对象(需要传递的参数有三个,分别是图标,标题和 时间)
//	             Notification notification = new Notification(R.drawable.ic_launcher,"通知",System.currentTimeMillis());
//	             Intent intent = new Intent(activity_main.this,activity_chat.class);
//	             PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
//	             
//	             notification.setLatestEventInfo(getApplicationContext(), "位置社交", "您有新聊天消息，请注意查看", pendingIntent);
//	             notification.flags = Notification.FLAG_AUTO_CANCEL;//点击后自动消失
//	             notification.defaults = Notification.DEFAULT_SOUND;//声音默认
////	             notification.flags |= Notification.FLAG_SHOW_LIGHTS;     
////	             notification.defaults = Notification.DEFAULT_LIGHTS;
//	             notification.ledARGB = Color.BLUE;     
//	             notification.ledOnMS =5000; //闪光时间，毫秒  
//	             manager.notify(1, notification);//发动通知,id由自己指定，每一个Notification对应的唯一标志
//	             //其实这里的id没有必要设置,只是为了下面要用到它才进行了设置			}
		}
		}
	};
	
	
	public receive_thread(Socket sc){//构造器
		this.sc=sc;//得到Socket
		try{
			din=new DataInputStream(sc.getInputStream());//输入流
			dout=new DataOutputStream(sc.getOutputStream());//输入出流
//			din=new BufferedReader(new InputStreamReader(sc.getInputStream()));//输入流
//			dout=new PrintWriter(sc.getOutputStream());//输入出流
		}
		catch(Exception e){//捕获异常
			e.printStackTrace();//打印异常
		}
	}
	public void run(){
		while(true){//循环
			try{
//				System.out.println("Client msg = 222");
				String msg=din.readUTF();//收消息  read-UTF
				System.out.println("Client msg = " + msg);
				if(msg.startsWith("<#chat#>")){//登录动作

					myHandler.sendEmptyMessage(1);
					
					break;
				}
			}
				catch(EOFException e){//捕获异常
					System.out.println("Client Down");
					flag = false;
					try{
						if(din != null){
							din.close();
							din = null;
						}
					}
					catch(Exception el){
						el.printStackTrace();
					}
					try{
						if(dout != null){
							dout.close();
							dout = null;
						}
					}
					catch(Exception el){
						el.printStackTrace();
					}
					try{
						if(sc != null){
							sc.close();
							sc = null;
						}
					}
					catch(Exception el){
						el.printStackTrace();
					}		
				}
				catch(Exception e){//捕获异常
					e.printStackTrace();//打印异常
				}
			}
		}
//		public void setFlag(boolean flag){//循环标志位的设置方法
//			this.flag = flag;
//		}
	}
	
