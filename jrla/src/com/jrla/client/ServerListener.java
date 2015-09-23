package com.jrla.client;

import java.util.List;

public interface ServerListener 
{
	void serverDataArrived(List list, boolean isEnd);
}
