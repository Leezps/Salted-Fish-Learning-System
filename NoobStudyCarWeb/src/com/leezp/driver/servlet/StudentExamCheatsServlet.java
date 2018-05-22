package com.leezp.driver.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.leezp.driver.business.ExamCheatsBuiness;
import com.leezp.driver.entity.ExamCheatsEntity;
import com.leezp.driver.vo.ExamCheatsVO;

public class StudentExamCheatsServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/json;charset=utf-8");
		
		PrintWriter out = resp.getWriter();
		String process_order = req.getParameter("process_order");
		
		ExamCheatsVO examVO = null;
		ExamCheatsBuiness business = new ExamCheatsBuiness();
		
		if(process_order == null) {
			examVO = new ExamCheatsVO(1000, "���������ʽ����", null);
		} else {
			List<ExamCheatsEntity> entities = business.getExamCheats(process_order);
			if(entities.size() <= 0) {
				examVO = new ExamCheatsVO(1001, "û�и�������ص����ݣ�", null);
			} else {
				examVO = new ExamCheatsVO(1002, "��������ɹ���", entities);
			}
		}
		
		out.write(new Gson().toJson(examVO));
		out.close();
	}
}
