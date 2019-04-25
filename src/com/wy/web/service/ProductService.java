package com.wy.web.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.wy.dao.ProductDao;
import com.wy.domain.Category;
import com.wy.domain.Order;
import com.wy.domain.PageBean;
import com.wy.domain.Product;
import com.wy.utils.DataSourceUtils;

public class ProductService {

	//获得热门商品
	public List<Product> findHotProductList() {
		// 
		ProductDao dao = new ProductDao();

		List<Product> hotProductList = null;
		try {
			hotProductList = dao.findHotProductList();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return  hotProductList;
	}

	//获得最新商品
	public List<Product> findNewProductList() {
		// 
		ProductDao dao = new ProductDao();
		List<Product> newProductList = null;
		try {
			newProductList =  dao.findNewProductList();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newProductList;
	}

	//查找商品类别
	public List<Category> findAllCategory() {
		ProductDao dao = new ProductDao();
		List<Category> categoryList = null;
		try {
			categoryList = dao.findAllCategory();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return categoryList;
	}

	public PageBean<Product> findProductListByCid(String cid, int currentPage, int currentCount) {
		
		ProductDao dao = new ProductDao();
		// 封装一个PageBean ,返回Web层
		PageBean<Product> pageBean = new PageBean<Product>();
	
		//1.封装当前页
		pageBean.setCurrentPage(currentPage);
		//2每页显示条数
		pageBean.setCurrentCount(currentCount);
		//3.计算总条数
		int totalCount = 0 ;
		try {
			totalCount = dao.getCount(cid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pageBean.setTotalCount(totalCount);
		//4.封装总页数
		int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
		
		pageBean.setTotalPage(totalPage);
		
		//5、当前页显示的数据
		//select * from product where cid=? limit ?,?
		//当前页与其实索引index的关系
		int index = (currentPage - 1)*currentCount;
		List<Product> list = null;
		try {
			list = dao.findProductByPage(cid,index,currentCount);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pageBean.setList(list);
		
		return pageBean;
	}

	//查找商品详细信息
	public Product findProductByPid(String pid) {
		// 
		ProductDao dao = new ProductDao();
		Product product = null;
		try {
			product = dao.findProductByPid(pid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return product;
	}

	// 提交订单 将订单的数据和订单项的数据存储到数据库中
	public void submitOrder(Order order) {

		ProductDao dao = new ProductDao();

		try {
			// 1、开启事务
			DataSourceUtils.startTransation();
			// 2、调用dao存储order表数据的方法
			dao.addOrders(order);
			// 3、调用dao存储orderitem表数据的方法
			dao.addOrderItem(order);

		} catch (SQLException e) {
			try {
				DataSourceUtils.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				DataSourceUtils.commitAndRelease();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public void updateOrderAdrr(Order order) {
		ProductDao dao = new ProductDao();
		try {
			dao.updateOrderAdrr(order);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateOrderState(String r6_Order) {
		ProductDao dao = new ProductDao();
		try {
			dao.updateOrderState(r6_Order);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//获得指定用 户的订单集合
	public List<Order> findAllOrders(String uid) {
		ProductDao dao = new ProductDao();
		List<Order> orderList = null;
		try {
			orderList = dao.finAllOrders(uid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orderList;
	}

	//
	public List<Map<String, Object>> findAllOrderItemByOid(String oid) {
		ProductDao dao = new ProductDao();
		List<Map<String, Object>> mapList = null;
		try {
			 mapList = dao.finAllOrderItemByOid(oid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  mapList;
	}
}
