package com.yyn.util;

import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.util.StringUtils;
import org.apache.jena.util.FileManager;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by koala on 2017/5/23.
 */
public class ResolveRule {
    public static String getSprqlPre(String file){
        OntModel ontModel = ModelFactory.createOntologyModel();
        FileManager.get().readModel(ontModel,file);
        Map<String,String> map = ontModel.getNsPrefixMap();
        Iterator<String> keys = map.keySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (keys.hasNext()){
            String key = keys.next();
            if ("".equals(key)){
                continue;
            }
            String str = "PREFIX "+key+": <"+map.get(key)+"> ";
            sb.append(str+"\n");
        }
        String res = sb.toString();
        System.out.println(res);
        return res.substring(0,res.length()-1);

    }

//    @prefix education_bupt: <http://www.semanticweb.org/yangyunong/ontologies/2015/11/education_bupt#>.
//    @include <RDFS>.
    public static String getRulePre(String file){
        OntModel ontModel = ModelFactory.createOntologyModel();
        FileManager.get().readModel(ontModel,file);
        Map<String,String> map = ontModel.getNsPrefixMap();
        Iterator<String> keys = map.keySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (keys.hasNext()){
            String key = keys.next();
            if ("".equals(key)){
                continue;
            }
            String str = "@prefix "+key+": <"+map.get(key)+">. ";
            sb.append(str+"\n");
        }
        sb.append("@include <RDFS>.");
        String res = sb.toString();
        System.out.println(res);
        return res;

    }
    public static boolean parseRule(String rule){
        if (rule.startsWith("@")){
            return true;
        }
        return false;
    }
}
