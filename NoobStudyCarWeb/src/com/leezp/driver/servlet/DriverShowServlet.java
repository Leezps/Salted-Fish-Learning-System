package com.leezp.driver.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.leezp.driver.business.DriverShowBusiness;
import com.leezp.driver.entity.DriverEntity;
import com.leezp.driver.vo.DriverShowVO;

public class DriverShowServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/json;charset=utf-8");
		
		PrintWriter out = resp.getWriter();
		DriverShowVO showVO = null;
		DriverShowBusiness business = new DriverShowBusiness();
		String place = req.getParameter("place");
		if(place == null || place.equals("")) {
			showVO = new DriverShowVO(1000, "数据请求格式有误！", null);
		} else {
			List<DriverEntity> entities = business.getDriversByPlace(place);
			showVO = new DriverShowVO(1001, "数据请求成功！", entities);
		}
		out.write(new Gson().toJson(showVO));
		out.close();
	}
}
