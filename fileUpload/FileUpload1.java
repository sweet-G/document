package com.kaishengit.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.kaishengit.web.BaseServlet;

@WebServlet("/upload")
public class FileServlet extends BaseServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		forward("test/upload", req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//设置文件上传的路径
		File file = new File("E:/upload");
		//判断文件上传路径是否存在
		if(!file.exists()) {
			file.mkdirs();
		}
		
		//设置文件上传的临时路径
		File tempFile = new File("E:/temp");
		if(!tempFile.exists()) {
			tempFile.mkdirs();
		}
		
		//判断文件上传的enctype是否为multipart/form-data
		if(ServletFileUpload.isMultipartContent(req)) {
			//开始上传
			DiskFileItemFactory factory = new DiskFileItemFactory();
			//设置缓冲区大小
			factory.setSizeThreshold(1024*10);
			//设置临时文件夹
			factory.setRepository(tempFile);
			//获取factory的ServletFileUpload对象
			ServletFileUpload upload = new ServletFileUpload(factory);
			try {
				//获得fileItemList集合
				//FileItem集合就是表单中所有的元素(普通元素和文件元素)
				List<FileItem> fileListItem = upload.parseRequest(req);
				
				for(FileItem item :fileListItem) {
					if(item.isFormField()) {
						//普通元素
						String filedName = item.getFieldName();
						String value = item.getString("UTF-8");
					} else {
						//文件元素
						String filedName = item.getFieldName();
						String name = item.getName();
						
						//生成唯一的文件名
						name = UUID.randomUUID().toString() + name.substring(name.lastIndexOf("."));
						
						//获得输入流
						InputStream input = item.getInputStream();
						FileOutputStream out = new FileOutputStream(new File(file,name));
						
						IOUtils.copy(input, out);
						
						out.flush();
						out.close();
						input.close();
						
						System.out.println("文件上传成功");
						
					}
				}
				
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
			
			
		}
		
	}
}
