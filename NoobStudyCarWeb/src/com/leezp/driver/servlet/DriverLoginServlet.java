package com.leezp.driver.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.leezp.driver.business.DriverLoginBusiness;
import com.leezp.driver.entity.DriverEntity;
import com.leezp.driver.vo.DriverVO;

public class DriverLoginServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/json;charset=utf-8");
		
		PrintWriter out = resp.getWriter();
		
		String phone = req.getParameter("phone");
		String password  = req.getParameter("password");
		
		DriverVO driverVO = null;
		if(phone == null || phone.equals("")) {
			driverVO = new DriverVO(1000, "�ֻ��Ų���Ϊ�գ�", null);
		} else if(password == null || password.equals("")) {
			driverVO = new DriverVO(1000, "���벻��Ϊ�գ�", null);
		} else {
			DriverLoginBusiness business = new DriverLoginBusiness();
			DriverEntity entity = business.driverLogin(phone, password);
			if(entity == null) {
				driverVO = new DriverVO(1000, "�ֻ��Ż�������ִ���", null);
			} else {
				driverVO = new DriverVO(1001, "��½�ɹ���", entity);
			}
		}
		Gson gson = new Gson();
		out.write(gson.toJson(driverVO));
		out.close();
	}
}
