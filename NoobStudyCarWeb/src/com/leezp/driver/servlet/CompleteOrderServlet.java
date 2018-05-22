package com.leezp.driver.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.leezp.driver.business.CompleteOrderBusiness;
import com.leezp.driver.entity.CompleteOrderEntity;
import com.leezp.driver.vo.CompleteOrderVO;

public class CompleteOrderServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/json;charset=utf-8");
		
		PrintWriter out = resp.getWriter();
		CompleteOrderVO compVo = null;
		CompleteOrderBusiness business = new CompleteOrderBusiness();
		
		String requestCode = req.getParameter("code");
		String role = req.getParameter("role");
		String userID = req.getParameter("id");
		String phone = req.getParameter("phone");
		String password = req.getParameter("password");
		String publishOrderId = req.getParameter("publishOrderId");
		
		if(requestCode == null) {
			compVo = new CompleteOrderVO(1000, "数据请求格式有误！", null);
		} else {
			if(requestCode.equals("0") && role != null && userID != null && phone != null && password != null) {
				List<CompleteOrderEntity> entities = business.getCompleteOrders(role, userID, phone, password);
				if(entities == null) {
					compVo = new CompleteOrderVO(1001, "没有该用户！", null);
				} else {
					compVo = new CompleteOrderVO(1002, "数据请求成功！", entities);
				}
			} else if(requestCode.equals("1") && role != null && userID != null && phone != null && password != null) {
				String order_id = req.getParameter("order_id");
				String remove_message = req.getParameter("remove_message");
				if(order_id == null) {
					compVo = new CompleteOrderVO(1000, "数据请求格式有误！", null);
				} else {
					if(remove_message == null) remove_message = "";
					int rowNum = business.removeCompleteOrder(role, userID, phone, password, order_id, remove_message);
					if(rowNum <= 0) {
						compVo = new CompleteOrderVO(1003, "参数不正确！", null);
					} else {
						compVo = new CompleteOrderVO(1004, "请求处理成功！", null);
					}
				}
			} else if(requestCode.equals("2") && publishOrderId != null) {
				List<CompleteOrderEntity> entities = business.getCompleteOrdersByPublishOrderId(publishOrderId);
				compVo = new CompleteOrderVO(1005, "数据请求成功！", entities);
			} else {
				compVo = new CompleteOrderVO(1000, "数据请求格式有误！", null);
			}
		}
		out.write(new Gson().toJson(compVo));
		out.close();
	}
}
