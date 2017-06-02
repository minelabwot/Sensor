package com.yyn.service;

import com.yyn.dao.BaseDeviceDao;
import com.yyn.model.WotProperty;
import com.yyn.util.NameSpaceConstants;
import com.yyn.util.RDFReasoning;
import com.yyn.util.ResolveRule;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by koala on 2017/5/17.
 */

@Service
public class EditBaseOwlService {

    @Autowired
    private BaseDeviceDao mDao;

    public WotProperty getWotProperty(String file){
        OntModel model = ModelFactory.createOntologyModel();
        WotProperty property = new WotProperty();
        FileManager.get().readModel(model,new File(file).getAbsolutePath());
        OntClass ontClass = model.getOntClass("http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#PropertyLabel");

        if (ontClass == null){
            return null;
        }
        ExtendedIterator<OntClass> iterable = ontClass.listSubClasses();
        if (iterable == null){
            return null;
        }
        List<String> concepts = new ArrayList<>();
        Map<String,String> conceptUriMap = new HashMap<>();
        concepts.add("name");
        concepts.add("description");
        Map<String,List<String>> map = new HashMap<>();
        Map<String,Boolean> selectmap = new HashMap<>();
        while (iterable.hasNext()){
            OntClass subclass = iterable.next();
            conceptUriMap.put(subclass.getLocalName(),subclass.getURI());
            if ( subclass != null && subclass.getLocalName() != null){
                concepts.add(subclass.getLocalName());
                ExtendedIterator<OntClass> subIterable = subclass.listSubClasses();
                List<String> subconcept = new ArrayList<>();
                selectmap.put(subclass.getLocalName(),false);
                while (subIterable.hasNext()){
                    OntClass subclass2 = subIterable.next();
                    if (subclass2 != null && subclass2.getLocalName() != null){
                        subconcept.add(subclass2.getLocalName());
                        selectmap.put(subclass.getLocalName(),true);
                    }
                }
                map.put(subclass.getLocalName(),subconcept);
            }
        }

        System.out.println(selectmap.toString());
        property.setConcepts(concepts);
        property.setMap(map);
        property.setIsselect(selectmap);
        property.setConceptUriMap(conceptUriMap);
        property.setFileName(file);
        System.out.println(conceptUriMap);
        return property;
    }

    public void insertDevice(String sql){
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
    }

    public List<Map<String,String>> selectAllDevice(String tableName){
        System.out.println(tableName);
        return mDao.getDeviceByTable(tableName);

    }

    public Integer getLastId(String tableName){
        if (mDao.searchLastId(tableName) == null){
            return 0;
        }
        return mDao.searchLastId(tableName);
    }

    public String pareSparql(String file,WotProperty property, HttpServletRequest request,int id){

        String prefix = ResolveRule.getSprqlPre(file);
        List<String> concepts = property.getConcepts();
        Map<String,String> map = property.getConceptUriMap();



        List<String> res = new ArrayList<>();
        res.add(prefix);
        String device_type = "ssn:Sensor";
        res.add("INSERT{");
        res.add("GRAPH "+"WoT_domain:sensor_annotation {");
        res.add("?device rdf:type "+device_type+". ");
        res.add("?device WoT_domain:deviceID \""+id+"\"^^xsd:string. ");

        concepts.remove("name");
        concepts.remove("description");
        String NS = map.get(concepts.get(0)).split("#")[0]+"#";
        for (int i=0;i<concepts.size();i++){
            String concept = concepts.get(i);
            res.add("?"+concept.toLowerCase()+" rdf:type WoT_domain:"+concept+". ");

            res.add("?device"+ " ?a"+i+" ?"+concept.toLowerCase()+". ");
        }
        res.add("?device WoT_domain:hasState WoT_domain:nomal. ");
        res.add("}");
        res.add("} USING "+ "WoT_domain:sensor_annotation ");
        res.add("where {");
        for (int i=0;i<concepts.size();i++){
            String concept = concepts.get(i);
            res.add("?a"+i+" rdfs:domain "+device_type+" ;");
            res.add("rdfs:range WoT_domain:"+concept+".");
            res.add("BIND(URI(CONCAT(\""+NS+"\",\""+request.getParameter(concepts.get(i))+"\")) as ?"+concepts.get(i).toLowerCase()+"). ");
        }
        res.add("BIND(URI(CONCAT(\""+NS+"\",\""+request.getParameter("name")+"\")) as ?device) }");

//        String update = StrUtils.strjoinNL(res);
//        System.out.println(update);

        return StrUtils.strjoinNL(res);
    }

}
