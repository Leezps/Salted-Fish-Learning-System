package com.leezp.driver.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.leezp.driver.business.HelpBusiness;
import com.leezp.driver.entity.HelpEntity;
import com.leezp.driver.vo.HelpVO;

public class HelpServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/json;charset=utf-8");
		
		PrintWriter out = resp.getWriter();
		HelpVO helpVo = null;
		HelpBusiness business = new HelpBusiness();
		
		String role = req.getParameter("role");
		
		if(role == null) {
			helpVo = new HelpVO(1000, "数据请求格式有误！", null);
		} else {
			List<HelpEntity> helps = business.getHelp(role);
			if(helps == null) {
				helpVo = new HelpVO(1000, "数据请求格式有误！", null);
			} else {
				helpVo = new HelpVO(1001, "数据请求成功！", helps);
			}
		}
		out.write(new Gson().toJson(helpVo));
		out.close();
	}
	
}
