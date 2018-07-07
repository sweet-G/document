package com.kaishengit.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.kaishengit.web.BaseServlet;


//@WebServlet("/download")
public class DownloadServlet extends BaseServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String fileName = req.getParameter("file"); 
		String name = req.getParameter("name"); 
		
		fileName = new String(fileName.getBytes("ISO8859-1"), "UTF-8");
		// name = new String(name.getBytes("ISO8859-1"), "UTF-8");
		
		File file = new File("e:/upload", fileName);
		if(file.exists()) {
			FileInputStream input = new FileInputStream(file);
			OutputStream output = resp.getOutputStream();
			
			// 如果name不为空，则下载图片
			if(StringUtils.isNotEmpty(name)) {
				// 设置mime类型
				resp.setContentType("application/octet-stream");
				// 设置文件大小
				resp.setContentLength((int)file.length());
				// 设置下载文件名
				// name = new String(name.getBytes("UTF-8"), "ISO8859-1");
				resp.addHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
			}
			
			IOUtils.copy(input, output);
			
			output.flush();
			output.close();
			input.close();
			
		} else {
			resp.sendError(404,"该资源未找到");
		}
		
	}
	
}