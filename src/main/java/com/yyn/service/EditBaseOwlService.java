package com.yyn.service;

import com.yyn.dao.BaseDeviceDao;
import com.yyn.model.WotProperty;
import com.yyn.util.ResolveRule;

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
        OntClass ontClass = model.getOntClass("http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#WoTProperty");

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
            if ( (subclass.getSuperClass().equals(ontClass) || subclass.getSuperClass().getLocalName() == null) && subclass != null && subclass.getLocalName() != null){
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

    public String pareSparql(String file,WotProperty property, HttpServletRequest resquest){

        String prefix = ResolveRule.getSprqlPre(file);
        List<String> concepts = property.getConcepts();
        Map<String,String> map = property.getConceptUriMap();

        List<String> res = new ArrayList<>();
        String device_type = "ssn:Sensor";
        res.add("insert{");
        res.add("GRAPH wot:sensor_annotation {");
        res.add("?device rdf:type "+device_type+". ");
//                "?region rdf:type wot:Region. ",
//                device_type +"?a1 wot:Region. ",
//                "?device"+ "?a1"+"?region. ",

        for (int i=0;i<concepts.size();i++){
            String concept = concepts.get(i);
            res.add("?"+concept.toLowerCase()+" rdf:type "+map.get(concept)+". ");
            res.add(device_type +"?a"+i+" "+map.get(concept)+". ");
            res.add("?device"+ "?a"+i+" ?"+concept.toLowerCase()+". ");
        }
        res.add("}");
        res.add(" } USING "+new File(file).getName()+" ");
        res.add("where {");
        for (int i=0;i<concepts.size();i++){
            res.add("BIND(URI(CONCAT(");
//            "BIND(URI(CONCAT(\""+NS_WOT+"\",\""+metadata_avp.get("name")+"\")) as ?device). ",
//                    "BIND(URI(CONCAT(\""+NS_WOT+"\",\""+metadata_avp.get("hasLocation")+"\")) as ?region). ",
        }
        System.out.println(StrUtils.strjoinNL(res));

        return StrUtils.strjoinNL(res);
    }

}
