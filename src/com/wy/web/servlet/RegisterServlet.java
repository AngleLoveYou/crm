package com.wy.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import com.wy.domain.User;
import com.wy.utils.CommonsUtils;
import com.wy.utils.MailUtils;
import com.wy.web.service.UserService;

public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		//获得表单数据
		Map<String ,String[]> properties = request.getParameterMap();
		User user = new User();
		try {
			//自己指定一个类型转换器（string转换Date)
			ConvertUtils.register(new Converter() {

				@Override
				public Object convert(Class clazz, Object value) {
					// 将string转date
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date parse = null;
					try {
						parse =  format.parse(value.toString());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					return parse;
				}
				
			}, Date.class);
			
			//映射封装
			BeanUtils.populate(user, properties);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		//private String uid;
		user.setUid(CommonsUtils.getUUID());
		//private String telephone;
		user.setTelephone(null);
		//private int state;
		user.setState(0);
		//private String code;
		String activeCode = CommonsUtils.getUUID();
		user.setCode(activeCode);
	
		//将user传递给service层
		UserService service = new UserService();
		boolean isRegisterSuccess = service.regist(user);
	
		//是否注册成功
		if(isRegisterSuccess) {
			//发送邮件
			String emailMsg = "恭喜你注册成功，请点击下面的链接进行激活账户"
								+ "<a href='http://localhost:8080/HeimaShop/active?activeCode="+activeCode+"'>"
										+ "http://localhost:8080/HeimaShop/active?activeCode="+activeCode+"</a>";
			try {
				MailUtils.sendMail(user.getEmail(), emailMsg);
			} catch (AddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//跳转到注册成功页面
			response.sendRedirect(request.getContextPath()+"/registerSuccess.jsp");
		}else {
			//跳转到失败的提示页面
			response.sendRedirect(request.getContextPath()+"/registerFail.jsp");
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}
