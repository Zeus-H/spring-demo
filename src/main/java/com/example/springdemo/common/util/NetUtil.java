package com.example.springdemo.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetUtil {
	
	public static String getLocalHostAddress() {
		  try {
			return   InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return "";
		}
	}
	
}
