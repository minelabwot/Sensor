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
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;
import org.aspectj.apache.bcel.classfile.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.yyn.model.User;
import com.yyn.service.OwlUploadService;
import com.yyn.util.RDFReasoning;

@Controller
public class OwlUploadController {

	@Autowired
	OwlUploadService mService;

	@RequestMapping("owlupdate.do")
	public String uploadOwl(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("userInfo");
		if (user == null){
			return "redirect:views/login/login.html";
		}
		if (null == file)
			throw new Exception("上传失败,文件为空");
		if (file.getSize() > 20000000)
			throw new Exception("上传失败,文件不能超过20M");
		if (file.getSize() > 0) {
			// 文件存放路径，上传的文件放在项目的upload文件夹下
			String savePath = "H:/sensor/upload/";
			File file2 = new File(savePath);
			if (!file2.exists()) {
				file2.mkdir();
			}

			String name = request.getParameter("name");

			String description = request.getParameter("description");
			String uri = request.getParameter("uri");
			String realName = file.getOriginalFilename();
			String root = savePath+name+"/";
			System.out.println(savePath + "_" + realName);
			File rootFile = new File(root);
			if (!rootFile.exists()){
				rootFile.mkdirs();
			}
			File out = new File(root + File.separator + realName);
			file.transferTo(out);


			Dataset ds = (Dataset) request.getServletContext().getAttribute("dataset");
			OntModel model = ModelFactory.createOntologyModel();
			FileManager.get().readModel(model, out.getAbsolutePath());
			mService.createNewTable(realName.replace(".","").toLowerCase(),model);

			ds.begin(ReadWrite.WRITE);
			ds.addNamedModel(realName, model);
			ds.commit();
			ds.end();

			mService.saveOwl(name,description,realName,root,uri,user.getId());
		}
		return "redirect:/listowl.do";
	}

	@RequestMapping("owlUpdateAndExpand.do")
	public String uploadOwlExpand(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
		if (null == file)
			throw new Exception("上传失败,文件为空");
		if (file.getSize() > 20000000)
			throw new Exception("上传失败,文件不能超过20M");
		if (file.getSize() > 0) {
			// 文件存放路径，上传的文件放在项目的upload文件夹下
			String savePath = "H:/sensor/upload/owl/";
			File file2 = new File(savePath);
			if (!file2.exists()) {
				file2.mkdir();
			}
			String realName = file.getOriginalFilename();
			System.out.println(savePath + "_" + realName);
			File out = new File(savePath + File.separator + realName);
			file.transferTo(out);
			
			String originFile = (String) request.getServletContext().getAttribute("updateAndExpand");
			System.out.println(originFile);
			File origin = new File(savePath+File.separator+originFile);
			Model model1 = ModelFactory.createDefaultModel();
			FileManager.get().readModel(model1, out.getAbsolutePath());
			
			Model model2 = ModelFactory.createDefaultModel();
			FileManager.get().readModel(model2, origin.getAbsolutePath());
			
			model1.union(model2);
			
			System.out.println(out.delete());
			
//			RDFReasoning.outputModel(model1, origin.getAbsolutePath());
			
			Dataset ds = (Dataset)request.getServletContext().getAttribute("dataset");
			ds.begin(ReadWrite.WRITE);
			ds.addNamedModel(realName, model1);
			ds.commit();
		}
		return "redirect:/listowl.do";
	}
	
	@RequestMapping("addOwl.do")
	public String addOwl() {
		return "expand/uploadowl.jsp";
	}
	
	@RequestMapping("expandOwl.do")
	public String expandOwl(HttpServletRequest request) { 
		String name = (String) request.getParameter("owlfile");
		System.out.println(name+"-------");
		request.getServletContext().setAttribute("updateAndExpand", name);
		return "expand/expand.jsp";
	}
}
