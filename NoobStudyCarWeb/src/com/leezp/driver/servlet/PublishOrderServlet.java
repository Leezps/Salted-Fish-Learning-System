package com.leezp.driver.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.leezp.driver.business.PublishOrderBusiness;
import com.leezp.driver.entity.PublishOrderEntity;
import com.leezp.driver.vo.PublishOrderVO;

public class PublishOrderServlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/json;charset=utf-8");
		
		PrintWriter out = resp.getWriter();
		Gson gson = new Gson();
		String role = req.getParameter("role");
		String code = req.getParameter("requestCode");
		String id = req.getParameter("id");
		PublishOrderVO publishOrderVO = null;
		PublishOrderBusiness business = new PublishOrderBusiness();
		if(role == null || code == null) {
			publishOrderVO = new PublishOrderVO(1000, "���ݲ�������", null);
		} else {
			if(role.equals("0")) {
				if(code.equals("0")) {
					String driverID = req.getParameter("driverID");
					List<PublishOrderEntity> entities = business.getPublishOrderByDriverID(driverID);
					if(entities != null) {
						publishOrderVO = new PublishOrderVO(1003, "��������ɹ���", entities);
					} else {
						publishOrderVO = new PublishOrderVO(1004, "�ý���û�з���������", null);
					}
				} else if(code.equals("1")) {
					String phone = req.getParameter("phone");
					String password = req.getParameter("password");
					String bookOrder = req.getParameter("bookOrder");
					if(phone == null || password == null || bookOrder == null || id == null) {
						publishOrderVO = new PublishOrderVO(1000, "�����������", null);
					} else {
						int state = business.studentBookOrder(id, phone, password, gson.fromJson(bookOrder, PublishOrderEntity.class));
						switch (state) {
							case -1:
								publishOrderVO = new PublishOrderVO(1005, "��ʼʱ�������ʱ�䲻��ͬһ�죡", null);
								break;
							case -2:
								publishOrderVO = new PublishOrderVO(1006, "��ʼʱ���ڽ���ʱ��֮��", null);
								break;
							case -3:
								publishOrderVO = new PublishOrderVO(1007, "ԤԼ�Ķ���ʱ�䲻��С��1Сʱ��", null);
								break;
							case -4:
								publishOrderVO = new PublishOrderVO(1008, "ʱ�������޷�������", null);
								break;
							case -5:
								publishOrderVO = new PublishOrderVO(1009, "�������ݸ�ʽ�����û�и��û���", null);
								break;
							case -6:
								publishOrderVO = new PublishOrderVO(1010, "�ý����ڸ�ʱ����ѱ�ԤԼ��", null);
								break;
							case 0:
								publishOrderVO = new PublishOrderVO(1011, "��ʱ���û�з���������", null);
								break;
							default:
								publishOrderVO = new PublishOrderVO(1012, "�ö����ɹ�������", null);
								break;
						}
					}
				} else {
					publishOrderVO = new PublishOrderVO(1002, "û���������ͣ�", null);
				}
			} else if(role.equals("1")) {
				if(code.equals("0")) {
					List<PublishOrderEntity> entities = business.getPublishOrderByDriverID(id);
					if(entities != null) {
						publishOrderVO = new PublishOrderVO(1003, "��������ɹ���", entities);
					} else {
						publishOrderVO = new PublishOrderVO(1004, "�ý���û�з���������", null);
					}
				} else if(code.equals("1")) {
					String phone = req.getParameter("phone");
					String password = req.getParameter("password");
					String bookOrder = req.getParameter("bookOrder");
					if(phone == null || password == null || bookOrder == null || id == null) {
						publishOrderVO = new PublishOrderVO(1000, "�����������", null);
					} else {
						int state = business.insertPublishOrder(id, phone, password, gson.fromJson(bookOrder, PublishOrderEntity.class));
						switch(state) {
							case -1:
								publishOrderVO = new PublishOrderVO(1005, "��ʼʱ�������ʱ�䲻��ͬһ�죡", null);
								break;
							case -2:
								publishOrderVO = new PublishOrderVO(1006, "��ʼʱ���ڽ���֮��֮��", null);
								break;
							case -3:
								publishOrderVO = new PublishOrderVO(1007, "����������ʱ��β���С������Сʱ��", null);
								break;
							case -4:
								publishOrderVO = new PublishOrderVO(1008, "ʱ�������޷�������", null);
								break;
							case -5:
								publishOrderVO = new PublishOrderVO(1009, "�������ݸ�ʽ�����û�и��û���", null);
								break;
							case 0:
								publishOrderVO = new PublishOrderVO(1010, "��ʱ�������ǰ����������ʱ��γ��ֳ�ͻ��", null);
								break;
							default:
								publishOrderVO = new PublishOrderVO(1011, "���������ɹ���", null);
								break;
						}
					}
				} else {
					publishOrderVO = new PublishOrderVO(1002, "û�и��������ͣ�", null);
				}
			} else {
				publishOrderVO = new PublishOrderVO(1001, "û�иý�ɫ��", null);
			}
		}
		out.write(gson.toJson(publishOrderVO));
		out.close();
	}
	
}
