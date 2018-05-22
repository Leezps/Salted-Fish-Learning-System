package com.leezp.driver.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse.SmsSendDetailDTO;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.google.gson.Gson;
import com.leezp.driver.business.StudentRegisterBusiness;
import com.leezp.driver.entity.StudentEntity;
import com.leezp.driver.vo.StudentLoginRegisterVO;

public class RegisterServlet extends HttpServlet{
	
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
		
		String code = req.getParameter("requestCode");
		String phone = req.getParameter("phone");
		String password = req.getParameter("password");
		String confirmPassword = req.getParameter("confirmPassword");
		
		StudentRegisterBusiness studentRegister = new StudentRegisterBusiness();
		StudentLoginRegisterVO studentRegisterVO = null;
		
		if(phone == null || phone.equals("") || phone.length() != 11) {
			studentRegisterVO = new StudentLoginRegisterVO(1000, "手机号格式不正确！", null);
		} else if(studentRegister.isExist(phone)) {
			studentRegisterVO = new StudentLoginRegisterVO(1001, "手机号已存在！", null);
		} else {
			if(code == null) {
				studentRegisterVO = new StudentLoginRegisterVO(1002, "非正常网络请求！", null);
			} else {
				if(code.equals("0")) {
					StringBuilder strBuilder = new StringBuilder("");
					for(int i=0; i<6; ++i) {
						strBuilder.append((int) (Math.random()*10));
					}
					SendSmsResponse sendMessage = null;
					try {
						sendMessage = studentRegister.sendMessageVerification(phone, strBuilder.toString());
					} catch (ClientException e) {
						e.printStackTrace();
					}
					if(sendMessage.getCode() != null && sendMessage.getCode().equals("OK")) {
						studentRegisterVO = new StudentLoginRegisterVO(1003, "已成功发送验证码！", null);
						studentRegisterVO.setVerification_id(sendMessage.getBizId());
					} else {
						studentRegisterVO = new StudentLoginRegisterVO(1004, "验证码发送失败！", null);
					}
				} else if(code.equals("1")) {
					if(password == null || password.equals("") || password.length() > 20 || confirmPassword == null) {
						studentRegisterVO = new StudentLoginRegisterVO(1005, "密码或确认密码格式不正确！", null);
					} else if(!password.equals(confirmPassword)) {
						studentRegisterVO = new StudentLoginRegisterVO(1006, "确认密码与密码不一致！", null);
					} else {
						String bizId = req.getParameter("bizId");
						String verification = req.getParameter("verification");
						QuerySendDetailsResponse getMessage = null;
						try {
							getMessage = studentRegister.getSendMessageDetail(phone, bizId);
						} catch (ClientException e) {
							e.printStackTrace();
						}
						if(getMessage.getCode() != null && getMessage.getCode().equals("OK") && getMessage.getSmsSendDetailDTOs().size() > 0) {
							SmsSendDetailDTO message = getMessage.getSmsSendDetailDTOs().get(0);
							if(message.getOutId().equals(verification)) {
								int studentId = studentRegister.register(phone, password);
								StudentEntity entity = new StudentEntity(phone, password);
								entity.setId(studentId);
								studentRegisterVO = new StudentLoginRegisterVO(1007, "验证成功！", entity);
							} else {
								studentRegisterVO = new StudentLoginRegisterVO(1008, "验证码错误！", null);
							}
						} else {
							studentRegisterVO = new StudentLoginRegisterVO(1009, "验证失败！", null);
						}
					}
				}
			}
		}
		out.write(gson.toJson(studentRegisterVO));
		out.close();
	}

}
