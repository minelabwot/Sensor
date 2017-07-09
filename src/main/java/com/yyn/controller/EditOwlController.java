package com.yyn.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yyn.config.TDBConfig;
import com.yyn.dao.OwlDao;
import com.yyn.model.MyOwl;
import com.yyn.model.Owldata;
import com.yyn.model.User;
import com.yyn.service.EditOwlService;
import com.yyn.util.NameSpaceConstants;
import com.yyn.util.RDFReasoning;

@Controller
public class EditOwlController {

	@Autowired
	EditOwlService service;

	@Autowired
	OwlDao mOwlDao;

	@RequestMapping("listOwl.do")
	public String listOwl(HttpServletRequest request,Model model){
		User user = (User) request.getSession().getAttribute("userInfo");
		if (user == null){
			return "login/login.html";
		}
		System.out.println(request.getSession().getAttribute("userInfo"));
		List<MyOwl> owls = service.listOwl(request,user.getId());
		model.addAttribute("owllist", owls);
		model.addAttribute("noProperty",request.getParameter("noProperty"));
		return "expand/owllist.jsp";
	}
	
	@RequestMapping("deleteOwl.do")
	public String deleteOwl(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		service.deleteOwlById(Integer.valueOf(id));
		return "redirect:/listowl.do";
	}
	
	@RequestMapping("editOwl.do")
	public String editOwl(HttpServletRequest request,Model model){
		model.addAttribute("currentOwl",request.getParameter("owlfile"));
		System.out.println(request.getParameter("owlfile"));
		request.getServletContext().setAttribute("currentOwl",request.getParameter("owlfile"));
		Owldata owldata = service.listConcepts(request,request.getParameter("owlfile"));
		request.getServletContext().setAttribute("owldata", owldata);
		return "expand/editowl.jsp";
	}
//
//	@RequestMapping("startup.do")
//	public String startUp(HttpSession session){
//
////		if (session.getServletContext().getAttribute("dataset") != null){
////			return "";
////		}
//
//		Thread t = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				while(true) {
//					User user = (User) session.getAttribute("userInfo");
//					if (user == null){
//						continue;
//					}
//					try {
//						Thread.sleep(1000*10);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					List<MyOwl> owlList = mOwlDao.queryAll(1);
//					Dataset dataset = RDFReasoning.getDataset(owlList);
//					session.getServletContext().setAttribute("dataset", dataset);
//					session.getServletContext().setAttribute("owlList",owlList);
//					System.out.println("Start up Listener execute, dataset  has been set to context");
//					for (MyOwl owl:owlList){
//						String rule = owl.getRule();
//						String[] rules = rule.split("000000");
//						for (int i=0;i<rules.length;i++){
//							String item = rules[i];
//							if (item != null && !"".equals(item)){
//								if (item.startsWith("PREFIX")){
//									try{
//										dataset.begin(ReadWrite.WRITE);
//										RDFReasoning.updateQuery(item,dataset);
//										dataset.commit();
//									}catch (Exception e){
//
//									}finally {
//										dataset.end();
//									}
//
//								}else {
//									System.out.println(item);
//
//
//									File file = new File(TDBConfig.file,"rule.rule");
//
//
//									try {
//										FileWriter writer = new FileWriter(file);
//										writer.write(item);
//										writer.close();
//									} catch (IOException e) {
//										e.printStackTrace();
//									}
//
//									try{
//										dataset.begin(ReadWrite.WRITE);
//										org.apache.jena.rdf.model.Model model = dataset.getNamedModel(com.yyn.config.NameSpaceConstants.WOT+"sensor_annotation");
//										List<Rule> rules1 = Rule.rulesFromURL(file.getAbsolutePath());
//
//										System.out.println("mark"+rules1.size()+rules1.get(0).toString());
//										Reasoner reasoner = new GenericRuleReasoner(rules1);
//										reasoner.setDerivationLogging(true);
//										InfModel inf = ModelFactory.createInfModel(reasoner, model);
//										dataset.addNamedModel(com.yyn.config.NameSpaceConstants.WOT+"sensor_annotation", inf);
//										dataset.commit();
//
//									}catch (Exception e){
//
//									}finally {
//										dataset.end();
//									}
//
//								}
//
//							}
//						}
//					}
////						as.generateDiagModel(dataset);
//					RDFReasoning.output(dataset,owlList);
//
//				}
//			}
//		});
//		t.start();
//		return "";
//	}

}
