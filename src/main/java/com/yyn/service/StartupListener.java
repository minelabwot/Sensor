package com.yyn.service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.swing.plaf.synth.SynthTextAreaUI;

import org.apache.jena.atlas.io.PrintUtils;
import org.apache.jena.fuseki.embedded.FusekiEmbeddedServer;
import org.apache.jena.fuseki.server.*;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.system.Txn;
import org.apache.jena.tdb.transaction.TxnState;
import org.apache.jena.util.PrintUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.yyn.config.NameSpaceConstants;
import com.yyn.config.TDBConfig;
import com.yyn.dao.OwlDao;
import com.yyn.model.MyOwl;
import com.yyn.model.Owldata;
import com.yyn.model.User;
import com.yyn.util.RDFReasoning;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Component
public class StartupListener implements ServletContextAware{
	@Autowired
	AnomalyService as;

	@Autowired
	OwlDao mOwlDao;

	@Override
	public void setServletContext(ServletContext sc) {
		List<MyOwl> owlList = mOwlDao.queryAll(1);
		Dataset dataset = RDFReasoning.getDataset(owlList);
//		FusekiEmbeddedServer server = FusekiEmbeddedServer.create()
//				.add("ds", dataset,true).build();
//		server.start();
		sc.setAttribute("dataset", dataset);
		sc.setAttribute("owlList",owlList);
		System.out.println("Start up Listener execute, dataset  has been set to context");

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true) {
					try {
						Thread.sleep(1000*10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for (MyOwl owl:owlList){
						String rule = owl.getRule();
						String[] rules = rule.split("000000");
						for (int i=0;i<rules.length;i++){
							String item = rules[i];
							if (item != null && !"".equals(item)){
								if (item.startsWith("PREFIX")){
									try{
										dataset.begin(ReadWrite.WRITE);
										RDFReasoning.updateQuery(item,dataset);
										dataset.commit();
									}catch (Exception e){

									}finally {
										dataset.end();
									}

								}else {
//									System.out.println(item);
									File file = new File(TDBConfig.file,"rule.rule");
									try {
										FileWriter writer = new FileWriter(file);
										writer.write(item);
										writer.close();
									} catch (IOException e) {
										e.printStackTrace();
									}

									try{
										dataset.begin(ReadWrite.WRITE);
										Model model = dataset.getNamedModel(NameSpaceConstants.WOT+"sensor_annotation");
										List<Rule> rules1 = Rule.rulesFromURL(file.getAbsolutePath());

//										System.out.println("mark"+rules1.size()+rules1.get(0).toString());
										Reasoner reasoner = new GenericRuleReasoner(rules1);
										reasoner.setDerivationLogging(true);
										InfModel inf = ModelFactory.createInfModel(reasoner, model);
										dataset.addNamedModel(NameSpaceConstants.WOT+"sensor_annotation", inf);
										dataset.commit();

									}catch (Exception e){

									}finally {
										dataset.end();
									}

								}

							}
						}
					}
//						as.generateDiagModel(dataset);
					RDFReasoning.output(dataset,owlList);

				}
			}
		});
		t.start();
	}

}
