package com.mzd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;

public class Thread4Request extends Thread {
	private String[] array = { ".html", ".css", ".js", ".png", ".ico" };

	private Socket sk;

	public Socket getSk() {
		return sk;
	}

	public void setSk(Socket sk) {
		this.sk = sk;
	}

	@Override
	public void run() {
		try {
			InputStream ip = sk.getInputStream();
			Request req = new Request(ip);
			String str = req.getUri();
			OutputStream op = sk.getOutputStream();
			Response rep = new Response(op);
			// 处理静态资源
			if (ifstatic(str)) {
				rep.staticrequest(str);
			} else {
				// 处理动态资源
				System.out.println("处理动态资源");
				String urlpath = str.substring(0, str.indexOf("?"));
				String classpath = getclasspath(urlpath);
				try {
					Class clazz = Class.forName(classpath);
					Method method = clazz.getMethod("service", req.getClass(), rep.getClass());
					Object obj = clazz.newInstance();
					method.invoke(obj, req, rep);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			sk.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean ifstatic(String uri) {
		for (String s : array) {
			if (uri.endsWith(s)) {
				return true;
			}
		}
		return false;
	}

	private String getclasspath(String urlpath) {
		String str = "";
		List<Xml4Servlet> list = Server.xb.getList();
		// 全匹配优先原则
		for (Xml4Servlet xs : list) {
			if (xs.getUrlfilter().equals(urlpath)) {
				str = xs.getClasspath();
			}
		}
		if (str.equals("")) {
			// 进行模糊匹配
			for (Xml4Servlet xs : list) {
				String url = xs.getUrlfilter().replaceAll("*", "");
				if (urlpath.contains(url)) {
					str = xs.getClasspath();
				}
			}
		}
		return str;
	}
}
