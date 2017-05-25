package com.yyn.service;

import java.io.File;

import javax.servlet.ServletContext;

import org.apache.jena.query.Dataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.yyn.util.RDFReasoning;

@Component
public class StartupListener implements ServletContextAware{
	@Autowired
	AnomalyService as;
	
	@Override
	public void setServletContext(ServletContext sc) {
		//System.out.println(sc.getInitParameter("contextConfigLocation"));
		// TODO Auto-generated method stub
		String tdbRoot = "H:/sensor/RDF_Database/";
		String addRoot = "H:/sensor/upload/owl/";

		File tdbFile = new File(tdbRoot);
		if (!tdbFile.exists()){
			tdbFile.mkdirs();
		}

		File addFile = new File(addRoot);
		if (!addFile.exists()){
			addFile.mkdirs();
		}

		Dataset dataset = RDFReasoning.getDataset(tdbRoot, "sensor_annotation", "wot.owl",addRoot);

		sc.setAttribute("dataset", dataset);
		System.out.println("Start up Listener execute, dataset  has been set to context");

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true) {
					try {
						Thread.sleep(1000*20);
						as.generateDiagModel(dataset,sc);
						RDFReasoning.output(dataset);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
	}
	
}
