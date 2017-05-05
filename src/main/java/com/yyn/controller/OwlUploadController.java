package com.yyn.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.aspectj.apache.bcel.classfile.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class OwlUploadController {

	@RequestMapping("owlupdate.do")
	public String uploadOwl(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
		if (null == file)
			throw new Exception("上传失败,文件为空");
		if (file.getSize() > 20000000)
			throw new Exception("上传失败,文件不能超过20M");
		if (file.getSize() > 0) {
			// 文件存放路径，上传的文件放在项目的upload文件夹下
			String savePath = request.getSession().getServletContext()
					.getRealPath("/WEB-INF/upload/owl");
			File file2 = new File(savePath);
			if (!file2.exists()) {
				file2.mkdir();
			}
			String realName = file.getOriginalFilename();
			System.out.println(savePath + "_" + realName);
			file.transferTo(new File(savePath + File.separator + realName));
		}
		return "redirect:/index.jsp";
	}

	@RequestMapping("owlexpand.do")
	public String owlexpand() {
		return "expand/uploadowl.jsp";
	}
}
