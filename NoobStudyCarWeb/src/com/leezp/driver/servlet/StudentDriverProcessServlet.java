package com.leezp.driver.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.leezp.driver.business.DriverProcessBusiness;
import com.leezp.driver.entity.DriverProcessEntity;
import com.leezp.driver.vo.DriverProcessVO;

public class StudentDriverProcessServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/json;charset=utf-8");
		
		PrintWriter out = resp.getWriter();
		DriverProcessBusiness business = new DriverProcessBusiness();
		DriverProcessVO driverVO = null;
		List<DriverProcessEntity> entities = business.getDriverProcess();
		if(entities == null) {
			driverVO = new DriverProcessVO(1000, "数据请求异常！", null);
		} else {
			driverVO = new DriverProcessVO(1001, "数据请求成功！", entities);
		}
		out.write(new Gson().toJson(driverVO));
		out.close();
	}
}
