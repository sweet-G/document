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
		
		//�����ļ��ϴ���·��
		File file = new File("E:/upload");
		//�ж��ļ��ϴ�·���Ƿ����
		if(!file.exists()) {
			file.mkdirs();
		}
		
		//�����ļ��ϴ�����ʱ·��
		File tempFile = new File("E:/temp");
		if(!tempFile.exists()) {
			tempFile.mkdirs();
		}
		
		//�ж��ļ��ϴ���enctype�Ƿ�Ϊmultipart/form-data
		if(ServletFileUpload.isMultipartContent(req)) {
			//��ʼ�ϴ�
			DiskFileItemFactory factory = new DiskFileItemFactory();
			//���û�������С
			factory.setSizeThreshold(1024*10);
			//������ʱ�ļ���
			factory.setRepository(tempFile);
			//��ȡfactory��ServletFileUpload����
			ServletFileUpload upload = new ServletFileUpload(factory);
			try {
				//���fileItemList����
				//FileItem���Ͼ��Ǳ������е�Ԫ��(��ͨԪ�غ��ļ�Ԫ��)
				List<FileItem> fileListItem = upload.parseRequest(req);
				
				for(FileItem item :fileListItem) {
					if(item.isFormField()) {
						//��ͨԪ��
						String filedName = item.getFieldName();
						String value = item.getString("UTF-8");
					} else {
						//�ļ�Ԫ��
						String filedName = item.getFieldName();
						String name = item.getName();
						
						//����Ψһ���ļ���
						name = UUID.randomUUID().toString() + name.substring(name.lastIndexOf("."));
						
						//���������
						InputStream input = item.getInputStream();
						FileOutputStream out = new FileOutputStream(new File(file,name));
						
						IOUtils.copy(input, out);
						
						out.flush();
						out.close();
						input.close();
						
						System.out.println("�ļ��ϴ��ɹ�");
						
					}
				}
				
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
			
			
		}
		
	}
}
