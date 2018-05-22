package com.leezp.driver.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.leezp.driver.business.StudentLoginBusiness;
import com.leezp.driver.entity.StudentEntity;
import com.leezp.driver.vo.StudentLoginRegisterVO;

public class LoginServlet extends HttpServlet{

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
		String password = req.getParameter("password");
		
		StudentLoginRegisterVO studentLoginVO;
		if(phone == null || phone.equals("")) {
			studentLoginVO = new StudentLoginRegisterVO(1000, "手机号不能为空！", null);
		} else if(password == null || password.equals("")) {
			studentLoginVO = new StudentLoginRegisterVO(1000, "密码不能为空！", null);
		} else {
			StudentLoginBusiness studentLogin = new StudentLoginBusiness();
			StudentEntity entity = studentLogin.login(phone, password);
			if(entity == null) {
				studentLoginVO = new StudentLoginRegisterVO(1000, "手机号或密码出现错误！", null);
			} else {
				studentLoginVO = new StudentLoginRegisterVO(1001, "登陆成功！", entity);
			}
		}
		Gson gson = new Gson();
		out.write(gson.toJson(studentLoginVO));
		out.close();
	}
}
