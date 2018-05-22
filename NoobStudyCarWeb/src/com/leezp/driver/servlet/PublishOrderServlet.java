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
			publishOrderVO = new PublishOrderVO(1000, "数据参数有误！", null);
		} else {
			if(role.equals("0")) {
				if(code.equals("0")) {
					String driverID = req.getParameter("driverID");
					List<PublishOrderEntity> entities = business.getPublishOrderByDriverID(driverID);
					if(entities != null) {
						publishOrderVO = new PublishOrderVO(1003, "数据请求成功！", entities);
					} else {
						publishOrderVO = new PublishOrderVO(1004, "该教练没有发布订单！", null);
					}
				} else if(code.equals("1")) {
					String phone = req.getParameter("phone");
					String password = req.getParameter("password");
					String bookOrder = req.getParameter("bookOrder");
					if(phone == null || password == null || bookOrder == null || id == null) {
						publishOrderVO = new PublishOrderVO(1000, "请求参数有误", null);
					} else {
						int state = business.studentBookOrder(id, phone, password, gson.fromJson(bookOrder, PublishOrderEntity.class));
						switch (state) {
							case -1:
								publishOrderVO = new PublishOrderVO(1005, "开始时间与结束时间不在同一天！", null);
								break;
							case -2:
								publishOrderVO = new PublishOrderVO(1006, "开始时间在结束时间之后！", null);
								break;
							case -3:
								publishOrderVO = new PublishOrderVO(1007, "预约的订单时间不能小于1小时！", null);
								break;
							case -4:
								publishOrderVO = new PublishOrderVO(1008, "时间数据无法解析！", null);
								break;
							case -5:
								publishOrderVO = new PublishOrderVO(1009, "请求数据格式有误或没有该用户！", null);
								break;
							case -6:
								publishOrderVO = new PublishOrderVO(1010, "该教练在该时间段已被预约！", null);
								break;
							case 0:
								publishOrderVO = new PublishOrderVO(1011, "该时间段没有发布订单！", null);
								break;
							default:
								publishOrderVO = new PublishOrderVO(1012, "该订单成功创建！", null);
								break;
						}
					}
				} else {
					publishOrderVO = new PublishOrderVO(1002, "没该请求类型！", null);
				}
			} else if(role.equals("1")) {
				if(code.equals("0")) {
					List<PublishOrderEntity> entities = business.getPublishOrderByDriverID(id);
					if(entities != null) {
						publishOrderVO = new PublishOrderVO(1003, "数据请求成功！", entities);
					} else {
						publishOrderVO = new PublishOrderVO(1004, "该教练没有发布订单！", null);
					}
				} else if(code.equals("1")) {
					String phone = req.getParameter("phone");
					String password = req.getParameter("password");
					String bookOrder = req.getParameter("bookOrder");
					if(phone == null || password == null || bookOrder == null || id == null) {
						publishOrderVO = new PublishOrderVO(1000, "请求参数有误", null);
					} else {
						int state = business.insertPublishOrder(id, phone, password, gson.fromJson(bookOrder, PublishOrderEntity.class));
						switch(state) {
							case -1:
								publishOrderVO = new PublishOrderVO(1005, "开始时间与结束时间不在同一天！", null);
								break;
							case -2:
								publishOrderVO = new PublishOrderVO(1006, "开始时间在结束之间之后！", null);
								break;
							case -3:
								publishOrderVO = new PublishOrderVO(1007, "发布订单的时间段不能小于两个小时！", null);
								break;
							case -4:
								publishOrderVO = new PublishOrderVO(1008, "时间数据无法解析！", null);
								break;
							case -5:
								publishOrderVO = new PublishOrderVO(1009, "请求数据格式有误或没有该用户！", null);
								break;
							case 0:
								publishOrderVO = new PublishOrderVO(1010, "该时间段与以前发布订单的时间段出现冲突！", null);
								break;
							default:
								publishOrderVO = new PublishOrderVO(1011, "订单发布成功！", null);
								break;
						}
					}
				} else {
					publishOrderVO = new PublishOrderVO(1002, "没有该请求类型！", null);
				}
			} else {
				publishOrderVO = new PublishOrderVO(1001, "没有该角色！", null);
			}
		}
		out.write(gson.toJson(publishOrderVO));
		out.close();
	}
	
}
