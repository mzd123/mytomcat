package com.mzd;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Server {
	private final static String xmlpath = "mzd/web.xml";
	static XmlBean xb = new XmlBean();

	public static void main(String args[]) {
		// 先初始化xb对象
		init_xb();
		Socket sk = null;
		ServerSocket ss = null;
		try {
			int port = xb.getPort();
			ss = new ServerSocket(port);
			System.out.println("等待响应");
			while (true) {
				sk = ss.accept();
				Thread4Request tr = new Thread4Request();
				tr.setSk(sk);
				tr.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void init_xb() {
		try {
			File xmlFile = new File(xmlpath);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document doc = builder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			// 端口号
			String port = doc.getElementsByTagName("server-port").item(0).getTextContent();
			xb.setPort(Integer.valueOf(port));
			List<Xml4Servlet> list = new ArrayList<Xml4Servlet>();
			/**
			 * servlet集合
			 */
			NodeList nList = doc.getElementsByTagName("mytomcat");
			for (int i = 0; i < nList.getLength(); i++) {
				Xml4Servlet xs = new Xml4Servlet();
				Node node = nList.item(i);
				Element ele = (Element) node;
				String name = ele.getElementsByTagName("name").item(0).getTextContent();
				String classpath = ele.getElementsByTagName("class").item(0).getTextContent();
				String urlfilter = ele.getElementsByTagName("url").item(0).getTextContent().substring(1);
				xs.setName(name);
				xs.setClasspath(classpath);
				xs.setUrlfilter(urlfilter);
				list.add(xs);
			}
			xb.setList(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
