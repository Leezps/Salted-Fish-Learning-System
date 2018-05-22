package com.leezp.driver.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.leezp.driver.business.MessageCenterBusiness;
import com.leezp.driver.entity.MessageCenterEntity;
import com.leezp.driver.vo.MessageCenterVO;

public class MessageCenterServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/json;charset=utf-8");
		
		PrintWriter out = resp.getWriter();
		MessageCenterVO meVo = null;
		MessageCenterBusiness business = new MessageCenterBusiness();
		
		String requestCode = req.getParameter("code");
		String role = req.getParameter("role");
		String id = req.getParameter("id");
		String phone = req.getParameter("phone");
		String password = req.getParameter("password");
		
		if(requestCode == null || role == null || id == null || phone == null || password == null || requestCode.equals("") || role.equals("") || id.equals("") || phone.equals("") || password.equals("")) {
			meVo = new MessageCenterVO(1000, "�������ݸ�ʽ����", null);
		} else {
			if (requestCode.equals("0")) {
				List<MessageCenterEntity> entities = business.getMessageCenter(role, id, phone, password);
				if(entities == null) {
					meVo = new MessageCenterVO(1001, "�����ڸ��û�!", null);
				} else {
					meVo = new MessageCenterVO(1002, "��������ɹ���", entities);
				}
			} else if(requestCode.equals("1")) {
				String message_id = req.getParameter("message_id");
				int rowNum = business.removeMessage(role, id, phone, password, message_id);
				if(rowNum <= -1) {
					meVo = new MessageCenterVO(1001, "�����ڸ��û�!", null);
				} else if(rowNum == 0) {
					meVo = new MessageCenterVO(1003, "û�и����ݣ�", null);
				} else {
					meVo = new MessageCenterVO(1004, "�����Ƴ��ɹ���", null);
				}
			} else if(requestCode.equals("2")) {
				String message_id = req.getParameter("message_id");
				int rowNum = business.openMessage(role, id, phone, password, message_id);
				if (rowNum <= -1) {
					meVo = new MessageCenterVO(1005, "�����ڸ��û���", null);
				} else{
					meVo = new MessageCenterVO(1006, "��Ϣ�Ѵ򿪣�", null);
				}
			} else {
				meVo = new MessageCenterVO(1000, "�������ݸ�ʽ����", null);
			}
		}
		out.write(new Gson().toJson(meVo));
		out.close();
	}
	
}
