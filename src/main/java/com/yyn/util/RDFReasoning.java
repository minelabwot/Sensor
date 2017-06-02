package com.yyn.util;

import com.yyn.config.TDBConfig;
import com.yyn.model.MyOwl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.tdb.TDB;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.util.FileManager;

public class RDFReasoning {
	//	public static void main(String[] args) {
//		String path = "/Users/yangyunong/javaworkspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/sensor_annotation/WEB-INF/RDF_Database/";
//		Dataset ds = getDataset(path, "sensor_annotation", "");
//		String prefix = StrUtils.strjoinNL(
//				"PREFIX wot: <http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#> ",
//				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ",
//				"PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> ",
//				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ");
//
//		String queryString1 = " select DISTINCT ?s1 ( sql:rnk_scale ( <LONG::IRI_RANK> ( ?s1 ) ) ) as ?rank " +
//				"where {" +
//				"?s1 rdfs:label ?o1 ." +
//				"?o1 bif:contains \"(' "+ "Beijing" + "' )\" ." +
//				"Filter regex (str(?s1),\"resource\",\"ontology\") ."+
//				"} "+
//				"order by desc ( ?rank )";
//
//		String queryString2 = "SELECT DISTINCT ?loc "+
//				"WHERE { "+
//				"SERVICE <http://dbpedia.org/sparql> { "+
//				"<http://dbpedia.org/resource/Beijing> ?rel ?loc. " +
//				"FILTER regex(str(?loc),\"China\") }}";
//
//		ResultSet rs = RDFReasoning.selectQuery(queryString2,ds);
//		ResultSetFormatter.out(rs);
//	}
	public static void first() {
		String queryString2 = "SELECT DISTINCT ?loc "+
				"WHERE { "+
				"<http://dbpedia.org/resource/Beijing> ?rel ?loc. " +
				"FILTER regex(str(?loc),\"China\") }";
		QueryEngineHTTP qe = new QueryEngineHTTP("http://dbpedia.org/sparql", queryString2);
		ResultSet rs = qe.execSelect();
		ResultSetFormatter.out(rs);
		qe.close();
	}
	public static Dataset getDataset(String fileName) {
		File file = new File(TDBConfig.root);
		if(!file.exists()) {
			Dataset ds = TDBFactory.createDataset(file.getAbsolutePath());
			Model model = ModelFactory.createDefaultModel();
			ds.begin(ReadWrite.WRITE);
			try {
				FileManager.get().readModel(model, fileName);
				ds.addNamedModel("http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#",model);
				ds.commit();
			} finally {
				ds.end();
			}
			model.close();
			ds.close();
		}
		return TDBFactory.createDataset(file.getAbsolutePath());
	}

	public static Dataset getDataset(List<MyOwl> owlList) {
		File file = new File(TDBConfig.root);
		if(!file.exists()) {
			Dataset ds = TDBFactory.createDataset(file.getAbsolutePath());

			ds.begin(ReadWrite.WRITE);
			try {
				for (MyOwl owl:owlList){
					System.out.println(owl);
					Model model = ModelFactory.createDefaultModel();
					FileManager.get().readModel(model, owl.getRoot()+owl.getFile());
					ds.addNamedModel("WoT_domain:sensor_annotation",model);
					model.close();
				}
				ds.commit();
			} finally {
				ds.end();
			}

			ds.close();
		}
		return TDBFactory.createDataset(file.getAbsolutePath());
	}

	public static ResultSet selectQuery(String queryString,Dataset dataset) {
		QueryExecution qExec = QueryExecutionFactory.create(queryString, dataset);
		ResultSet rs = qExec.execSelect();
		//ResultSetFormatter.out(rs);
		return rs;
	}

	public static ResultSet remoteQuery(String queryString) {
		QueryExecution qExec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", queryString);
		ResultSet rs = qExec.execSelect() ;
		return rs;
	}

	public static void updateQuery(String updateString,Dataset dataset) {
//		System.out.println(updateString);
		UpdateRequest request = UpdateFactory.create(updateString) ;
		UpdateProcessor proc = UpdateExecutionFactory.create(request, dataset) ;
		proc.execute() ;
	}

	public static void output(Dataset ds,String output){
		FileOutputStream fos = null;
		ds.begin(ReadWrite.READ);
		try{
			fos = new FileOutputStream(output);
			Model model = ds.getDefaultModel();
			model.getWriter().write(model, fos,null);
			ds.commit();
			ds.end();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("更新了output.owl");
	}

	public static void output(Dataset ds,List<MyOwl> owlList){

		ds.begin(ReadWrite.READ);
		try{
			for (MyOwl  owl:owlList){
				FileOutputStream fos = new FileOutputStream(owl.getRoot()+"output.owl");
				System.out.println(owl.getRoot()+"output.owl");
				Model model = ds.getNamedModel(owl.getName());
				model.getWriter().write(model, fos,null);
				fos.close();
			}

			ds.commit();
			ds.end();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("更新了output.owl");
	}

	public static void output(Dataset ds){
		FileOutputStream fos = null;
		ds.begin(ReadWrite.READ);
		try{
			fos = new FileOutputStream("H:/sensor/out.owl");
			Model model = ds.getDefaultModel();
			model.getWriter().write(model, fos,null);
			ds.commit();
			ds.end();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("更新了output.owl");
	}
}
