package com.mzd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Response {
	private final static String html = "http/1.1 200 ok \r\n Content-Type: text/xml; charset=utf-8\r\n"
			+ "Content-Length: length" + "\n\n";
	private ThreadLocal<OutputStream> op = new ThreadLocal<OutputStream>();

	public Response(OutputStream op) {
		this.op.set(op);
	}

	/**
	 * 处理静态资源
	 * 
	 * @param uri
	 */
	public void staticrequest(String uri) {
		OutputStream op = this.op.get();
		if (op != null) {
			try {
				System.out.println("处理静态资源");
				FileInputStream fi = new FileInputStream(new File(uri));
				byte[] b = new byte[fi.available()];
				int i = 0;
				op.write(html.getBytes());
				while ((i = fi.read(b)) != -1) {
					op.write(b, 0, i);
				}
				op.flush();
				op.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					op.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
