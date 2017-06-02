package com.yyn.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yyn.model.MyOwl;
import com.yyn.model.Owldata;
import com.yyn.service.EditOwlService;
import com.yyn.util.NameSpaceConstants;
import com.yyn.util.RDFReasoning;

@Controller
public class EditOwlController {

	@Autowired
	EditOwlService service;
	@RequestMapping("listowl.do")
	public String listOwl(HttpServletRequest request,Model model){
		List<MyOwl> owls = service.listOwl(request);
		model.addAttribute("owllist", owls);
		model.addAttribute("baseOwl","H:/sensor/RDF_Database/wot.owl");
		model.addAttribute("noProperty",request.getParameter("noProperty"));
		return "expand/owllist.jsp";
	}
	
	@RequestMapping("deleteOwl.do")
	public String deleteOwl(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
//		model.addAttribute("currentOwl",owlfile);
//		File file = new File(owlfile);
//		if (file.exists()) {
//			System.out.println(file.getAbsolutePath());
//			System.out.println(file.delete());
//		}
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

//	@RequestMapping("deleteInstance.do")
//	public String deleteInstance(HttpServletRequest request,Model model){
//		String uri = request.getParameter("instance");
//		String concept = request.getParameter("concept");
//		String currentFile = (String) request.getServletContext().getAttribute("currentOwl");
//		System.out.println(uri);
//		System.out.println(concept);
//		Dataset dataset = (Dataset) request.getServletContext().getAttribute("dataset");
//
//		String prefix = StrUtils.strjoinNL("PREFIX wot: <" + NameSpaceConstants.WOT + "> ",
//				"PREFIX rdf: <" + NameSpaceConstants.RDF + "> ", "PREFIX ssn: <" + NameSpaceConstants.SSN + "> ",
//				"PREFIX rdfs: <" + NameSpaceConstants.RDFS + "> ");
//		String queryString = "PREFIX untitled-ontology-13: <http://www.semanticweb.org/yangyunong/ontologies/2015/11/untitled-ontology-13#> "
//				+"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>. "
//				+"DELETE DATA { <"+uri+"> rdf:type untitled-ontology-13:Senior}";
//
//		dataset.begin(ReadWrite.WRITE);
//		RDFReasoning.updateQuery(queryString,dataset);
//		dataset.commit();
//
//
//		model.addAttribute("curren tOwl",request.getParameter("owlfile"));
//		System.out.println(request.getParameter("owlfile"));
//		request.getServletContext().setAttribute("currentOwl",request.getParameter("owlfile"));
//		Owldata owldata = service.listConcepts(request,request.getParameter("owlfile"));
//		request.getServletContext().setAttribute("owldata", owldata);
//		return "expand/editowl.jsp";
//	}

}
