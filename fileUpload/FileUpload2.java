package com.kaishengit.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.kaishengit.web.BaseServlet;

@WebServlet("/upload2")
@MultipartConfig
public class FileServlet2 extends BaseServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		forward("test/upload2", req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		// 1.使用servlet3.1文件上传可以通过req.getParamaeter(String name)的方法获得普通表单的值
		String desc = req.getParameter("desc");
		System.out.println("desc:" + desc);
		
		// 2.获得文件元素
		Part part = req.getPart("file");
		
		/*System.out.println(part.getName());
		System.out.println(FileUtils.byteCountToDisplaySize(part.getSize()));
		System.out.println(part.getContentType()); */

		// 2.1 原始文件名称
		String contentDisposition = part.getHeader("Content-Disposition");
		System.out.println("Disposition:" + contentDisposition);
		
		String fileName = contentDisposition.split(";")[2].split("\"")[1];
		
		fileName = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."));
		
		// 2.2 文件的二进制流
		InputStream input = part.getInputStream();
		OutputStream output = new FileOutputStream(new File("e:/upload",fileName));
		
		IOUtils.copy(input, output);
		
		output.flush();
		output.close();
		input.close();
		
		System.out.println("文件上传成功");
	}
}
