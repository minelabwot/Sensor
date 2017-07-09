package com.yyn.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yyn.service.AnomalyService;
import com.yyn.service.DataService;
import com.yyn.util.NameSpaceConstants;
import com.yyn.util.RDFReasoning;

@Controller
@RequestMapping("/sensor_data*.do")
public class DataController {
	//	private static final String NS_SSN = "http://purl.oclc.org/NET/ssnx/ssn#";
	private static final String NS_WOT = "https://raw.githubusercontent.com/minelabwot/SWoT/master/swot-o.owl#";
//	private static final String NS_RDFS = "http://www.w3.org/2000/01/rdf-schema#";
//	private static final String NS_DUL = "http://www.loa-cnr.it/ontologies/DUL.owl#";

	@Autowired
	private DataService ds;

	@Autowired
	private AnomalyService as;
	/**
	 * 上传传感器数据的接口,并根据上传的数据判断是否超出规定范围应判定为异常事件.
	 * @param request 用于获取real path
	 * @param id 设备id
	 * @param value 设备采集的传感值
	 * @return 当前设备的详情页面
	 */
	@RequestMapping("/sensor_data_update.do")
	public String updateData(HttpServletRequest request,String id,String value) {
		Timestamp time = new Timestamp(System.currentTimeMillis());
		float val = Float.parseFloat(value);
//		ds.update_sensordata(id, time, val);
		//在这里添加异常判断

		Dataset ds = (Dataset)request.getSession().getServletContext().getAttribute("dataset");

		ds.begin(ReadWrite.READ);
		String queryString = StrUtils.strjoinNL(
				"PREFIX wot: <https://raw.githubusercontent.com/minelabwot/SWoT/master/swot-o.owl#> ",
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ",
				"PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> ",
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> ",
				"SELECT ?a ?pro",
				"WHERE {",
				"GRAPH wot:sensor_annotation {",
				"?a wot:deviceID '"+id+ "'^^xsd:string. ",
				"?type a wot:EntityType. ",
				"?a wot:hasType ?type. ",
				"?type wot:defaultObserved ?pro}}");
		QueryExecution qExec = QueryExecutionFactory.create(queryString, ds);
		ResultSet rs = qExec.execSelect();
		String name = null;
		String pro = null;
		while(rs.hasNext()) {
			QuerySolution qs = rs.next();
			name = qs.get("a").asResource().getLocalName();
			pro = qs.get("pro").asResource().getLocalName();
		}
		System.out.println(name+"---"+pro);
		qExec.close();
		ds.commit();
		ds.end();

		//写入当前设备的当前的温度值
		ds.begin(ReadWrite.WRITE);

		try {
			//每次更新传感值时同步
			String update = StrUtils.strjoinNL(NameSpaceConstants.PREFIX,
					"DELETE { GRAPH wot:sensor_annotation { ?device wot:hasValue ?val} } ",
					"INSERT { ",
					"GRAPH wot:sensor_annotation {",
					"?device wot:hasValue '"+val+"'^^xsd:float} } ",
					"USING wot:sensor_annotation ",
					"WHERE {?device wot:deviceID '"+id+ "'^^xsd:string. ",
					"?device ?rel ?val",
					"}"); //用于删除之前的选择,并且兼顾如果初始没有hasValue的情况
			RDFReasoning.updateQuery(update, ds);
			ds.commit();
		} finally {
			ds.end();
		}

		//产生设备当前状态
		as.createState(ds);

		String currentFile = request.getServletContext().getAttribute("currentFile").toString();

		File configFile = new File(new File(currentFile).getParent(),"config.properties");
		Properties proper = new Properties();
		try {
			proper.load(new FileInputStream(configFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		float temp_high = Float.parseFloat(proper.get(pro+"_high").toString());
		float temp_low = Float.parseFloat(proper.get(pro+"_low").toString());
		System.out.println(temp_high+"---"+temp_low);

		//只有温度,等检测性传感器可以产生异常

		if(pro != null) {
			if(val > temp_high || val < temp_low) {
				ds.begin(ReadWrite.WRITE);
				String s = null;
				if(val < temp_low) {
					s = "low";
					System.out.println("产生了低温异常");
				}
				else if(val > temp_high) {
					s = "high";
					System.out.println("产生了高温异常");
				}
				String update = StrUtils.strjoinNL(
						NameSpaceConstants.PREFIX,
//						"DELETE { GRAPH wot:sensor_annotation { ?device ssn:detects ?event. ",
//						"?anomaly ssn:observationSamplingTime ?time. } }",
						"INSERT { GRAPH wot:sensor_annotation { ?anomaly rdf:type wot:Anomaly. ",
						//同时生成致因实体
						"?cause rdf:type wot:ObservedCause. ",
//						"?anomaly wot:hasPotCause ?cause.",
						"?cause ssn:observationSamplingTime '"+time.toString()+"'^^xsd:dataTime. ",
						"?device ssn:detects ?anomaly. ",
						"?anomaly ssn:observationSamplingTime '"+time.toString()+"'^^xsd:dataTime. } }",
						"USING wot:sensor_annotation ",
						"WHERE { ?device wot:deviceID '"+id+"'^^xsd:string .",
						"?event a ?eventCls. ",
//						"?sub ?rel ?time. ",
						"BIND(URI('"+ NameSpaceConstants.WOT+"temp_"+s+"_"+id+"_cause') as ?cause). ",
						"BIND(URI('"+ NameSpaceConstants.WOT+"temp_"+s+"_"+id+"') as ?anomaly).",
						"}");
				System.out.println(update);
				RDFReasoning.updateQuery(update, ds);
				ds.commit();
				ds.end();
			}
			else {
				ds.begin(ReadWrite.WRITE);
				String update = StrUtils.strjoinNL(
						NameSpaceConstants.PREFIX,
						"DELETE { GRAPH wot:sensor_annotation { ?device wot:detects ?event. } } ",
						"USING wot:sensor_annotation ",
						"WHERE { ?device wot:deviceID '"+id+"'^^xsd:string .",
						"?event a ?eventCls. ",
						"?eventCls rdfs:subClassOf ssn:Stimulus. }");
				RDFReasoning.updateQuery(update, ds);
				RDFReasoning.updateQuery(update, ds);
				ds.commit();
				ds.end();
			}
		}
		RDFReasoning.output(ds);
		return "redirect:dynamic/goActuatorDetail.do?id="+id;
	}

	@RequestMapping("/sensor_data_diagnosis.do")
	public String diagnosis(HttpServletRequest request) {
		Dataset ds = (Dataset)request.getSession().getServletContext().getAttribute("dataset");
		as.generateDiagModel(ds);
		RDFReasoning.output(ds);
		return "servicePage/deviceList.jsp";
	}

}
