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
import com.leezp.driver.business.StudentInforUploadBusiness;
import com.leezp.driver.dao.StudentDao;
import com.leezp.driver.entity.StudentEntity;
import com.leezp.driver.vo.StudentInforUploadVO;

public class StudentInforUploadServlet extends HttpServlet {
	
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
		StudentEntity entity = gson.fromJson(params, StudentEntity.class);
		
		StudentInforUploadBusiness studentInforUpload = new StudentInforUploadBusiness();
		int rowNum = studentInforUpload.updateStudentInfor(entity);
		StudentInforUploadVO studentInforVo = null;
		if (rowNum <= 0) {
			studentInforVo = new StudentInforUploadVO(1000, "用户信息未能修改成功！", null);
		} else {
			StudentEntity student = new StudentDao().findStudentByPhone(entity.getPhone());
			studentInforVo = new StudentInforUploadVO(1001, "用户信息修改成功！", student);
		}
		out.write(gson.toJson(studentInforVo));
		out.close();
	}

}
