package com.jrla.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class main {
	private static boolean flag = true;//循环标志位
	static ServerSocket ss;//ServerSocket的引用
	public static void main(String[] args){//重写的run方法
		try {
			ss=new ServerSocket(9999);//监听到9999端口
		} catch (IOException e1) {//捕获异常
			e1.printStackTrace();//打印异常信息
		}
		while(flag){//进入主循环
			try{
				Socket sc=ss.accept();
				//阻塞式方法，等待用户连接//System.out.println("==========ServerThread============");
				
				System.out.println("==========a Click Connection============");
				thread sa = new thread(sc);//创建代理线程
				sa.start();//启动代理线程
			}
			catch(Exception e){//捕获异常
//				e.printStackTrace();//打印异常
			}
		}
	}
	public void setFlag(boolean flag){//设置循环变量
		this.flag = flag;
	}
}