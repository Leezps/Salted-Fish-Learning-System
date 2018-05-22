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
import com.leezp.driver.business.StudentImageUploadBusiness;
import com.leezp.driver.entity.StudentEntity;
import com.leezp.driver.vo.StudentImageUploadVO;

public class StudentImageUploadServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/json;charset=utf-8");
		
		PrintWriter out = resp.getWriter();
		Map<String, String> map = new HashMap<>();
		StudentImageUploadBusiness studentImageUpload = new StudentImageUploadBusiness();
		StudentImageUploadVO studentImageUploadVO = null;
		
		if(ServletFileUpload.isMultipartContent(req)) {
			//设置缓冲区大小和临时文件目录
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			//设置文件大小不能超过10M,单位为byte
			upload.setSizeMax(10*1024*1024);
			upload.setHeaderEncoding("utf-8");
			
			try {
				//调用ServletFileUpload.parseRequest方法解析request对象，得到一个保存了所有上传内容的List对象。
				List<FileItem> fileItemList = upload.parseRequest(req);
				Iterator<FileItem> fileItems = fileItemList.iterator();
				String newFileName = null;
				FileItem image = null;
				
				//遍历list，判断每一个FileItem对象是否是上传文件
				while(fileItems.hasNext()) {
					FileItem fileItem = fileItems.next();
					//普通表单元素
					if(fileItem.isFormField()) {
						String name = fileItem.getFieldName();
						String value = fileItem.getString("utf-8");
						value = value.substring(1, value.length()-1);
						map.put(name, value);
					} else {
						//文件名称
						String fileName = fileItem.getName();
						//文件扩展名
						String suffix = fileName.substring(fileName.lastIndexOf('.'), fileName.length()-1);
						//创建新的文件名
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
					studentImageUploadVO = new StudentImageUploadVO(1000, "数据请求格式有误！", null);
				} else {
					if(studentImageUpload.isRightStudent(id, phone, password)) {
						if(code.equals("0")) {
							path = path+"/Head-Image/"+newFileName;
							File file = new File(path);
							image.write(file);
							int rowNum = studentImageUpload.changeImageUrl(code, "Head-Image/"+newFileName, id);
							if(rowNum <= 0) {
								studentImageUploadVO = new StudentImageUploadVO(1001, "头像上传失败！", null);
							} else {
								StudentEntity entity = studentImageUpload.getStudentInformation(phone);
								studentImageUploadVO = new StudentImageUploadVO(1002, "头像上传成功！", entity);
							}
						} else if(code.equals("1")) {
							path = path+"/Identify-Image/"+newFileName;
							File file = new File(path);
							image.write(file);
							int rowNum = studentImageUpload.changeImageUrl(code, "Identify-Image/"+newFileName, id);
							if(rowNum <= 0) {
								studentImageUploadVO = new StudentImageUploadVO(1003, "身份证图像上传失败！", null);
							} else {
								StudentEntity entity = studentImageUpload.getStudentInformation(phone);
								studentImageUploadVO = new StudentImageUploadVO(1004, "身份证图像上传成功！", entity);
							}
						} else {
							studentImageUploadVO = new StudentImageUploadVO(1005, "数据请求代码有误！", null);
						}
					} else {
						studentImageUploadVO = new StudentImageUploadVO(1006, "该用户的信息不正确！", null);
					}
				}
			} catch (FileUploadException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		out.write(new Gson().toJson(studentImageUploadVO));
		out.close();
	}
}
