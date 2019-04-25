package com.wy.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wy.web.service.UserService;

public class ActiveServlet extends HttpServlet {

	/**
	 * 激活码
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//获得激活码
		 String activeCode = request.getParameter("activeCode");
		 UserService service = new UserService();
		 service.active(activeCode);
		 
		 //跳转到登录页面
		 response.sendRedirect(request.getContextPath()+"/login.jsp");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(request, response);
	}
	
}
