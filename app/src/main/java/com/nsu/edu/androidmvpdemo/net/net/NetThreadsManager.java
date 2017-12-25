package com.nsu.edu.androidmvpdemo.net.net;

import java.util.ArrayList;
import java.util.List;

public class NetThreadsManager {

	public static List<String> arrUri = new ArrayList();
	public synchronized static boolean hasURI(String uri){
		return arrUri.contains(uri);
	}
	public synchronized static boolean addURI(String uri){
		if(true == arrUri.contains(uri)){
			return false;
		}else{
			return arrUri.add(uri);
		}
	}
	public synchronized static boolean delURI(String uri){
		if(true == arrUri.contains(uri)){
			return arrUri.remove(uri);
		}else{
			return false;
		}
	}
}
