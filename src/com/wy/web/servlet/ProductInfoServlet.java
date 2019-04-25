package com.wy.web.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wy.domain.Product;
import com.wy.web.service.ProductService;

public class ProductInfoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//获得当前页
		String currentPage = request.getParameter("currentPage");
		//获得商品类别
		String cid = request.getParameter("cid");
		
		//获得pid
		String pid = request.getParameter("pid");
		
		ProductService service = new ProductService();
		Product product = service.findProductByPid(pid);
		
		//获得客户端携带cookie---获得名字是pids的cookie
		String pids = pid;
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if("pids".equals(cookie.getName())) {
					pids = cookie.getValue();//1-3-2
					//将pids拆成一个数组
					String[] split = pids.split("pids");
					List<String> asList = Arrays.asList(split);
					LinkedList<String> list = new LinkedList<String>(asList);
					//判断集合中是否存在当前pid
					if(list.contains(pid)) {
						//包含查询商品pid
						list.remove(pid);
					}
					list.addFirst(pid);
					
					StringBuffer sb = new StringBuffer();
					for(int i=0;i<list.size() && i <7;i++) {
						sb.append(list.get(i));
						sb.append("-");
					}
					pids = sb.substring(0,sb.length()-1);
				  
					Cookie cookie_pids = new Cookie("pids",pids);
					
					response.addCookie(cookie_pids);
				}
			}
		}
		
		Cookie cookie_pids = new Cookie("pids",pids);
		
		response.addCookie(cookie_pids);
		
		
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("product", product);
		
		
		request.getRequestDispatcher("/product_info.jsp").forward(request, response);
		}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}
	
}
