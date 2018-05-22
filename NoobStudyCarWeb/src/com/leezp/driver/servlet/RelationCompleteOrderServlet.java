package com.leezp.driver.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.leezp.driver.business.RelationCompleteOrderBusiness;
import com.leezp.driver.vo.RelationCompleteOrderVO;

public class RelationCompleteOrderServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/json;charset=utf-8");
		
		PrintWriter out = resp.getWriter();
		String role = req.getParameter("role");
		String orderID = req.getParameter("orderID");
		String driverID = req.getParameter("driverID");
		String studentID = req.getParameter("studentID");
		RelationCompleteOrderVO relationVO = null;
		RelationCompleteOrderBusiness business = new RelationCompleteOrderBusiness();
		if(role == null || orderID == null || driverID == null || studentID == null || role.equals("") || orderID.equals("") || driverID.equals("") || studentID.equals("")) {
			relationVO = new RelationCompleteOrderVO(1000, "数据请求参数有误！");
		} else {
			Map<String, String> map = business.getRelationCompleteOrder(role, orderID, studentID, driverID);
			if(map == null) {
				relationVO = new RelationCompleteOrderVO(1001, "没有相关数据！");
			} else {
				relationVO = new RelationCompleteOrderVO(1002, "数据请求成功！", orderID, map.get("phone"), map.get("name"), map.get("head_url"));
			}
		}
		out.write(new Gson().toJson(relationVO));
		out.close();
	}
}
