package com.jrla.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class main {
	private static boolean flag = true;//ѭ����־λ
	static ServerSocket ss;//ServerSocket������
	public static void main(String[] args){//��д��run����
		try {
			ss=new ServerSocket(9999);//������9999�˿�
		} catch (IOException e1) {//�����쳣
			e1.printStackTrace();//��ӡ�쳣��Ϣ
		}
		while(flag){//������ѭ��
			try{
				Socket sc=ss.accept();
				//����ʽ�������ȴ��û�����//System.out.println("==========ServerThread============");
				
				System.out.println("==========a Click Connection============");
				thread sa = new thread(sc);//���������߳�
				sa.start();//���������߳�
			}
			catch(Exception e){//�����쳣
//				e.printStackTrace();//��ӡ�쳣
			}
		}
	}
	public void setFlag(boolean flag){//����ѭ������
		this.flag = flag;
	}
}