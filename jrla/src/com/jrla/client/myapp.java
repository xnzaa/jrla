package com.jrla.client;

import com.jrla.client.activity_chat.MyHandler;

import android.R.string;
import android.app.Application;
import android.os.Handler;

/**
 * �Լ�ʵ��Application��ʵ�����ݹ���
 * 
 * @author jason
 */
public class myapp extends Application {
	// �������
	private static MyHandler handler = null;
	
	// set����
	public void setHandler(MyHandler handler) {
		this.handler = handler;
	}

	// get����
	public MyHandler getHandler() {
		return handler;
	}
	
//	public void setHandler(Handler handler) {
//		this.handler = handler;
//	}
//
//	// get����
//	public Handler getHandler() {
//		return handler;
//	}
	
	private String  chatString=null;
	// set����
		public void setchat(String str) {
			this.chatString = str;
		}

		// get����
		public String getchat() {
			return chatString;
		}
}
