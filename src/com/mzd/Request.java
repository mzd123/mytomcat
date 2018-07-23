package com.mzd;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Request {
	private ThreadLocal<String> uri = new ThreadLocal<String>();
	private ThreadLocal<HashMap<String, String>> tl = new ThreadLocal<HashMap<String, String>>();

	public Request(InputStream ip) throws IOException {
		if (ip != null) {
			// 定义一个参数列表
			tl.set(new HashMap<String, String>());
			byte b[] = new byte[1024 * 1024];
			int len = ip.read(b);
			// 请求地址（get请求的话，会有参数）
			String str = "";
			// 整个请求头
			String str2 = "";
			// post请求参数
			String str3 = "";
			if (len > 0) {
				str2 = new String(b, 0, len);
				str = str2.substring(str2.indexOf("/") + 1, str2.lastIndexOf("HTTP/1.1") - 1);
				if (str2.substring(0, 4).equalsIgnoreCase("post")) {
					// 如果是post请求需要单独处理
					str3 = "?" + str2.substring(str2.lastIndexOf("\n") + 1);
				}
				uri.set(str + str3);
				System.out.println(uri + "aaaaa");
				setParamters(str + str3);
			}
		} else {
			throw new NullPointerException();
		}
	}

	public String getUri() {
		return uri.get();
	}

	public void setParamters(String key, String value) {
		HashMap<String, String> hashmap = tl.get();
		hashmap.put(key, value);
	}

	/**
	 * 设置参数---仅仅只是请求的时候使用
	 * 
	 * @param uri
	 */
	private void setParamters(String uri) {
		if (uri == null || uri.equals("") || (!uri.contains("?")) || (!uri.contains("="))) {
			return;
		} else {
			try {
				String str[] = uri.split("\\?");
				String kvs = str[1];
				String params[] = kvs.split("&");
				for (String param : params) {
					String k_v[] = param.split("=");
					String k = k_v[0];
					String v = k_v[1];
					HashMap<String, String> hashmap = tl.get();
					hashmap.put(k, v);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}

	/**
	 * 获取参数
	 * 
	 * @param k
	 * @return
	 */
	public String getParamter(String k) {
		String str = "";
		if (k != null && !(k.equals(""))) {
			HashMap<String, String> hashmap = tl.get();
			str = hashmap.get(k);
		}
		return str;
	}
}
