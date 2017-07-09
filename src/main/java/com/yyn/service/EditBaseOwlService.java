package com.yyn.service;

import com.yyn.dao.BaseDeviceDao;
import com.yyn.model.SensorConfig;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by koala on 2017/5/17.
 */

@Service
public class EditBaseOwlService {

    @Autowired
    private BaseDeviceDao mDao;


    public WotProperty getWotProperty(String file){
        System.out.println(file) ;
        OntModel model = ModelFactory.createOntologyModel();
        WotProperty property = new WotProperty();
        FileManager.get().readModel(model,new File(file).getAbsolutePath());
        OntClass ontClass = model.getOntClass("https://raw.githubusercontent.com/minelabwot/SWoT/master/swot-o.owl#PropertyLabel");

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

    public List<Map<String,String>> selectAllDevice(String tableName,String type){
        System.out.println(tableName);
        return mDao.getDeviceByTable(tableName,type);

    }

    public Integer getLastId(String tableName){
        if (mDao.searchLastId(tableName) == null){
            return 0;
        }
        return mDao.searchLastId(tableName);
    }

    public String pareSparql(String file,WotProperty property, HttpServletRequest request,int id,String uri,String type){
        String prefix = ResolveRule.getSprqlPre(file);
        List<String> concepts = property.getConcepts();
        Map<String,String> map = property.getConceptUriMap();
        List<String> res = new ArrayList<>();
        res.add(prefix);

        String uriPre = "swot";
        String device_type;
        String state;
        if("Sensor".equals(type)){
            device_type = "ssn:"+type;
            state = "?device swot:currentStatus swot:nomal. ";
        } else{
            device_type = "<http://www.irit.fr/recherches/MELODI/ontologies/SAN#Actuator>";
            state = "?device swot:currentState swot:off. " +
                    "?device swot:currentStatus swot:nomal. ";
        }
        res.add("INSERT{");
        res.add("GRAPH "+uriPre+":sensor_annotation {");
        res.add("?device rdf:type "+device_type+". ");
        res.add("?device swot:deviceID \""+id+"\"^^xsd:string. ");
        res.add(state);

        concepts.remove("name");
        concepts.remove("description");
        String NS = map.get(concepts.get(0)).split("#")[0]+"#";
        for (int i=0;i<concepts.size();i++){
            String concept = concepts.get(i);
            res.add("?"+concept.toLowerCase()+" rdf:type swot:"+concept+". ");

            res.add("?device"+ " ?a"+i+" ?"+concept.toLowerCase()+". ");
        }

        res.add("?entitytype swot:defaultObserved swot:Temperature. ");
        res.add("}");
        res.add("} USING "+ uriPre+":sensor_annotation ");
        res.add("where {");
        for (int i=0;i<concepts.size();i++){
            String concept = concepts.get(i);
            res.add("?a"+i+" rdfs:domain "+device_type+" ;");
            res.add("rdfs:range swot:"+concept+".");
            res.add("BIND(URI(CONCAT(\""+NS+"\",\""+request.getParameter(concepts.get(i))+"\")) as ?"+concepts.get(i).toLowerCase()+"). ");
        }
        res.add("BIND(URI(CONCAT(\""+NS+"\",\""+request.getParameter("name")+"\")) as ?device) }");
        return StrUtils.strjoinNL(res);
    }


    public List<String> getSensorType(String file){
        List<String> sensorTypeList = new ArrayList<>();
        OntModel model = ModelFactory.createOntologyModel();
        FileManager.get().readModel(model,new File(file).getAbsolutePath());
        OntClass ontClass = model.getOntClass("https://raw.githubusercontent.com/minelabwot/SWoT/master/swot-o.owl#SensorProperty");
        if (ontClass == null){
            return null;
        }
        ExtendedIterator<OntClass> iterable = ontClass.listSubClasses();
        if (iterable == null){
            return null;
        }
        while (iterable.hasNext()){
            OntClass ontClass1 = iterable.next();
            if (ontClass1 != null && ontClass1.getLocalName() != null){
                sensorTypeList.add(ontClass1.getLocalName());
                System.out.println(ontClass1.getLocalName());
            }

        }
        return sensorTypeList;
    }

    public List<SensorConfig> getSensorConfig(List<String> typeList,File root){
        Properties proper = new Properties();
        try {
            proper.load(new FileInputStream(new File(root,"config.properties")));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        List<SensorConfig> configList = new ArrayList<>();
        for (String type:typeList){
            SensorConfig config = new SensorConfig();
            config.setName(type);
            if (proper.get(type+"_high") == null){
                proper.put(type+"_high","0.0");
                config.setHigh("0.0");
            }else{
                proper.setProperty(type+"_high",proper.getProperty(type+"_high"));
                config.setHigh(proper.getProperty(type+"_high"));
            }

            if (proper.get(type+"_low") == null){
                proper.put(type+"_low","0.0");
                config.setLow("0.0");
            }else{
                proper.setProperty(type+"_low",proper.getProperty(type+"_low"));
                config.setLow(proper.getProperty(type+"_low"));
            }
            configList.add(config);
        }
        OutputStreamWriter outputStream = null;
        try {
            outputStream = new OutputStreamWriter(new FileOutputStream(new File(root,"config.properties")));
            proper.store(outputStream, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return configList;
    }
}
