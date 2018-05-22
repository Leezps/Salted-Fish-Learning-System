package com.leezp.driver.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leezp.driver.business.DriverInforUploadBusiness;
import com.leezp.driver.dao.DriverDao;
import com.leezp.driver.entity.DriverEntity;
import com.leezp.driver.vo.DriverVO;

public class DriverInforUploadServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/json;charset=utf-8");
		
		PrintWriter out = resp.getWriter();
		Gson gson = new GsonBuilder().create();
		
		BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) req.getInputStream(), "utf-8"));
		StringBuffer sb = new StringBuffer("");
		String temp;
		while ((temp = br.readLine()) != null) {
			sb.append(temp);
		}
		br.close();
		String params = sb.toString();
		DriverEntity entity = gson.fromJson(params, DriverEntity.class);
		
		DriverInforUploadBusiness business = new DriverInforUploadBusiness();
		int rowNum = business.updateDriverInfor(entity);
		DriverVO driverVO = null;
		if(rowNum <= 0) {
			driverVO = new DriverVO(1000, "用户信息未能修改成功！", null);
		} else {
			DriverEntity driver = new DriverDao().findDriverByPhone(entity.getPhone());
			driverVO = new DriverVO(1001, "用户信息修改成功！", driver);
		}
		out.write(gson.toJson(driverVO));
		out.close();
	}
}
