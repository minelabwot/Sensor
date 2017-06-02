package com.yyn.service;

import javax.servlet.ServletContext;

import org.apache.jena.query.Dataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.yyn.dao.OwlDao;
import com.yyn.model.MyOwl;
import com.yyn.model.Owldata;
import com.yyn.util.RDFReasoning;

import java.util.List;

@Component
public class StartupListener implements ServletContextAware{
	@Autowired
	AnomalyService as;

	@Autowired
	OwlDao mOwlDao;

	@Override
	public void setServletContext(ServletContext sc) {
		//System.out.println(sc.getInitParameter("contextConfigLocation"));
		// TODO Auto-generated method stub
		String tdbRoot = "H:/sensor/upload/wot4/";

		List<MyOwl> owlList = mOwlDao.queryAll();
//		MyOwl owl = owlList.get(0);
		Dataset dataset = RDFReasoning.getDataset(owlList);
		sc.setAttribute("dataset", dataset);
		sc.setAttribute("owlList",owlList);
		System.out.println("Start up Listener execute, dataset  has been set to context");

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true) {
					try {
						Thread.sleep(1000*20);
						as.generateDiagModel(dataset);
						RDFReasoning.output(dataset,owlList);
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
