package com.yyn.service;

import com.yyn.dao.OwlDao;
import com.yyn.model.MyOwl;
import com.yyn.model.Owldata;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OwlUploadService {

	@Autowired
	OwlDao mDao;
	public void saveOwl(String name,String des,String fileName,String root){
		mDao.inserOwl(new MyOwl(name,des,fileName,root));
	}

	public void createNewTable(String tablename, OntModel model){
		OntClass ontClass = model.getOntClass("http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#PropertyLabel");

		ExtendedIterator<OntClass> iterable = ontClass.listSubClasses();
		List<String> concepts = new ArrayList<>();
		StringBuilder sb = new StringBuilder("create table "+tablename+" ( `id` int(11) primary key not null auto_increment, `name` varchar(255), `description` varchar(255),");
		while (iterable.hasNext()){
			OntClass subclass = iterable.next();
			if (subclass != null && subclass.getLocalName() != null){
				sb.append(" `"+subclass.getLocalName().toLowerCase()+"` varchar(255),");
			}
		}

		String str = sb.toString();
		String sql = str.substring(0,str.length()-1)+");";
		System.out.println(sql);

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/sensor_annotation?serverTimezone=PRC&useUnicode=true&characterEncoding=UTF-8&useSSL=false";
			Connection connection = DriverManager.getConnection(url,"root","111111");
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.execute();
			ps.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

//		mDao.createTable(sql);
	}

}
