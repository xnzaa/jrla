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
	Socket sc;//����Socket������
//	main father;//����ServerThread������
	DataInputStream din = null;//������
	DataOutputStream dout = null;//�����
//	BufferedReader din;
//	PrintWriter dout;
	private boolean flag=true;//ѭ������ 
	
	Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 1){
//				 NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//	             //����һ��֪ͨ����(��Ҫ���ݵĲ���������,�ֱ���ͼ��,����� ʱ��)
//	             Notification notification = new Notification(R.drawable.ic_launcher,"֪ͨ",System.currentTimeMillis());
//	             Intent intent = new Intent(activity_main.this,activity_chat.class);
//	             PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
//	             
//	             notification.setLatestEventInfo(getApplicationContext(), "λ���罻", "������������Ϣ����ע��鿴", pendingIntent);
//	             notification.flags = Notification.FLAG_AUTO_CANCEL;//������Զ���ʧ
//	             notification.defaults = Notification.DEFAULT_SOUND;//����Ĭ��
////	             notification.flags |= Notification.FLAG_SHOW_LIGHTS;     
////	             notification.defaults = Notification.DEFAULT_LIGHTS;
//	             notification.ledARGB = Color.BLUE;     
//	             notification.ledOnMS =5000; //����ʱ�䣬����  
//	             manager.notify(1, notification);//����֪ͨ,id���Լ�ָ����ÿһ��Notification��Ӧ��Ψһ��־
//	             //��ʵ�����idû�б�Ҫ����,ֻ��Ϊ������Ҫ�õ����Ž���������			}
		}
		}
	};
	
	
	public receive_thread(Socket sc){//������
		this.sc=sc;//�õ�Socket
		try{
			din=new DataInputStream(sc.getInputStream());//������
			dout=new DataOutputStream(sc.getOutputStream());//�������
//			din=new BufferedReader(new InputStreamReader(sc.getInputStream()));//������
//			dout=new PrintWriter(sc.getOutputStream());//�������
		}
		catch(Exception e){//�����쳣
			e.printStackTrace();//��ӡ�쳣
		}
	}
	public void run(){
		while(true){//ѭ��
			try{
//				System.out.println("Client msg = 222");
				String msg=din.readUTF();//����Ϣ  read-UTF
				System.out.println("Client msg = " + msg);
				if(msg.startsWith("<#chat#>")){//��¼����

					myHandler.sendEmptyMessage(1);
					
					break;
				}
			}
				catch(EOFException e){//�����쳣
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
				catch(Exception e){//�����쳣
					e.printStackTrace();//��ӡ�쳣
				}
			}
		}
//		public void setFlag(boolean flag){//ѭ����־λ�����÷���
//			this.flag = flag;
//		}
	}
	
