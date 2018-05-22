package com.leezp.driver.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;
import com.leezp.driver.business.DriverImageUploadBusiness;
import com.leezp.driver.entity.DriverEntity;
import com.leezp.driver.vo.DriverVO;

public class DriverImageUploadServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/json;charset=utf-8");
		
		PrintWriter out = resp.getWriter();
		Map<String, String> map = new HashMap<>();
		DriverImageUploadBusiness business = new DriverImageUploadBusiness();
		DriverVO driverVo = null;
		if(ServletFileUpload.isMultipartContent(req)) {
			//���û�������С����ʱ�ļ�Ŀ¼
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			//�����ļ���С���ܳ���10M,��λΪbyte
			upload.setSizeMax(10*1024*1024);
			upload.setHeaderEncoding("utf-8");
			
			try {
				//����ServletFileUpload.parseRequest��������request���󣬵õ�һ�������������ϴ����ݵ�List����
				List<FileItem> fileItemList = upload.parseRequest(req);
				Iterator<FileItem> fileItems = fileItemList.iterator();
				String newFileName = null;
				FileItem image = null;
				
				//����list���ж�ÿһ��FileItem�����Ƿ����ϴ��ļ�
				while(fileItems.hasNext()) {
					FileItem fileItem = fileItems.next();
					//��ͨ��Ԫ��
					if(fileItem.isFormField()) {
						String name = fileItem.getFieldName();
						String value = fileItem.getString("utf-8");
						value = value.substring(1, value.length()-1);
						map.put(name, value);
					} else {
						//�ļ�����
						String fileName = fileItem.getName();
						//�ļ���չ��
						String suffix = fileName.substring(fileName.lastIndexOf('.'), fileName.length()-1);
						//�����µ��ļ���
						newFileName = new Date().getTime() + suffix;
						image = fileItem;
					}
				}
				String path = req.getSession().getServletContext().getRealPath("/");
				String code = map.get("requestCode");
				String id = map.get("id");
				String phone = map.get("phone");
				String password = map.get("password");
				if(id == null || phone == null || password == null || code == null || id.equals("") || phone.equals("") || password.equals("") ||code.equals("") || image == null) {
					driverVo = new DriverVO(1000, "���������ʽ����", null);
				} else {
					if (code.equals("0")) {
						path = path+"/Head-Image/"+newFileName;
						File file = new File(path);
						image.write(file);
						int rowNum = business.changeImageUrl(code, "Head-Image/"+newFileName, id, phone, password);
						if(rowNum <= 0) {
							driverVo = new DriverVO(1001, "ͷ���ϴ�ʧ�ܣ�", null);
						} else {
							DriverEntity entity = business.getDriverInformation(phone);
							driverVo = new DriverVO(1002, "ͷ���ϴ��ɹ���", entity);
						}
					} else if(code.equals("1")) {
						path = path+"/Identify-Image/"+newFileName;
						File file = new File(path);
						image.write(file);
						int rowNum = business.changeImageUrl(code, "Identify-Image/"+newFileName, id, phone, password);
						if(rowNum <= 0) {
							driverVo = new DriverVO(1003, "���֤ͼ���ϴ�ʧ�ܣ�", null);
						} else {
							DriverEntity entity = business.getDriverInformation(phone);
							driverVo = new DriverVO(1004, "���֤ͼ���ϴ��ɹ���", entity);
						}
					} else if(code.equals("2")) {
						path = path+"/Trainer-Image/"+newFileName;
						File file = new File(path);
						image.write(file);
						int rowNum = business.changeImageUrl(code, "Trainer-Image/"+newFileName, id, phone, password);
						if(rowNum <= 0) {
							driverVo = new DriverVO(1005, "����֤ͼ���ϴ�ʧ�ܣ�", null);
						} else {
							DriverEntity entity = business.getDriverInformation(phone);
							driverVo = new DriverVO(1006, "����֤ͼ���ϴ��ɹ���", entity);
						}
					} else {
						driverVo = new DriverVO(1007, "���������������", null);
					}
				}
			} catch (FileUploadException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		out.write(new Gson().toJson(driverVo));
		out.close();
	}
}
