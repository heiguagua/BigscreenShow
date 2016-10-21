package com.chinawiserv.deepone.manager.core.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 服务器参数类
 * @author zengpzh
 * @version 0.1
 */
public class ServerConfigUtil {
	private static final Log _log = LogFactory.getLog(ServerConfigUtil.class);

	private static String serverLocalIp = null;
	
	private static String serverPartLocalIp = null;

	/**
	 * 获取服务器内网Ip
	 * @author zengpzh
	 */
	public synchronized static String getServerLocalIp() {
		if (serverLocalIp == null) {
			serverLocalIp = getLocalIp();
		}
		return serverLocalIp;
	}

	public synchronized static String getServerPartLocalIp() {
		if (serverPartLocalIp == null) {
			serverPartLocalIp = getLocalIp();
			if (serverPartLocalIp == null || "".equals(serverPartLocalIp.trim())) {
				serverPartLocalIp = "*.*.*"+serverPartLocalIp.substring(serverPartLocalIp.lastIndexOf("."));
			}
		}
		return serverPartLocalIp;
	}
	
	/**
	 * 获取服务器内网Ip
	 * 
	 * @author zengpzh
	 */
	private static String getLocalIp() {
		String localip = "127.0.0.1";
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface
					.getNetworkInterfaces();
			InetAddress ip = null;
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> address = ni.getInetAddresses();
				while (address.hasMoreElements()) {
					ip = address.nextElement();
					if (ip.isSiteLocalAddress()) {
						localip = ip.getHostAddress();
					}
				}
			}
		} catch (SocketException e) {
			_log.debug("获取内网Ip出错:" + e.getMessage());
		}
		return localip;
	}
}
