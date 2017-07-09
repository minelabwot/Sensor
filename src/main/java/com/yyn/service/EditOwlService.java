package com.yyn.service;

import com.yyn.dao.OwlDao;
import com.yyn.model.MyOwl;
import com.yyn.model.Owldata;
import com.yyn.util.RDFReasoning;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.ontology.impl.IndividualImpl;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditOwlService {

	@Autowired
	OwlDao mDao;
	public List<MyOwl> listOwl(HttpServletRequest request,int userId){
		return mDao.queryAll(userId);
	}

	public void deleteOwlByFile(String file){
		mDao.deleteByFile(file);
	}

	public void deleteOwlById(int id){
		mDao.deleteById(id);
	}

	public Owldata listConcepts(HttpServletRequest request, String name){
		OntModel model = ModelFactory.createOntologyModel();
		File file = new File(name);
		FileManager.get().readModel(model,file.getAbsolutePath());
		return findAllConcepts(model);
	}

	private Owldata findAllConcepts(OntModel ontModel) {
		Owldata owldata = new Owldata();
		Map<String,String> uriMap = new HashMap<>();
		Map<String,String> prefixMap = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, ontModel).getNsPrefixMap();
		owldata.setPrefixs(prefixMap);

		Map<String,List<String>> map = new HashMap<>();

		List<String> conceptslist = new ArrayList<>();
		String classStr = null;
		List<OntClass> concepts = new ArrayList<OntClass>();

		for (Iterator<OntClass> allConcepts = ontModel.listClasses(); allConcepts.hasNext();) {
			OntClass oConcept = allConcepts.next();
			classStr = oConcept.getLocalName();

			if (classStr != null && classStr != "") {
				concepts.add(oConcept);
			}
		}
		//concepts为过滤后所有概念的集合
		for (Iterator<OntClass> allConcepts = concepts.iterator();allConcepts.hasNext();) {
			OntClass oConcept = allConcepts.next();
			classStr = oConcept.getLocalName();
			uriMap.put(classStr,oConcept.getURI());

			conceptslist.add(classStr);
			List<String> instances = new ArrayList<>();
			instances.clear();
			ExtendedIterator iterator = oConcept.listInstances();
			String sClassStr;
			while (iterator.hasNext()) {
				Individual individual = (Individual) iterator.next();
				sClassStr = individual.getLocalName();
				if (sClassStr != null && sClassStr != "") {
//					System.out.println("实例：" + sClassStr);
//					System.out.println(resource.getURI());
					uriMap.put(sClassStr, URLEncoder.encode(individual.getURI()));
					instances.add(sClassStr);
				}


			}
			map.put(classStr,instances);

		}
		owldata.setConcepts(conceptslist);
		owldata.setMap(map);
		owldata.setNameUriMap(uriMap);
		return owldata;
	}

//	public void deleteInstance(String uri,String file,OntModel model){
//		Iterator iterator = model.listClasses();
//		while (iterator.hasNext()){
//			OntClass ontClass = (OntClass) iterator.next();
//			Iterator instanceiterator = ontClass.listInstances();
//			while (instanceiterator.hasNext()){
//				Individual individual = (Individual) instanceiterator.next();
//				if (individual != null && individual.getURI().equals(uri)){
//					individual.removeOntClass(ontClass.asResource());
//					System.out.println("删除操作"+individual.getURI());
//					try {
//						RDFReasoning.outputModel(model,file);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					return;
//				}
//			}
//		}
//	}
}
