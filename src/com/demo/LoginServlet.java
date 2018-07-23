package com.demo;

import com.mzd.BaseServlet;
import com.mzd.Request;
import com.mzd.Response;

public class LoginServlet extends BaseServlet {

	@Override
	public void service(Request req, Response rep) {
		String username = req.getParamter("username");
		System.out.println("username=" + username);
	}
}
