package com.leezp.driver.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.leezp.driver.business.StudentGetPaymentBusiness;
import com.leezp.driver.entity.GetPaymentEntity;
import com.leezp.driver.vo.StudentGetPaymentVO;

public class StudentGetPaymentServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/json;charset=utf-8");
		
		PrintWriter out = resp.getWriter();
		StudentGetPaymentVO studentGetPaymentVO = null;
		StudentGetPaymentBusiness business = new StudentGetPaymentBusiness();
		
		String requestCode = req.getParameter("code");
		String id = req.getParameter("id");
		String phone = req.getParameter("phone");
		String password = req.getParameter("password");
		if(requestCode == null || id == null || phone == null || password == null || requestCode.equals("") || id.equals("") || phone.equals("") || password.equals("")) {
			studentGetPaymentVO = new StudentGetPaymentVO(1000, "请求数据格式有问题！", null);
		} else {
			if(requestCode.equals("0")) {
				List<GetPaymentEntity> payment_way = business.getPaymentWay(id, phone, password);
				if(payment_way == null) {
					studentGetPaymentVO = new StudentGetPaymentVO(1001, "不存在该用户！", null);
				} else {
					studentGetPaymentVO = new StudentGetPaymentVO(1002, "数据请求成功！", payment_way);
				}
			} else if(requestCode.equals("1")) {
				String payment_way = req.getParameter("payment_way");
				int code = business.insertPaymentWay(id, phone, password, new Gson().fromJson(payment_way, GetPaymentEntity.class));
				if(code <= -1) {
					studentGetPaymentVO = new StudentGetPaymentVO(1001, "不存在该用户！", null);
				} else if(code == 0) {
					studentGetPaymentVO = new StudentGetPaymentVO(1003, "支付方式超过了5种！", null);
				} else {
					List<GetPaymentEntity> entities = business.getPaymentWay(id, phone, password);
					studentGetPaymentVO = new StudentGetPaymentVO(1004, "支付方式添加成功！", entities);
				}
			} else if(requestCode.equals("2")){
				String payment_id = req.getParameter("payment_id");
				if(payment_id == null || payment_id.equals("") ) {
					studentGetPaymentVO = new StudentGetPaymentVO(1000, "请求数据格式有问题！", null);
				} else {
					int code = business.removePaymentWay(id, phone, password, payment_id);
					if(code <= -1) {
						studentGetPaymentVO = new StudentGetPaymentVO(1001, "不存在该用户！", null);
					} else if(code == 0) {
						studentGetPaymentVO = new StudentGetPaymentVO(1005, "没有该支付方式！", null);
					} else {
						studentGetPaymentVO = new StudentGetPaymentVO(1006, "支付方式移除成功！", null);
					}
				}
			} else {
				studentGetPaymentVO = new StudentGetPaymentVO(1000, "请求数据格式有问题！", null);
			}
		}
		out.write(new Gson().toJson(studentGetPaymentVO));
		out.close();
	}
}
